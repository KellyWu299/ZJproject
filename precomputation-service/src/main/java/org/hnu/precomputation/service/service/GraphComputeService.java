package org.hnu.precomputation.service.service;

import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.graphAlgo.graphCompute.BaseBetweennessCompute;
import org.hnu.precomputation.service.graphAlgo.util.GraphUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GraphComputeService {

    private final static Logger logger = LoggerFactory.getLogger("GraphComputeService");

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private JanusGraphService janusGraphService;
    // 根据id计算指定图数据集的betweenness
    public Map<Object, Double> getBetweenness(Long id){
        Dataset dataset = datasetService.queryDataset(id);  //根据id获取图元数据
        ArrayList<Pair> g =  janusGraphService.getGraph(dataset.getVertexProperty(), dataset.getEdgeProperty());  //获取图数据集
        Map<Object, List<Object>> mMap = GraphUtil.gFormat(g);  //格式转换
        BaseBetweennessCompute baseBetweennessCompute = new BaseBetweennessCompute(mMap, false, false);
        baseBetweennessCompute.execute();  //计算BC
        return  baseBetweennessCompute.getBetweennessMap();

    }
}
