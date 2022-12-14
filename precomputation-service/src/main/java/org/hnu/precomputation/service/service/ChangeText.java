package org.hnu.precomputation.service.service;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

//用于importer导入，提前将配置文件修改
public class ChangeText {
    public String readFileContent(String BasefilePath,String newAddress) {
        BufferedReader br = null;
        String line = null;
        StringBuffer bufAll = new StringBuffer();  //保存修改过后的所有内容，不断增加
        try {
            // 避免中文乱码问题 GBK编码
            br = new BufferedReader(new InputStreamReader(new FileInputStream(BasefilePath), "GBK"));
            while ((line = br.readLine()) != null) {
                // 修改内容核心代码
                // 此处根据实际需要修改某些行的内容
                if (line.startsWith("  - path:")) {
                    line = "  - path: "+ newAddress;
                    bufAll.append(line).append("\n");
                }
                // 如果不用修改, 则按原来的内容回写
                else {
                    bufAll.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        return bufAll.toString();
    }
    //写回文件
    public void writeFile(String filePath, String content) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    bw = null;
                }
            }
        }
    }

    public void change(String name) {
        String BasefilePath = "precomputation-service/src/main/resources/NebulaEdgeFile/example.yaml";
        String NewAddress = "./"+name;
        ChangeText changetext = new ChangeText();
        changetext.writeFile(BasefilePath, changetext.readFileContent(BasefilePath,NewAddress));
    }



    /*图空间*/
    public void ChangeSpace(String GraphName) {

        Replace(GraphName,"precomputation-service/src/main/resources/NebulaEdgeFile/example.yaml");

    }

    public void Replace(String newString,String path){
        String oldString = "";
        ArrayList<String> strings = new ArrayList<String>();
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String s;//读取的每一行数据
            while ((s=br.readLine()) != null) {
                if (s.startsWith("  space:")) {
                    StringTokenizer st = new StringTokenizer(s, " ");
                    st.nextToken();
                    oldString = st.nextToken();
                    break;
                }
            }
            BufferedReader br1 = new BufferedReader(new FileReader(file));
            while ((s=br1.readLine()) != null) {
                if (s.contains(oldString)) {
                    s = s.replace(oldString, newString);
                }
                strings.add(s);//将数据存入集合
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String string : strings) {
                bw.write(string);//一行一行写入数据
                bw.newLine();//换行
            }
            bw.close();

        }catch (Exception e){
            System.out.println(e);
        }
    }



}

