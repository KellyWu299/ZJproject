package org.hnu.precomputation.service.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.hnu.precomputation.common.model.Nebula.*;
import org.hnu.precomputation.common.model.api.NebulaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.hnu.precomputation.common.model.api.NebulaConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class NebulaGraphService {
    private final static Logger logger = LoggerFactory.getLogger("NebulaGraphService");
    @Autowired
    NebulaTemplate nebulaTemplate;
    @Autowired
    SqlBuildUtils sqlBuildUtils;
    /*初始化*/
    public class NebulaConfig {
        @Bean

            public NebulaPool nebulaPool () throws Exception {
                NebulaPool pool = new NebulaPool();
                NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
                nebulaPoolConfig.setMaxConnSize(1000);
                List<HostAddress> addresses = Arrays.asList(new HostAddress("127.0.0.1", 9669));
                boolean init = pool.init(addresses, nebulaPoolConfig);
                if (!init) {
                    throw new RuntimeException("NebulaGraph init err !");
                } else {
                    log.info("NebulaGraph init Success ！");
                }
                return pool;
            }


            @Bean
            @Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
            public Session session (NebulaPool nebulaPool){
                try {
                    Session session = nebulaPool.getSession("root", "123456", false);
                    session.execute(NebulaConstant.USE+"NebulaTest"+NebulaConstant.SEMICOLON);
                    return session;
                } catch (Exception e) {
                    log.error("get nebula session err , {} ", e.toString());
                }
                return null;
        }
    }

    //将上传的数据集文件报存到本地并且调用命令行启动importer导入数据集
    public void OpenNebula(MultipartFile f, String GraphName) throws InterruptedException, IOException {
        //报存上传的文件
        if (f.isEmpty()) {
//            return "false";
        }
        String fileName = f.getOriginalFilename();
        File dest = new File(new File("importer").getAbsolutePath()+ "/" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            f.transferTo(dest); // 保存文件
          //  return "true";
        } catch (Exception e) {
            e.printStackTrace();
           // return "false";
        }

        //修改配置文件
        ChangeText changeText = new ChangeText();
        changeText.change(fileName);
        changeText.ChangeSpace(GraphName);



//        String BasefilePath = "E:\\desk\\nebula-importer\\nebula-importer\\example.yaml";
//        String NewAddress = "./newCsv.csv";
//        ChangeText changetext = new ChangeText();
//        changetext.writeFile(BasefilePath, changetext.readFileContent(BasefilePath,NewAddress));

        //控制台调用importer
        System.out.println(System.getProperty("GBK"));//显示当前语言编码
        Process p;//命令进程创建
//        String fname=f.getName();
        String[] cmd = new String[]{"cmd","/C","nebula-importer-windows-amd64-v3.1.0.exe","--config","example.yaml"};//命令，按空格分割，windows命令行前两个cmd，/C一定要加

        try {
            p = Runtime.getRuntime().exec(cmd,null,new File("importer")) ;//后面还有俩参数，第二个一般是null，第三个是命令的文件目录，默认是java所在工程目录
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            //把命令输出打印
            while((line = br.readLine()) != null)
                System.out.println(line);
            br.close();//输入流关闭，不关会进程错误，但我自己测试了关不关都没错
            int exitVal = p.waitFor(); //获取进程最后返回状态
            System.out.println("Process exitValue: " + exitVal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dest.delete();
    }


    //返回某一数据集所有起点和终点的边集合
    public List<serviceEdge> tasksservice(String s, String SpaceName){
        String choose = sqlBuildUtils.chooseGraph(SpaceName);
        nebulaTemplate.executeObject(choose);
        String s1 = String.format("LOOKUP ON %s YIELD edge AS e",s);
        NebulaResult<nebulaEdge> teamNebulaResult = nebulaTemplate.queryObject(s1, nebulaEdge.class);
        List<nebulaEdge> list = teamNebulaResult.getData();
        Iterator<nebulaEdge> iterator =list.iterator();
        List<serviceEdge> list1 = new ArrayList<>();
        while(iterator.hasNext()){
            String its = String.valueOf(iterator.next());
            JSONObject jsonObject = JSON.parseObject(its);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("e");

            System.out.println(jsonObject.get("e"));
            String r = (String)jsonObject1.get("rightVid");
            String l = (String) jsonObject1.get("leftVid");
            serviceEdge serviceEdge=new serviceEdge();
            serviceEdge.setLeft(r);
            serviceEdge.setRight(l);
            list1.add(serviceEdge);


        }
        System.out.println(list1);
        return list1;


    }

    //查找数据集并返回具体信息
    public Object findSpace(String s){
        String ss = String.format("DESCRIBE SPACE %s;",s);
        nebulaTemplate.executeObject(ss);
        return nebulaTemplate;
    }


    //删除数据集
    public void deleteSpace(String s){
        String ss = String.format("DROP SPACE [IF EXISTS] %s",s);
        nebulaTemplate.executeObject(ss);

    }







}