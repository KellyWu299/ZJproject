package org.hnu.precomputation.service.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.hnu.precomputation.common.model.Nebula.*;
import org.hnu.precomputation.service.Impl.Pair;
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
import java.util.stream.Collectors;

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
            List<HostAddress> addresses = Arrays.asList(new HostAddress("192.168.70.184", 9669));
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

                return session;
            } catch (Exception e) {
                log.error("get nebula session err , {} ", e.toString());
            }
            return null;
        }
    }
    //因为nebula图空间名字不支持”.“且必须以英文字母开头所以使用此方法转化
    public String getGraphName(String n){
        String temp = n.replaceFirst("\\.","_");
        StringBuffer stringBuffer = new StringBuffer(temp);
        stringBuffer.insert(0,"Nebula");
        return stringBuffer.toString();
    }
    //将上传的数据集文件报存到本地并且调用命令行启动importer导入数据集
    public void OpenNebula(MultipartFile f) throws Exception {
        //报存上传的文件
        if (f.isEmpty()) {
//            return "false";
        }
        String fileName = f.getOriginalFilename();
        File dest = new File(new File("precomputation-service/src/main/resources/NebulaEdgeFile").getAbsolutePath()+ "/" + fileName);
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
        changeText.ChangeSpace(getGraphName(fileName));
        UpServer upServer = new UpServer();
        upServer.Up(fileName);
        upServer.Up("example.yaml");

        //控制台调用importer
//        upServer.excuteReturnString("cd /home/hnu/zcl");
        log.info("导入开始");
        boolean b = upServer.execute("cd /home/hnu/zcl && sudo -S ./nebula-importer-linux-amd64-v3.2.0 --config "+"example.yaml");
        if(b){
            System.out.println("add source success");
        }
        else {
            System.out.println("add source failed");

        }
        upServer.execute("cd /home/hnu/zcl && sudo -S rm -f "+fileName);
        log.info("导入结束");
    }
    //返回某一数据集所有起点和终点的边集合
    public List<Pair> GetServiceEdge(String fileName) throws IOErrorException, InterruptedException {
        List<Pair> list = new ArrayList<>();
        try {
            String filePath="precomputation-service/src/main/resources/NebulaEdgeFile/"+fileName;
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String s=null;
            while ((s = br.readLine()) != null) {
                String[] str = s.split(",");
                Integer a = Integer.parseInt(str[0]);
                Integer b = Integer.parseInt(str[1]);
                //  Long id1 = (Long) g.E(s).inV().next().id();
                //  Long id2 = (Long) g.E(s).outV().next().id();
                Pair pair = new Pair();
                pair.vertex1=a;
                pair.vertex2=b;
                list.add(pair);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //查找数据集并返回具体信息
    public NebulaResult findSpace(String s){
        String ss = String.format("DESCRIBE SPACE %s;",s);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    //删除数据集
    public String deleteSpace(String fileName){
        String ss = String.format("DROP SPACE [IF EXISTS] %s",getGraphName(fileName));
        nebulaTemplate.executeObject(ss);
        File file = new File("precomputation-service/src/main/resources/NebulaEdgeFile"+"/"+fileName);
        file.delete();
        return "delete successfully!";
    }
}