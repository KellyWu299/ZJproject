package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import org.hnu.precomputation.service.graphAlgo.util.GraphUtil;
import org.hnu.precomputation.service.service.Pair;

import java.util.ArrayList;
import java.util.Map;

public class optBSearchAlgo {
    public static Map<Integer, Float> optbsearch(ArrayList<long[]> pairs) {
        JNI topk_optbsearch = new JNI();
        Map<Integer, Float> res = topk_optbsearch.callOptBSearch(pairs);
        return res;
    }
}
