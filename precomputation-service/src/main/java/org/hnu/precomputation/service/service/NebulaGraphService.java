package org.hnu.precomputation.service.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;
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


    public void OpenNebula(MultipartFile f) throws InterruptedException, IOException {

        //报存上传的文件
        if (f.isEmpty()) {
//            return "false";
        }
        String fileName = f.getOriginalFilename();
        File dest = new File(new File("C:/Users/86152/Desktop/my/zj/impoter").getAbsolutePath()+ "/" + fileName);
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



//        String BasefilePath = "E:\\desk\\nebula-importer\\nebula-importer\\example.yaml";
//        String NewAddress = "./newCsv.csv";
//        ChangeText changetext = new ChangeText();
//        changetext.writeFile(BasefilePath, changetext.readFileContent(BasefilePath,NewAddress));

        //控制台调用importer
        System.out.println(System.getProperty("GBK"));//显示当前语言编码
        Process p;//命令进程创建
//        String fname=f.getName();
        String[] cmd = new String[]{"cmd","/C","nebula-importer-windows-amd64-v3.1.0.exe","--config","C:/Users/86152/Desktop/my/zj/impoter/example.yaml"};//命令，按空格分割，windows命令行前两个cmd，/C一定要加
        String[] cmd2 = new String[]{"cmd","/C","dir"};
        String cmd3 = String.format("nebula-importer-windows-amd64-v3.1.0.exe --config C:/Users/86152/Desktop/my/zj/impoter/example.yaml");
//        cmd[0] = "cd";
//        cmd[1] = "nebula-docker-compose/";
        try {
            //执行命令，运行helloWord.class文件。
            p = Runtime.getRuntime().exec(cmd,null,new File("C:/Users/86152/Desktop/my/zj/")) ;//后面还有俩参数，第二个一般是null，第三个是命令的文件目录，默认是java所在工程目录
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
    }

    /*在节点上增加注解*/
//    @Data
//    @ClassAutoMapping("modelandclass")
//    public class ModelAndClass implements Serializable {
//
//        /**
//         * id vid
//         */
//        @FieldAutoMapping(method = "getId", type = "Long")
//        private Long id;
//
//        /**
//         * 父级ID
//         */
//        @FieldAutoMapping(method = "getPid", type = "Long")
//        private Long pid;
//        /**
//         * 名称
//         */
//        @FieldAutoMapping(method = "getName", type = "String")
//        private String name;
//
//        /**
//         * 描述
//         */
//        @FieldAutoMapping(method = "getDescription", type = "String")
//        private String description;
//
//    }
//    /*增加注解并继承边的通用属性*/
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @ClassAutoMapping("project_attributeandrelationship")
//    public class ProjectAttributeAndRelationship extends Edge implements Serializable {
//
//        @FieldAutoMapping(method = "getStartId",type = "Long")
//        private Long startId;
//
//        @FieldAutoMapping(method = "getEndId",type = "Long")
//        private Long endId;
//
//    }

//    private static void printResult(ResultSet resultSet) throws UnsupportedEncodingException {
//        List<String> colNames = resultSet.keys();
//        for (String name : colNames) {
//            System.out.printf("%15s |", name);
//        }
//        System.out.println();
//        for (int i = 0; i < resultSet.rowsSize(); i++) {
//            ResultSet.Record record = resultSet.rowValues(i);
//            for (ValueWrapper value : record.values()) {
//                if (value.isLong()) {
//                    System.out.printf("%15s |", value.asLong());
//                }
//                if (value.isBoolean()) {
//                    System.out.printf("%15s |", value.asBoolean());
//                }
//                if (value.isDouble()) {
//                    System.out.printf("%15s |", value.asDouble());
//                }
//                if (value.isString()) {
//                    System.out.printf("%15s |", value.asString());
//                }
//                if (value.isTime()) {
//                    System.out.printf("%15s |", value.asTime());
//                }
//                if (value.isDate()) {
//                    System.out.printf("%15s |", value.asDate());
//                }
//                if (value.isDateTime()) {
//                    System.out.printf("%15s |", value.asDateTime());
//                }
//                if (value.isVertex()) {
//                    System.out.printf("%15s |", value.asNode());
//                }
//                if (value.isEdge()) {
//                    System.out.printf("%15s |", value.asRelationship());
//                }
//                if (value.isPath()) {
//                    System.out.printf("%15s |", value.asPath());
//                }
//                if (value.isList()) {
//                    System.out.printf("%15s |", value.asList());
//                }
//                if (value.isSet()) {
//                    System.out.printf("%15s |", value.asSet());
//                }
//                if (value.isMap()) {
//                    System.out.printf("%15s |", value.asMap());
//                }
//            }
//            System.out.println();
//        }
//    }
    public Object tasksservice(String s){
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







}