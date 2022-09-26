package org.hnu.precomputation.service.service;

import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.graphAlgo.graphCompute.BaseBetweennessCompute;
import org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness.adjMatrixAlgo;
import org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness.baseBSearchAlgo;
import org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness.optBSearchAlgo;
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

    public Map<Object, Double> getBetweenness(Long id){
        System.out.println("start to get graph by ID ................");
        long startTime=System.currentTimeMillis();
        Dataset dataset = datasetService.queryDataset(id);  //根据id获取图元数据
        ArrayList<Pair> g =  janusGraphService.getGraph(dataset.getVertexProperty(), dataset.getEdgeProperty());  //获取图数据集
        long endTime=System.currentTimeMillis();
        System.out.println("获取图数据时间： "+(endTime-startTime)+" ms");
        Map<Object, List<Object>> mMap = GraphUtil.gFormat(g);  //格式转换
        System.out.println("start to compute betweenness ................");
        BaseBetweennessCompute baseBetweennessCompute = new BaseBetweennessCompute(mMap, false, false);
        baseBetweennessCompute.execute();  //计算BC
        return  baseBetweennessCompute.getBetweennessMap();
    }

    public ArrayList<Pair> gGraph(Long id) {
        System.out.println("start to get graph by ID ................");
        long startTime = System.currentTimeMillis();
        Dataset dataset = datasetService.queryDataset(id);
        ArrayList<Pair> g =  janusGraphService.getGraph(dataset.getVertexProperty(), dataset.getEdgeProperty());
        long endTime = System.currentTimeMillis();
        System.out.println("获取图数据时间： " + (endTime-startTime) + " ms");
        return g;
    }

    public Map<Integer, Float> gEgoUsingBaseBSearch(Long id) {
        ArrayList<Pair> g = gGraph(id);
        ArrayList<long[]> pairs = GraphUtil.gFormatForEgo(g);
        System.out.println("start to compute ego betweenness using basebsearch...............");
        return  baseBSearchAlgo.basebsearch(pairs);
    }

    public Map<Integer, Float> gEgoUsingOptBSearch(Long id) {
        ArrayList<Pair> g = gGraph(id);
        ArrayList<long[]> pairs = GraphUtil.gFormatForEgo(g);
        System.out.println("start to compute ego betweenness using optbsearch...............");
        return optBSearchAlgo.optbsearch(pairs);
    }

    public Map<Integer, Float> gEgoUsingAdjMatrix(Long id) {
        ArrayList<Pair> g = gGraph(id);
        ArrayList<long[]> pairs = GraphUtil.gFormatForEgo(g);
        System.out.println("start to compute ego betweenness using adjacent matrix...............");
        return adjMatrixAlgo.adjMatrix(pairs);
    }
}
