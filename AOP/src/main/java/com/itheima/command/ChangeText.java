package com.itheima.command;

import java.io.*;

public class ChangeText {
    public String readFileContent(String BasefilePath,String newAddress) {
        BufferedReader br = null;
        String line = null;
        StringBuffer bufAll = new StringBuffer();  //�����޸Ĺ�����������ݣ���������
        try {
            // ���������������� GBK����
            br = new BufferedReader(new InputStreamReader(new FileInputStream(BasefilePath), "GBK"));
            while ((line = br.readLine()) != null) {
                // �޸����ݺ��Ĵ���
                // �˴�����ʵ����Ҫ�޸�ĳЩ�е�����
                if (line.startsWith("  - path:")) {
                    line = "  - path:"+ newAddress;
                    bufAll.append(line).append("\n");
                }
                // ��������޸�, ��ԭ�������ݻ�д
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
    //д���ļ�
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
