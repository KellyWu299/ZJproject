package org.hnu.precomputation.service.service;

import lombok.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class jdbcUtils {
    private static final String driverClassName;
    private static final String url;
    private static final String username;
    private static final String password;
    private int a;
    //编写静态代码块 调用静态变量时，会执行静态代码块
    static{
        //1.注册驱动
        driverClassName = "com.mysql.cj.jdbc.Driver";
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application-dev.yml"));
        Properties properties = yaml.getObject();
        url = (String) properties.get("spring.datasource.druid.url");
        username = (String) properties.get("spring.datasource.druid.username");
        password = ((Integer) properties.get("spring.datasource.druid.password")).toString();

//        url="jdbc:mysql://192.168.70.184:3306/zhijiang?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true";
//        username="zhijiang";
//        password="123456";
    }
    //编写注册驱动
    public static void loadDriver(){
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //获得连接
    public static Connection getConnection(){
        Connection conn = null;
        try {
            loadDriver();
            conn = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }
    //释放资源
    public static void close(Connection conn, Statement st){
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
            conn = null;
        }
        conn = null;
        if(st != null){
            try{
                st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        st = null;
    }
    //释放资源
    public static void close(Connection conn, Statement st,ResultSet rs){
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
            conn = null;
        }
        conn = null;
        if(st != null){
            try{
                st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        st = null;
        if(rs != null){
            try{
                rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        rs = null;
    }
}
