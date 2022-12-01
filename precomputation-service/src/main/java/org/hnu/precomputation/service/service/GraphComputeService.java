package org.hnu.precomputation.service.service;

import com.vesoft.nebula.client.graph.exception.IOErrorException;
import org.hnu.precomputation.common.model.Nebula.serviceEdge;
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
import org.hnu.precomputation.service.Impl.Pair;

import java.util.Map.Entry;

import java.util.*;

@Service
public class GraphComputeService {

    private final static Logger logger = LoggerFactory.getLogger("GraphComputeService");

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private JanusGraphService janusGraphService;

    @Autowired
    private  NebulaGraphService nebulaGraphService;

    public Map<Object, Double> getBetweenness(Long id) throws Exception{
        System.out.println("start to get graph by ID ................");
        long startTime=System.currentTimeMillis();
        Dataset dataset = datasetService.queryDataset(id);  //根据id获取图元数据
        ArrayList<Pair> g = new ArrayList<>();
        if(dataset.getSource()==2) {
             g = janusGraphService.getGraph(dataset.getEdgeProperty(), dataset.getJanusIdFileName());  //获取图数据集
        }
        if(dataset.getSource()==1){
            g = (ArrayList<Pair>) nebulaGraphService.GetServiceEdge(dataset.getName());
        }
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
        ArrayList<Pair> g =  janusGraphService.getGraph(dataset.getEdgeProperty(),dataset.getJanusIdFileName());
//        nebulaGraphService.tasksservice()
        long endTime = System.currentTimeMillis();
        System.out.println("获取图数据时间： " + (endTime-startTime) + " ms");
        return g;
    }

    public List<Pair> gGraph1(Long id) throws Exception {
        System.out.println("start to get graph by ID ................");
        long startTime = System.currentTimeMillis();
        Dataset dataset = datasetService.queryDataset(id);
        List<Pair> g = nebulaGraphService.GetServiceEdge(dataset.getName());
        long endTime = System.currentTimeMillis();
        System.out.println("获取图数据时间： " + (endTime-startTime) + " ms");
        return g;
    }

    public Map<Integer, Float> gEgoUsingBaseBSearch(Long id){
        ArrayList<Pair> g = gGraph(id);
        ArrayList<long[]> pairs = GraphUtil.gFormatForEgo(g);
        System.out.println("start to compute ego betweenness using basebsearch...............");
        return  baseBSearchAlgo.basebsearch(pairs);
    }

    public Map<Integer, Float> gEgoUsingOptBSearch(Long id){
        ArrayList<Pair> g = gGraph(id);
        ArrayList<long[]> pairs = GraphUtil.gFormatForEgo(g);
        System.out.println("start to compute ego betweenness using optbsearch...............");
        return optBSearchAlgo.optbsearch(pairs);
    }

    public Map<Integer, Float> gEgoUsingAdjMatrix(Long id){
        ArrayList<Pair> g = gGraph(id);
        ArrayList<long[]> pairs = GraphUtil.gFormatForEgo(g);
        System.out.println("start to compute ego betweenness using adjacent matrix...............");
        return adjMatrixAlgo.adjMatrix(pairs);
    }

    public long calSize(Long id) {
        long vertexNum = 0;
        long edgeNum = 0;
        Set<Long> vertexSet = new HashSet<>();
        ArrayList<Pair> g = gGraph(id);
        edgeNum = g.size();
        for (Pair pair : g) {
            long v1 = pair.vertex1;
            long v2 = pair.vertex2;
            vertexSet.add(v1);
            vertexSet.add(v2);
        }
        vertexNum = vertexSet.size();
        return vertexNum + edgeNum;
    }

    public Map<Integer, Float> gEgoRes(Long id) {
        long datasetSize = calSize(id);
        Map<Integer, Float> allRes = new HashMap<>();
        Map<Integer, Float> res = new HashMap<>();
        if (datasetSize < 1000) {
            allRes =  gEgoUsingAdjMatrix(id);
        } else {
            allRes = gEgoUsingOptBSearch(id);
        }

        List<Map.Entry<Integer, Float>> list = new ArrayList<Entry<Integer, Float>>(allRes.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer, Float>>() {
            //降序排序
            public int compare(Entry<Integer, Float> o1, Entry<Integer, Float> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (int i = 0; i < 0.05 * allRes.size(); i ++) {
            res.put(i, allRes.get(i));
        }
        return res;
    }
}
