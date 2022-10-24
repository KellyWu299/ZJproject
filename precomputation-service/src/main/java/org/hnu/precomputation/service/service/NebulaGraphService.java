package org.hnu.precomputation.service.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.binding.StringFormatter;
import com.sun.prism.shader.Solid_ImagePattern_Loader;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.hnu.precomputation.common.model.Nebula.*;
//import org.hnu.precomputation.common.model.Nebula.*;
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
    public void OpenNebula(MultipartFile f, String GraphName) throws Exception {
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

        String name = getGraphName(GraphName);
        changeText.change(fileName);
        changeText.ChangeSpace(getGraphName(GraphName));
        UpServer upServer = new UpServer();
        upServer.Up(fileName);
        upServer.Up("example.yaml");

        //控制台调用importer
//        upServer.excuteReturnString("cd /home/hnu/zcl");
        boolean b = upServer.execute("cd /home/hnu/zcl && sudo -S ./nebula-importer-linux-amd64-v3.2.0 --config "+"example.yaml");
        if(b){
            System.out.println("add source success");
        }
        else {
            System.out.println("add source failed");

        }
        upServer.execute("cd /home/hnu/zcl && sudo -S rm -f "+fileName);
        dest.delete();
    }
    //返回某一数据集所有起点和终点的边集合
    public List<Pair> GetServiceEdge(String SpaceName) throws IOErrorException, InterruptedException {
        String choose = sqlBuildUtils.chooseGraph(SpaceName);
        String cI = sqlBuildUtils.createEIndex("nebulaEdge");
        NebulaResult nebulaResult1 = nebulaTemplate.executeObject(choose+cI);
        System.out.println(nebulaResult1);
        Thread.currentThread().sleep(20000);
        NebulaResult nebulaResult2 = nebulaTemplate.executeObject(choose+"REBUILD EDGE INDEX nebulaEdge_index");
        System.out.println(nebulaResult2);
        Thread.currentThread().sleep(20000);
        String s1 = String.format("LOOKUP ON %s YIELD edge AS e","nebulaEdge");
        NebulaResult<Pair> teamNebulaResult = nebulaTemplate.queryObject(choose+s1, Pair.class);
        List<Pair> list = teamNebulaResult.getData();
        Iterator<Pair> iterator =list.iterator();
        List<Pair> list1 = new ArrayList<>();
        while(iterator.hasNext()){
            String its = String.valueOf(iterator.next());
            JSONObject jsonObject = JSON.parseObject(its);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("e");
            //System.out.println(jsonObject.get("e"));
            String r = (String)jsonObject1.get("rightVid");
            String l = (String) jsonObject1.get("leftVid");
            Integer rr = Integer.valueOf(r);
            Integer ll = Integer.valueOf(l);
            Pair pair =new Pair();
            pair.setVertex1(rr);
            pair.setVertex2(ll);
            list1.add(pair);
        }
        System.out.println(list1);
        return list1;


    }
    //查找数据集并返回具体信息
    public NebulaResult findSpace(String s){
        String ss = String.format("DESCRIBE SPACE %s;",s);
        NebulaResult nebulaResult = nebulaTemplate.executeObject(ss);
        return nebulaResult;
    }
    //删除数据集
    public void deleteSpace(String s){
        String ss = String.format("DROP SPACE [IF EXISTS] %s",s);
        nebulaTemplate.executeObject(ss);

    }







}