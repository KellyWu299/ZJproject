package com.itheima.dao;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Dao {
    public void save();

    public void update();

    public void delete();
}
