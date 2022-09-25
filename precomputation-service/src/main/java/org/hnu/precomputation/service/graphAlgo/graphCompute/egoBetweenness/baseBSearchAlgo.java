package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import org.hnu.precomputation.service.service.Pair;

import java.util.ArrayList;
import java.util.Map;

public class baseBSearchAlgo {
    public static Map<Integer, Float> basebsearch(ArrayList<Pair> arrayList) {
        JNI topk_basebsearch = new JNI();

        ArrayList<long[]> pairs = new ArrayList<long[]>();
        for (int i = 0; i < arrayList.size(); i ++) {
            long v1 = arrayList.get(i).vertex1;
            long v2 = arrayList.get(i).vertex2;
            pairs.add(new long[] {v1, v2});
        }

        Map<Integer, Float> res = topk_basebsearch.callBaseBSearch(pairs);
        //System.out.println(res);
        return res;
    }

//    public static void main(String[] args){
//        baseBSearchAlgo.basebsearch();
//    }
}
