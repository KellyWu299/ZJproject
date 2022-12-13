package org.hnu.precomputation.service.graphAlgo.preCompute;

import org.hnu.precomputation.service.Impl.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

public class H2H_Index {
    public Map<Integer, Vertex> v_map = RoadProcess.v_map;//<id, Vertex>
    public TreeNode root;
    public Map<Integer, TreeNode> node_map;

    public /*TreeNode*/void generate_H2H_Index(ArrayList<Pair> graph) throws IOException {
        TreeDecomposition_When tree = new TreeDecomposition_When(graph);
        root = tree.root;//对该root的修改不会影响TEN_Index中的root
        node_map = tree.node_map;
//        encode_map = tree.encode_map;
        LinkedList<TreeNode> q = new LinkedList<>();
        //计算每个节点的anc数组
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode Xv = q.poll();//队首元素
//            System.out.println(Xv.grayId);
            TreeNode p = Xv;//p是指针，一开始指向Xv
            ArrayList<Integer> anc = new ArrayList<>();
            while (p.parent != null) {
                anc.add(p.grayId);//按照往上走的顺序加入
                p = p.parent;
            }
            anc.add(p.grayId);//加上根
            //再反转
            Collections.reverse(anc);
            Xv.anc = anc;
            ArrayList<TreeNode> childs = Xv.child;
            q.addAll(childs);
        }
        //将每个节点中的顶点按照消点序降序排序，并求fai
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode Xv = q.poll();//队首元素
            ArrayList<Integer> neibList = Xv.neibList;//顶点id
            ArrayList<Vertex> list = new ArrayList<>();
            for (Integer eachV : neibList) {
                Vertex v = v_map.get(eachV);
                list.add(v);
            }
            list.sort((o1, o2) -> {
                return o2.deleteOrder - o1.deleteOrder;//消点序降序
            });
            neibList.clear();
            for (Vertex v : list) {
                neibList.add(v.id);
            }//消点序降序完成
            //求每个节点的fai数组
            ArrayList<Double> fai = Xv.fai;
            for (int eachV : neibList) {
                for (Integer v : Xv.neibWeight.keySet()) {//根据顶点id去neibWeight中找该Vertex
                    if (v == eachV) {
                        fai.add(Xv.neibWeight.get(v));
                    }
                }
            }//求完fai了，测试正确
            //孩子节点入队
            ArrayList<TreeNode> childs = Xv.child;
            q.addAll(childs);
        }
        //以上是处理完了DP树分解的部分，下面是实现论文中的算法5部分
        //求每个节点的pos数组和dis数组
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode Xv = q.poll();//队首元素
            ArrayList<Integer> neibList = Xv.neibList;
            ArrayList<Integer> anc = Xv.anc;
            ArrayList<Integer> pos=new ArrayList<>();

            //求pos数组
            //获取第i个顶点
            for (int Xi : neibList) {
                //去anc数组找对应的位置
                for (int j = 0; j < anc.size(); j++) {
                    if (Xi == anc.get(j)) {
                        Xv.pos.add(j);
                        pos.add(j);
                    }
                }
            }


            //求dis数组
            for (int i = 0; i < anc.size() - 1; i++) {
                double d;
                double res = Integer.MAX_VALUE;
                for (int j = 0; j < neibList.size() - 1; j++) {
                    int Xj = neibList.get(j);
//                    d = 0;
                    if (pos.get(j) > i) {
                        d = node_map.get(Xj).dis.get(i);
                    } else {
                        d = node_map.get(Xv.anc.get(i)).dis.get(pos.get(j));
                    }
                    res = Math.min(res, Xv.fai.get(j) + d);
                }

                Xv.dis.add(res);
            }
            Xv.dis.add(0.0);

            ArrayList<TreeNode> childs = Xv.child;
            q.addAll(childs);
        }
    }
}