package org.hnu.precomputation.service.dao;

import org.hnu.precomputation.common.model.dataset.Dataset;

public interface DatasetDao {
    int insert(Dataset record);

    int deleteByPrimaryKey(Long id);

    Dataset selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Dataset record);
}
