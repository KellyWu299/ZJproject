package org.hnu.precomputation.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hnu.precomputation.common.model.dataset.Dataset;

import java.util.List;

@Mapper
public interface DatasetDao {

    int insert(Dataset record);

    Integer deleteByPrimaryKey(Long id);

    Dataset selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Dataset record);

    @Select("SELECT * from dataset")
    List<Dataset> selectAll();
}
