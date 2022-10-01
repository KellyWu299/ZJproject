package com.itheima;

import com.itheima.dao.Dao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Dao dao = (Dao) ctx.getBean("DaoImpl");
        System.out.println(dao);
        dao.save();
    }
}
