package org.hnu.precomputation.service.graphAlgo.preCompute;

import org.hnu.precomputation.service.service.JanusGraphService;
import org.hnu.precomputation.service.Impl.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 将原始的路网数据，处理成邻接表返回
 */
public class RoadProcess {
    static Map<Integer, Map<Integer,Double>> neighborMap;
    static Map<Integer, Vertex> v_map = new HashMap<>();//<顶点id, 对应的顶点>

    public static Map<Integer, Map<Integer,Double>> generate_g(ArrayList<Pair> arrayList) throws IOException {
        neighborMap=new HashMap<>();
        int a;
        int b;
        double c;
        for (Pair p:arrayList
             ) {
            a= p.vertex1.intValue();
            b=p.vertex2.intValue();
            if(a==b)
                continue;
            c = 1.0;
            Vertex a_nb = new Vertex(b);//a_nb:a的邻居
            addin(a,a_nb,c);
            Vertex b_nb = new Vertex(a);
            addin(b,b_nb,c);
            v_map.put(b, a_nb);
            v_map.put(a, b_nb);
        }
        return neighborMap;
    }
    public static void addin(int a,Vertex a_n,Double c){
        if(neighborMap.get(a)==null){
            Map<Integer,Double> NewNeigMap=new HashMap<>();
            neighborMap.put(a,NewNeigMap);
        }
        neighborMap.get(a).put(a_n.id,c);
    }


}
