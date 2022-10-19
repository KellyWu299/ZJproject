package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import java.util.ArrayList;
import java.util.Map;

public class baseBSearchAlgo {
    public static Map<Integer, Float> basebsearch(ArrayList<long[]> pairs) {
        JNI topk_basebsearch = new JNI();
        Map<Integer, Float> res = topk_basebsearch.callBaseBSearch(pairs);
        //System.out.println(res);
        return res;
    }

//    public static void main(String[] args){
//        baseBSearchAlgo.basebsearch();
//    }
}
