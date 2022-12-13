package org.hnu.precomputation.service.graphAlgo.preCompute;

import java.io.*;
import java.util.*;

public class DijkstraShortestPath {
    private static final Double INF = Double.MAX_VALUE;   // 最大值

    private Object source;
    private Map<Object, Double> distances;

    private Map<Object, List<EdgeWrapper>> neighborMap;


    public DijkstraShortestPath(Object source, Map<Object, List<EdgeWrapper>> neighborMap) {
        this.source = source;
        this.neighborMap = neighborMap;
        this.distances = new HashMap<>();
//        this.predecessors = new HashMap<>();
    }

    public double execute(Object target) {
        if (target == null && target.equals(source)) {
            return 0;
        }
        Set<Object> unsettledNodes = new HashSet();
        Set<Object> settledNodes = new HashSet();
        Iterator<Object> iterator =neighborMap.keySet().iterator();
        Object minDistanceNode;
        while (iterator.hasNext()) {
            minDistanceNode = iterator.next();
            distances.put(minDistanceNode, INF);
        }
        distances.put(source, 0.0D);
        unsettledNodes.add(source);
        int i=0;
        while (!unsettledNodes.isEmpty()) {
            i++;
            Double minDistance = INF;
            minDistanceNode = null;
            Iterator<Object> unsettledIter = unsettledNodes.iterator();
            while (unsettledIter.hasNext()) {  //在unsettledNodes里找到最短路径的点和对应的路径长度
                Object k = unsettledIter.next();
                Double dist = distances.get(k);
                if (minDistanceNode == null) {
                    minDistanceNode = k;
                }
                if (dist.compareTo(minDistance) < 0) {
                    minDistance = dist;
                    minDistanceNode = k;
                }
            }
            if((int)minDistanceNode==(int)target){
                return distances.get(minDistanceNode);
            }
            unsettledNodes.remove(minDistanceNode);
            settledNodes.add(minDistanceNode);
            Iterator<EdgeWrapper> nbrEdgeIter = neighborMap.get(minDistanceNode).iterator();
            while (nbrEdgeIter.hasNext()) {
                EdgeWrapper nbrEdge = nbrEdgeIter.next();
                Object nbr = nbrEdge.target;
                if (!settledNodes.contains(nbr)) {
                    Double dist = getShortestDistance(minDistanceNode) + nbrEdge.weight;
                    if (getShortestDistance(nbr) > dist) {    //对比更新
                        distances.put(nbr, dist);
                    }
                    unsettledNodes.add(nbr);
                }
            }
        }
//        System.out.println(i);
        return  0;
    }
    private Double getShortestDistance(Object target) {
        Double d = distances.get(target);
        return d == null ? INF : d;
    }

}


