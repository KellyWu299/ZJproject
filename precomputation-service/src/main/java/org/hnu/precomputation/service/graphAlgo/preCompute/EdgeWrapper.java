package org.hnu.precomputation.service.graphAlgo.preCompute;

public class EdgeWrapper {
    public Object source;
    public Object target;
    public Double weight = 1.0D;

    public EdgeWrapper(Object src, Object tar) {
        this.source = src;
        this.target = tar;
    }

    public EdgeWrapper(Object src, Object tar, Double weight) {
        this.source = src;
        this.target = tar;
        this.weight = weight;
    }
}
