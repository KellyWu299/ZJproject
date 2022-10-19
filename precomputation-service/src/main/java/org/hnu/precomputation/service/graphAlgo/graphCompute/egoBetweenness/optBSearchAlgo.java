package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import java.util.ArrayList;
import java.util.Map;

public class optBSearchAlgo {
    public static Map<Integer, Float> optbsearch(ArrayList<long[]> pairs) {
        JNI topk_optbsearch = new JNI();
        Map<Integer, Float> res = topk_optbsearch.callOptBSearch(pairs);
        return res;
    }
}
