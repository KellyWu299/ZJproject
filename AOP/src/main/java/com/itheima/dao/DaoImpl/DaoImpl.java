package com.itheima.dao.DaoImpl;
import org.apache.ibatis.annotations.Mapper;
import com.itheima.dao.Dao;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Component("DaoImpl")
public class DaoImpl implements Dao {

    private int num;
    private String description;



    public DaoImpl() {
    }

    @Override
    public void save() {
        System.out.println("DaoImpl save什么鬼");
    }

    @Override
    public void update() {
        System.out.println("DaoImpl update什么鬼");
    }

    @Override
    public void delete() {
        System.out.println("删除");
    }
}
