package org.hnu.precomputation.service.dao;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.model.dataset.index;


import java.util.ArrayList;
import java.util.List;

@Mapper
public interface IndexDao extends BaseMapper<index> {
    index selectById(@Param("tableName")String tableName,@Param("id")int id);
    index selectByIdCode(@Param("tableName")String tableName,@Param("code")String code);
    void delectAll(@Param("tableName")String tableName);
    void creatTable(String tableName);
    int existTable(String tableName);
    Dataset getFileId(@Param("id")Integer id);
}
