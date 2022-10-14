package org.hnu.precomputation.service.graphAlgo.preCompute;

import org.hnu.precomputation.service.Impl.Pair;


import java.io.*;
import java.util.*;


/**
 * 用于求 When Hierarchy Meets 2-Hop-Labeling : Efficient Shortest Distance Queries on Road Networks
 * 的那篇论文的树分解
 */
public class TreeDecomposition_When {
    Map<Integer, Map<Integer,Double>> neighborMap;
    Map<Integer, Vertex> v_map;//<顶点id,顶点v> 它的keySet就是路网的顶点集V
    Map<Integer, TreeNode> node_map = new HashMap<>();//存储生成的树节点, <灰色顶点id, 树节点>
    //    Map<String, TreeNode> encode_map = new HashMap<>();//存储生成的树节点, <层次编码, 对应的树节点>
    Map<String, Integer> encode_Map = new HashMap<>();//存储生成的树节点, <层次编码, 对应的树节点的grayId>
    int deleteOrderNum = 1;//消点序的值
    TreeNode root;
    //按照 度   排序的队列，自定义了VertexComparator，排序的时候传入
    ArrayList<Vertex> q = new ArrayList<>();
    VertexComparator vc = new VertexComparator();//比较器

    public TreeDecomposition_When(ArrayList<Pair> arrayList) throws IOException {
        this.neighborMap = RoadProcess.generate_g(arrayList);
        this.v_map=RoadProcess.v_map;//<顶点id,顶点v> 的map
        generate_tree();
    }
    public void generate_tree() throws IOException {
        root = new TreeNode(-1);
        v_map = RoadProcess.v_map;//<顶点id,顶点v> 的map
        Set<Integer> v_id_set = v_map.keySet();
        //计算每个顶点的度
        for (int id : v_id_set) {
            updateDegree(id);
        }
        //全都加入q中
        for (int id : v_id_set) {
            q.add(v_map.get(id));
        }
        //排序
        q.sort(vc);//传入比较器vc
        //顶点约简过程
        while (!q.isEmpty()) {
            Vertex v = q.remove(0);//开始处理, v是灰色顶点
            //1.生成树节点
            TreeNode node = new TreeNode(v.id);
            Set<Integer> v_neibs = neighborMap.get(v.id).keySet();
            //添加X(v)的顶点
            node.neibList.add(v.id);//先添加灰色顶点
            //再添加邻居
            node.neibList.addAll(v_neibs);
            //求每个节点的neibWeight数组，用于以后求 fai数组
            for (Integer v_id : neighborMap.get(node.grayId).keySet()) {
                if (v_id == node.grayId) {
                    node.neibWeight.put(v_id,0.0);
                } else {
                    node.neibWeight.put(v_id,neighborMap.get(v.id).get(v_id));
                }
            }
            //neibWeight数组求完了

            node_map.put(v.id, node);//将该树节点存起来
            //2.让所有邻居对之间连边，注意更新权重
            //将所有邻居存到数组里
            ArrayList<Integer> list = new ArrayList<>(v_neibs);
            //双指针遍历所有邻居对
            for (int i = 0; i < list.size(); i++) {
                int s = list.get(i);//start顶点
                for (int j = i + 1; j < list.size(); j++) {
                    int e = list.get(j);//end顶点
                    //距离消除的形式：无边直接添边，有边则更新为最小
                    double new_w = 0;
                    new_w+=neighborMap.get(v.id).get(s);//s--->v的权重
                    new_w+=neighborMap.get(v.id).get(e);//v--->e的权重
                    if (!neighborMap.get(s).containsKey(e)) {//(s, e)没有边的时候加上新边
                        neighborMap.get(s).put(e,new_w);
                        neighborMap.get(e).put(s,new_w);
                    }
                    else {
                        double old_w=neighborMap.get(s).get(e);
                        if(new_w<old_w)
                        {
                            neighborMap.get(s).remove(e);
                            neighborMap.get(s).put(e,new_w);
                            neighborMap.get(e).remove(s);
                            neighborMap.get(e).put(s,new_w);
                        }
                    }
                }
            }

            //3.从图(邻接表)中删掉v
            for (Integer it : neighborMap.get(v.id).keySet()) {
                neighborMap.get(it).remove(v.id);
            }
            Set<Integer> v_neib=neighborMap.get(v.id).keySet();
            neighborMap.remove(v.id);    //????????????????????
            //4.更新邻居的度，将v加入消点序队列
            for (Integer eachv : v_neib) {
                updateDegree(eachv);
            }
            //重新排序
            q.sort(vc);
            //记录消点序
            v.deleteOrder = deleteOrderNum++;
        }
        //对树节点进行连接
        for (int id : v_id_set) {//v_id_set就是顶点集V
            int minOrder = Integer.MAX_VALUE;
            TreeNode u = null;//Xv的父节点
            TreeNode Xv = node_map.get(id);//找到节点id对应的树节点
            if (Xv.neibList.size() == 1) {
                root = Xv;//指定树根
                System.out.println(root.grayId);
            }
            if (Xv.neibList.size() > 1) {
//                ArrayList<Integer> neibList = Xv.neibList;
                for (int neib : Xv.neibList) {//不能用默认的Integer
                    if (neib == id)
                        continue;
                    int curOrder = v_map.get(neib).deleteOrder;//该邻居的消点序
                    if (curOrder < minOrder) {
                        minOrder = curOrder;
                        u = node_map.get(neib);
                    }
                }
                Xv.parent = u;//指向u
                assert u != null;
                u.child.add(Xv);
            }
        }
        enCodeTree(root);
        WriteCodeTree();
    }

    //给树编码，用于快速定位两个节点的公共祖先                     ？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？有错误
    private void enCodeTree(TreeNode root){
        LinkedList<ArrayList<TreeNode>> queue = new LinkedList<>();
        queue.add(root.child);
        root.code = "(1)";
        encode_Map.put(root.code, root.grayId);
        while (!queue.isEmpty()) {
            ArrayList<TreeNode> childs = queue.poll();
            for (int i = 0; i < childs.size(); i++) {
                TreeNode c = childs.get(i);//第i个孩子节点
                String p_code = c.parent.code;
                c.code = p_code + "(" + (i + 1) + ")";
                encode_Map.put(c.code, c.grayId);
                queue.add(c.child);
            }
        }
    }

    private void WriteCodeTree() throws IOException {
        FileOutputStream file = new FileOutputStream(FilePath.CodeMap);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file));
        for (Object a : encode_Map.keySet()) {
            bw.write(a + " " + encode_Map.get(a) + "\n");
        }
        bw.flush();
        bw.close();
    }

    //更新 度
    private void updateDegree(int id) {
        int neibNum = neighborMap.get(id).size();//邻居数，也就是度
        Vertex v = v_map.get(id);
        v.degree = neibNum;
    }
}
