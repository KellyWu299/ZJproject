package org.hnu.precomputation.service.graphAlgo.preCompute;

import java.util.Comparator;

public class VertexComparator implements Comparator<Vertex>{
    @Override
    public int compare(Vertex o1, Vertex o2) {
        return o1.degree - o2.degree;
    }
}