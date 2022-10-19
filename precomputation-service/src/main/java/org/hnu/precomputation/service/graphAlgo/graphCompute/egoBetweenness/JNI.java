package org.hnu.precomputation.service.graphAlgo.graphCompute.egoBetweenness;

import java.util.ArrayList;
import java.util.Map;

public class JNI {
    public native Map<Integer, Float> callAdjMatrix(ArrayList<long[]> values);
    public native Map<Integer, Float> callBaseBSearch(ArrayList<long[]> values);
    public native Map<Integer, Float> callOptBSearch(ArrayList<long[]> values);

    static{
        System.loadLibrary("egoBetweennessDLL");
    }
}
