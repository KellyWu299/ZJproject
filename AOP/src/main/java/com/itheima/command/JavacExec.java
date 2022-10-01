package com.itheima.command;

import org.apache.ibatis.annotations.Mapper;
import org.junit.Test;

import java.io.*;

@Mapper
public class JavacExec {

    public JavacExec() {
    }

    @Test
    public void javac1() {
        try {
            Runtime rt = Runtime.getRuntime();
            //javac后无具体的参数，会输出错误信息。
            Process p = rt.exec("javac");
            //获取错误信息流。
            InputStream stderr = p.getErrorStream();
            //将错误信息流输出
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            System.out.println("--------------error傻逼把是不是乱码---------------");
            while((line = br.readLine()) != null)
                System.out.println(line);
            System.out.println("等待进程完成");
            //等待进程完成。
            int exitVal = p.waitFor();
            System.out.println("Process Exitvalue: " + exitVal);
        }catch(Throwable t) {
            t.printStackTrace();
        }
    }


    @Test
    public void javac2() throws InterruptedException{
        Process p;
        String[] cmds = new String[2];
        cmds[0] = "javac";
        cmds[1] = "E:\\desk\\JAVAHOME\\AOP\\src\\test\\java\\CommandTest\\CommandTest.java";

        try {
            //执行命令，将helloWord.java文件编译为.class文件
            p = Runtime.getRuntime().exec(cmds);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null)
                System.out.println(line);
            br.close();
            System.out.println(p.exitValue());
        }catch(Exception e) {
            e.printStackTrace();
        }


//        String[] cmds1 = new String[2];
//        cmds1[0] = "java";
//        cmds1[1] = "CommandTest";
//
//        try {
//            //执行命令，运行helloWord.class文件。
//            p = Runtime.getRuntime().exec(cmds1, null, new File("E:\\desk\\JAVAHOME\\AOP\\src\\test\\java\\CommandTest\\"));
//            BufferedReader br1 = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line = null;
//            while((line = br1.readLine()) != null)
//                System.out.println(line);
//
//            System.out.println();
//            br1.close();
//            int exitVal = p.waitFor(); //获取进程最后返回状态
//            System.out.println("Process exitValue是: " + exitVal);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void javac3() throws InterruptedException{
        Process p;
        String[] cmds = new String[2];
        cmds[0] = "java";
        cmds[1] = "CommandTest";

        try {
            //执行命令，运行helloWord.class文件。
            p = Runtime.getRuntime().exec(cmds, null, new File("E:\\desk\\JAVAHOME\\AOP\\src\\test\\java\\CommandTest\\"));
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while((line = br.readLine()) != null)
                System.out.println(line);

            System.out.println();
            int exitVal = p.waitFor(); //获取进程最后返回状态
            System.out.println("Process exitValue: " + exitVal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void OpenNebula() throws InterruptedException{
        System.out.println(System.getProperty("file.encoding"));//显示当前语言编码
        System.out.println("啦啦啦");
        Process p;//命令进程创建
        String[] cmd = new String[]{"cmd","/C","java","-version"};//命令，按空格分割，windows命令行前两个cmd，/C一定要加
        String[] cmd2 = new String[]{"cmd","/C","dir"};
        String[] cmd3 = new String[]{"cmd","/C","ipconfig","/all"};
        String[] cmd4 = new String[]{"cmd","/C","cd","nebula-docker-compose/"};
        String[] cmd5 = new String[]{"cmd","/C","nebula-importer","--config","E:\\desk\\nebula-importer\\nebula-importer\\example.yaml"};



//        cmd[0] = "cd";
//        cmd[1] = "nebula-docker-compose/";
        try {
            //执行命令，运行helloWord.class文件。
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec(cmd5 ,null,new File("E:\\desk\\nebula-importer\\nebula-importer"));//后面还有俩参数，第二个一般是null，第三个是命令的文件目录，默认是java所在工程目录
//            System.out.println("---------------------------$$$$$$$$$$$$$----------------------");
//            runtime.exec(cmd2);
//            System.out.println("---------------------------$$$$$$$$$$$$$----------------------");
//            runtime.exec(cmd2);
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
}
