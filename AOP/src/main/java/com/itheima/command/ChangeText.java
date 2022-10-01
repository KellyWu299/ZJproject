package com.itheima.command;

import java.io.*;

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
                    line = "  - path:"+ newAddress;
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

    public static void main(String[] args) {
        String BasefilePath = "E:\\desk\\nebula-importer\\nebula-importer\\example.yaml";
        String NewAddress = "./newCsv.csv";
        ChangeText changetext = new ChangeText();
        changetext.writeFile(BasefilePath, changetext.readFileContent(BasefilePath,NewAddress));
    }

}
