package org.hnu.precomputation.service.service;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.diskstorage.BackendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class JanusGraphService {
    private final static Logger logger = LoggerFactory.getLogger("JanusGraphService");
    JanusGraph graph;
    String edgeLabel="E_Label";
    String vertexLabel="V_Label";
    String vertexProperty="V_property";
    String egdeProperty="E_property";
    List<Object> edgeIdlist=new ArrayList<>();

    @PostConstruct
    private void init() throws BackendException {
        logger.info("JanusGraph init.");
        //判断图是否为空，空的继续创建图，非空直接打开
        String path="D:\\HNU\\ZJproject\\ZJproject-dev\\precomputation-web\\src\\main\\resources\\janusgraph-cql-cassandra.properties";
        this.graph = JanusGraphFactory.open(path);
        JanusGraphManagement mgmt = graph.openManagement();
        if(!graph.containsPropertyKey("P_vertex")) {
            this.graph = JanusGraphFactory.open(path);
            VertexLabel vertexLabel = mgmt.getOrCreateVertexLabel("L_vertex");
            PropertyKey l_vertex = mgmt.makePropertyKey("P_vertex").dataType(Integer.class).make();
            mgmt.addProperties(vertexLabel, l_vertex);
            EdgeLabel edgeLabel = mgmt.getOrCreateEdgeLabel("L_edge");
            PropertyKey l_edge = mgmt.makePropertyKey("P_edge").dataType(Integer.class).make();
            mgmt.addProperties(edgeLabel, l_edge);
        }
        System.out.println(mgmt.printSchema());
        mgmt.commit();
    }

    public String printSchema(){
        JanusGraphManagement mgmt = graph.openManagement();
        String schema = mgmt.printSchema();
        return schema;

    }
    public String putFile(MultipartFile file, String vertexProperty, String edgeProperty) throws Exception {
        GraphTraversalSource g = graph.traversal();
        List<Integer> list =  new ArrayList<>();;
        // FileInputStream fis = new FileInputStream( file);
        InputStream fis = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s = null;
        Object id1 = null,id2 = null;
        Vertex vertexB, vertexA;
        System.out.println("开始添加数据:");
        int i = 1;
        while ((s = br.readLine()) != null) {
            String[] str = s.split(",");
            Integer a = Integer.parseInt(str[0]);
            Integer b = Integer.parseInt(str[1]);
            if (list.contains(a)) {
                vertexA = g.V(id1).next();
            } else {
                vertexA = graph.addVertex(vertexLabel).property(vertexProperty, a).element();
                id1 = vertexA.id();
                list.add(a);
            }
            if (list.contains(b)) {
                vertexB = g.V(id2).next();
            } else {
                vertexB = graph.addVertex(vertexLabel).property(vertexProperty, b).element();
                id2 = vertexB.id();
                list.add(b);
            }
            String value=id1+"and"+id2;
            vertexB.addEdge(edgeLabel, vertexA,edgeProperty,value);
            System.out.println("已添加" + i++ + "条数据");
        }
        System.out.println("提交事务");
        graph.tx().commit();;
        return "Putting database finished";
    }

}
