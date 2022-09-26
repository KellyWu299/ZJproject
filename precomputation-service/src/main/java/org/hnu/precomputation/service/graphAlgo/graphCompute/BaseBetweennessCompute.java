package org.hnu.precomputation.service.graphAlgo.graphCompute;

import org.hnu.precomputation.service.graphAlgo.util.GraphUtil;
import java.util.*;
import java.util.stream.Collectors;

public class BaseBetweennessCompute {
    private int N;
    private Map<Object, Integer> id2index;
    private Map<Integer, Object> index2Id;
    private static final Double INF = Double.MAX_VALUE;   // 最大值

    private Map<Object, List<Object>> neighborMap;
    private boolean isDirected;
    private boolean isNormalized;

    private double diameter;
    private double avgDist;

    private double[] betweenness;
    private double[] closeness;
    private double[] eccentricity;

    private Map<Object, Double> betweennessMap;
    private Map<Object, Double> closenessMap;
    private Map<Object, Double> eccentricityMap;

    public BaseBetweennessCompute(Map<Object, List<Object>> neighborMap, boolean isDirected, boolean isNormalized) {
        //初始化
        N = neighborMap.size();
        id2index = new HashMap<>();
        index2Id = new HashMap<>();
        this.neighborMap = neighborMap;
        this.isDirected = isDirected;
        this.isNormalized = isNormalized;

        int index = 0;
        for (Object id : neighborMap.keySet()) {
            id2index.put(id, index);
            index2Id.put(index, id);
            index++;
        }

        betweenness = new double[N];
        closeness = new double[N];
        eccentricity = new double[N];

        betweennessMap = new HashMap<>();
        closenessMap = new HashMap<>();
        eccentricityMap = new HashMap<>();
    }

    public void execute() {
        this.calculateDistanceMetrics();
    }

    public void calculateDistanceMetrics() {
        double totalPaths = 0.0D;
        Iterator<Object> nodeIter = neighborMap.keySet().iterator();
        while (nodeIter.hasNext()) {
            Object id = nodeIter.next();
            int s = id2index.get(id);
            Stack<Integer> S = new Stack<>();
            LinkedList<Integer>[] P = new LinkedList[N];
            double[] theta = new double[N];
            int[] d = new int[N];
            this.setInitParametetrsForNode(P, theta, d, s);
            LinkedList<Integer> Q = new LinkedList<>();
            Q.addLast(s);

            while (!Q.isEmpty()) {
                int v = Q.removeFirst();
                S.push(v);
                List<Integer> nbrs = neighborMap.get(index2Id.get(v)).stream().map(i->id2index.get(i)).collect(Collectors.toList());
                Iterator<Integer> nbrIter = nbrs.iterator();

                while (nbrIter.hasNext()) {
                    int w = nbrIter.next();
                    if (d[w] < 0) {
                        Q.addLast(w);
                        d[w] = d[v] + 1;
                    }
                    if (d[w] == d[v] + 1) {
                        theta[w] += theta[v];
                        P[w].addLast(v);
                    }
                }
            }

            double reachable = 0.0D;

            for(int i = 0; i < N; ++i) {
                if (d[i] > 0) {
                    avgDist += d[i];
                    eccentricity[s] = (Math.max(eccentricity[s], d[i]));
                    closeness[s] += d[i];
                    diameter = Math.max(this.diameter, d[i]);
                    ++reachable;
                }
            }

            if (reachable != 0.0D) {
                closeness[s] = closeness[s] == 0.0D ? 0.0D :reachable /closeness[s];
            }

            totalPaths += reachable;
            double[] delta = new double[N];
            while (!S.isEmpty()) {
                int w = S.pop();
                int u;
                for (ListIterator<Integer> iter1 = P[w].listIterator(); iter1.hasNext(); delta[u] += theta[u] / theta[w] * (1.0D + delta[w])) {
                    u = iter1.next();
                }

                if (w!=s){
                    betweenness[w] += delta[w];
                }
            }
        }
        avgDist = totalPaths == 0 ? 0.0D : avgDist / totalPaths;
        this.calculateCorrection();
    }

    private void setInitParametetrsForNode(LinkedList<Integer>[] P, double[] theta, int[] d, int index) {
        for(int j = 0; j < N; ++j) {
            P[j] = new LinkedList<>();
            theta[j] = 0.0D;
            d[j] = -1;
        }

        theta[index] = 1.0D;
        d[index] = 0;
    }

    private void calculateCorrection() {
        for (int i = 0; i < N; i++) {
            if (!isDirected) {
                betweenness[i] /= 2.0D;
            }
            if (isNormalized) {
                int divd =  isDirected ? (N - 1) * (N - 2) : (N - 1) * (N - 2) / 2;
                betweenness[i] /= divd;
            }
            Object id = index2Id.get(i);
            closenessMap.put(id, GraphUtil.round2DecimalDouble(closeness[i]));
            eccentricityMap.put(id, GraphUtil.round2DecimalDouble(eccentricity[i]));
            betweennessMap.put(id, GraphUtil.round2DecimalDouble(betweenness[i]));
            avgDist = GraphUtil.round2DecimalDouble(avgDist);
            diameter = GraphUtil.round2DecimalDouble(diameter);
        }
    }


    public Double getDiameter() {
        return diameter;
    }

    public Double getAvgDist() {
        return avgDist;
    }

    public Map<Object, Double> getBetweennessMap() {
        return betweennessMap;
    }

    public Map<Object, Double> getClosenessMap() {
        return closenessMap;
    }

    public Map<Object, Double> getEccentricityMap() {
        return eccentricityMap;
    }
}
