package org.hnu.precomputation.service.graphAlgo.preCompute;

import org.hnu.precomputation.service.Impl.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将原始的路网数据，处理成邻接表返回
 */
public class RoadProcess {
    static Map<Integer, Map<Integer,Double>> neighborMap=new HashMap<>();
    static Map<Integer, Vertex> v_map = new HashMap<>();//<顶点id, 对应的顶点>
    static Map<Object, List<EdgeWrapper>> neighborMapForDijks=new HashMap<>();

    public static Map<Integer, Map<Integer,Double>> generate_g(ArrayList<Pair> arrayList) throws IOException {
        int a;
        int b;
        double c;
        for (Pair p:arrayList
             ) {
            a= p.vertex1;
            b= p.vertex2;
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

    //Dijks:
    public static Map<Object, List<EdgeWrapper>>  generate_g_Dijks(ArrayList<Pair> arrayList) throws IOException {
        int a;
        int b;
        double c;
        for (Pair p:arrayList
        ) {
            a= p.vertex1;
            b= p.vertex2;
            if(a==b)
                continue;
            c = 1.0;
            //Dijks:
            EdgeWrapper a_nb_Dijks = new EdgeWrapper(a,b,c);//a_nb:a的邻居
            addin(a,a_nb_Dijks);
            EdgeWrapper b_nb_Dijks = new EdgeWrapper(b,a, c);
            addin(b,b_nb_Dijks);
        }
        return neighborMapForDijks;
    }
    public static void addin(int a,Vertex a_n,Double c){
        if(neighborMap.get(a)==null){
            Map<Integer,Double> NewNeigMap=new HashMap<>();
            neighborMap.put(a,NewNeigMap);
        }
        neighborMap.get(a).put(a_n.id,c);
    }

    //Dijks:
    public static void addin(int a,EdgeWrapper a_n) throws IOException{
        if(neighborMapForDijks.get(a)==null){
            List<EdgeWrapper> a_nei=new ArrayList<>();
            neighborMapForDijks.put(a,a_nei);
        }
        if(!neighborMapForDijks.get(a).contains(a_n))
            neighborMapForDijks.get(a).add(a_n);
    }
}
