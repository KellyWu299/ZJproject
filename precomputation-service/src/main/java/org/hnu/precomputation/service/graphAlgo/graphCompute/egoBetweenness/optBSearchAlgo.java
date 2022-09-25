package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import org.hnu.precomputation.service.service.Pair;

import java.util.ArrayList;
import java.util.Map;

public class optBSearchAlgo {
    public static Map<Integer, Float> optbsearch(ArrayList<Pair> arrayList) {
        JNI topk_optbsearch = new JNI();

        ArrayList<long[]> pairs = new ArrayList<long[]>();
        for (int i = 0; i < arrayList.size(); i ++) {
            long v1 = arrayList.get(i).vertex1;
            long v2 = arrayList.get(i).vertex2;
            pairs.add(new long[] {v1, v2});
        }

        Map<Integer, Float> res = topk_optbsearch.callOptBSearch(pairs);
        return res;
    }
}
