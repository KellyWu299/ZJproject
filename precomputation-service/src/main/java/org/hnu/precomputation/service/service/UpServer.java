package org.hnu.precomputation.service.service;



import com.jcraft.jsch.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class UpServer {
    private ChannelSftp channelSftp;
    private ChannelExec channelExec;
    private Session session=null;
    private Channel channel = null;
    private int timeout=60000;
    ByteArrayOutputStream err = null;
    StringBuffer outbuf = null;

    public void SshUtil(SshConfiguration conf) throws JSchException {
        System.out.println("try connect to  "+conf.getHost()+",username: "+conf.getUserName()+",password: "+conf.getPassword()+",port: "+conf.getPort());
        JSch jSch=new JSch(); //创建JSch对象
        session=jSch.getSession(conf.getUserName(), conf.getHost(), conf.getPort());//根据用户名，主机ip和端口获取一个Session对象
        session.setPassword(conf.getPassword()); //设置密码
        Properties config=new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);//为Session对象设置properties
        session.setTimeout(timeout);//设置超时
        session.connect();//通过Session建立连接
    }
    public void download(String src,String dst) throws JSchException, SftpException{
        //src linux服务器文件地址，dst 本地存放地址
        channelSftp=(ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.get(src, dst);
        channelSftp.quit();
    }
    public void upLoad(String src,String dst) throws JSchException,SftpException{
        //src 本机文件地址。 dst 远程文件地址
        channelSftp=(ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.put(src, dst);
        channelSftp.quit();
    }
    public void close(){
        try {
            if (channel != null) {
                channel.disconnect();
                channel = null;
            }
            if (session != null) {
                session.disconnect();
                session = null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public Session con() throws JSchException {
        SshConfiguration configuration=new SshConfiguration();
        configuration.setHost("192.168.70.184");
        configuration.setUserName("hnu");
        configuration.setPassword("hnucs420");
        configuration.setPort(22);
        SshUtil(configuration);
        return session;
    }
    public void Up(String name){

        try{
//            SshUtil sshUtil=new SshUtil(configuration);
//            sshUtil.download("/home/cafintech/Logs/metaData/meta.log","D://meta.log");
//            sshUtil.close();
//            System.out.println("文件下载完成");
            con();
            upLoad("importer/"+name,"/home/hnu/zcl");
            close();
            System.out.println("文件上传完成");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public int excute(String cmd) throws JSchException, IOException {
        int exitStatus = 0;

        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setInputStream(null);
        err = new ByteArrayOutputStream();
        channelExec.setErrStream(err);

        channelExec.setCommand(cmd);

        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        outbuf = new StringBuffer();
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                outbuf.append(new String(tmp, 0, i));
            }
            if (channelExec.isClosed()) {
                if (in.available() > 0) continue;
                exitStatus = channelExec.getExitStatus();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }

        channelExec.disconnect();

        return exitStatus;
    }
    public boolean execute(String command) throws Exception {
        // JSch jsch = new JSch();
        // Session session = null;
        con();
        Channel channel = null;
        try {

            /*
             * session = jsch.getSession("zhejiang", "192.168.52.191", 22);
             * session.setPassword("jiang1234"); session.setTimeout(2000);
             * Properties config = new Properties();
             * config.put("StrictHostKeyChecking", "no");
             */

            Properties config = new Properties();
            session.setConfig(config);

            channel = session.openChannel("exec");
            ChannelExec execChannel = (ChannelExec) channel;
            execChannel.setCommand(command);
            InputStream in = channel.getInputStream();
            channel.connect();
            OutputStream out = channel.getOutputStream();
            out.write(("hnucs420" + "\n").getBytes());
            out.flush();
            byte[] buffer = new byte[1024];
            for (;;) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    String s = new String(buffer, 0, i);
                }
                if (channel.isClosed()) {
                    System.out.println("SSH2 Exit: " + channel.getExitStatus());
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return false;
    }
}




