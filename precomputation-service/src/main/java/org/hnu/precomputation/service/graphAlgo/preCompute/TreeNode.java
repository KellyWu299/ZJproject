package org.hnu.precomputation.service.graphAlgo.preCompute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreeNode {
    public int grayId;//灰色顶点,X(v)的v
    public ArrayList<Integer> neibList = new ArrayList<>();//X(v)包含的顶点(含v本身), Integer指顶点id
    public TreeNode parent;
    public ArrayList<TreeNode> child = new ArrayList<>();
    public String code;//层次编码
    public ArrayList<Integer> anc = new ArrayList<>();
    public ArrayList<Double> dis = new ArrayList<>();
    public ArrayList<Integer> pos = new ArrayList<>();
    public ArrayList<Double> fai = new ArrayList<>();//权重数组 fai[i]：neibList中第i个顶点到Xv的权重
    //下面的数组，Vertex的信息不全，仅限于求上面的fai时使用
    public Map<Integer,Double> neibWeight = new HashMap<>();//X(v)包含的顶点(含v本身)，该Vertex的 w 指的是该顶点到grayId的权值

    public TreeNode(int grayId) {
        this.grayId = grayId;
    }
}