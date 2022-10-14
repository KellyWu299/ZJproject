package org.hnu.precomputation.service.dao;

import org.hnu.precomputation.common.model.dataset.Dataset;

import java.util.List;

public interface DatasetDao {
    int insert(Dataset record);

    Integer deleteByPrimaryKey(Long id);

    Dataset selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Dataset record);

    List<Dataset> selectAll();
}
