package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import org.hnu.precomputation.service.graphAlgo.util.GraphUtil;
import org.hnu.precomputation.service.service.Pair;

import java.util.ArrayList;
import java.util.Map;

public class adjMatrixAlgo {
    public static Map<Integer, Float> adjMatrix(ArrayList<long[]> pairs) {
        JNI adj = new JNI();
        Map<Integer, Float> res = adj.callAdjMatrix(pairs);
        return res;
    }
}
