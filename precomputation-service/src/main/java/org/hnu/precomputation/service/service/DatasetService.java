package org.hnu.precomputation.service.service;

import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.dao.DatasetDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatasetService {
    private final static Logger logger = LoggerFactory.getLogger("DatasetService");

    @Autowired
    private DatasetDao datasetDao;

    public void addDataset(Dataset dataset) {
        datasetDao.insert(dataset);
    }

    public Dataset queryDataset(Long id) {
        return datasetDao.selectByPrimaryKey(id);
    }

    public void update(Dataset dataset) {
        datasetDao.updateByPrimaryKey(dataset);
    }
}
