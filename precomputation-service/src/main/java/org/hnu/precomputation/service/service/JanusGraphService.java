package org.hnu.precomputation.service.service;

import com.datastax.dse.driver.api.core.graph.DseGraph;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.hnu.precomputation.common.model.dataset.JanusSchema;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.*;
import org.janusgraph.diskstorage.BackendException;
import org.janusgraph.graphdb.database.management.ManagementSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.datastax.dse.driver.api.core.graph.DseGraph.g;

@Service
public class JanusGraphService  {
    private final static Logger logger = LoggerFactory.getLogger("JanusGraphService");
    String path="precomputation-service/src/main/resources/janusgraph-cql.properties";
    JanusGraph graph;
    String edgeLabel="E_Label";
    String vertexLabel="V_Label";
    String vertexProperty="V_property";
    String egdeProperty="E_property";
    List<Object> edgeIdlist=new ArrayList<>();
    JanusGraphManagement mgmt;
    GraphTraversalSource g;



    @PostConstruct
    private void init() throws Exception {

        JanusGraph graph = JanusGraphFactory.open(path);
        this.graph=graph;
        this.mgmt=graph.openManagement();
        this.g = graph.traversal();
        //初始化schema
        if(!graph.containsPropertyKey(vertexProperty)) {
            JanusGraphManagement  mgmt = graph.openManagement();
            EdgeLabel EL = mgmt.makeEdgeLabel(edgeLabel).make();
            VertexLabel VL = mgmt.makeVertexLabel(vertexLabel).make();
            PropertyKey VP = mgmt.makePropertyKey(vertexProperty).dataType(Integer.class).make();
            PropertyKey EP = mgmt.makePropertyKey(egdeProperty).dataType(String.class).make();
            mgmt.addProperties(EL, EP);
            mgmt.addProperties(VL, VP);
          /*  mgmt.buildIndex("searchVertex",Vertex.class).addKey(VP).buildCompositeIndex();
            // ManagementSystem.awaitGraphIndexStatus(graph, "searchVertex").call();
            mgmt.commit();
            mgmt = graph.openManagement();
            mgmt.updateIndex(mgmt.getGraphIndex("searchVertex"), SchemaAction.REINDEX).get();
           */
            mgmt.commit();
        }

        logger.info("JanusGraph init.");

    }

    public String createSchema(String vertexProperty,String edgeProperty) throws Exception {
        //创建schema
         mgmt = graph.openManagement();
        PropertyKey v_property = mgmt.makePropertyKey(vertexProperty).dataType(Integer.class).make();
        PropertyKey e_property = mgmt.makePropertyKey(edgeProperty).dataType(String.class).make();
        EdgeLabel e_label = mgmt.getOrCreateEdgeLabel("E_Label");
        VertexLabel v_label = mgmt.getOrCreateVertexLabel("V_Label");
        mgmt.addProperties(e_label,e_property);
        mgmt.addProperties(v_label,v_property);
        mgmt.buildIndex(vertexProperty,Vertex.class).addKey(v_property).buildCompositeIndex();
        // ManagementSystem.awaitGraphIndexStatus(graph, "searchVertex").call();
        mgmt.commit();
        mgmt = graph.openManagement();
        mgmt.updateIndex(mgmt.getGraphIndex(vertexProperty), SchemaAction.REINDEX).get();
        String s = mgmt.printSchema();
        mgmt.commit();


        return s;

    }
    public String printSchema(){
        JanusGraphManagement mgmt = graph.openManagement();
        String s=mgmt.printSchema();
        mgmt.commit();
        return s;
    }
    //顶点增删查改
    //1,顶点增加
    public long addVertexValue(String vertexproperty,Integer value){
        long id = graph.addVertex(vertexLabel).property(vertexproperty, value).element().longId();
        graph.tx().commit();
        return id;
    }
    //2,顶点删除
    public void deleteVertex(String vertexProperty,Integer value){
        GraphTraversalSource g = graph.traversal();
        try {
            g.V().has(vertexProperty).hasValue(value).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.tx().commit();
    }
    //3,根据id查询顶点
    public Vertex  selectVertexById(long id){
        GraphTraversalSource g = graph.traversal();
        Vertex next = g.V(id).next();
        g.tx().commit();
        return next;
    }
    //4.顶点删除
    public void deleteById(long id){
        GraphTraversalSource g = graph.traversal();
        if(g.V(id).hasNext()) {
            g.V(id).next().remove();
            g.tx().commit();
            System.out.println("delete successfully");
        }
        else System.out.println("vertex not exist");

    }

    //边增删改查
    //1,边增加,通过id
    public Object addEdgeById(Long id1,Long id2,String edgeProperty){
        GraphTraversalSource g = graph.traversal();
        if(g.V().hasId(id1)!=null&&g.V().hasId(id2)!=null){
            Vertex vertex1 = g.V(id1).next();
            Vertex vertex2 = g.V(id2).next();
            String value=id1+"and"+id2;
            Edge edge = vertex1.addEdge(edgeLabel, vertex2, edgeProperty,value);
            Object id = edge.id();
            edgeIdlist.add(id);
            g.tx().commit();
            return id;
        }
        else{
            System.out.println("Edge hashcode not exist");
            return null;
        }
    }
    //2,边删除
    public String deleteEdge(long id1,long id2,String edgeProperty){
        GraphTraversalSource g = graph.traversal();
        if(g.V().hasId(id1).next()!=null&&g.V().hasId(id2).next()!=null){
            String value=id1+"and"+id2;
            g.E().has(edgeProperty, value).drop().iterate();
            return "delete successfully";
        }
        else{
            return "edge not exists";
        }
    }
    //3,边查询
    public Object selectEdgeById(long id1,long id2,String edgeProperty){
        GraphTraversalSource g = graph.traversal();
        if(g.V().hasId(id1)!=null&&g.V().hasId(id2)!=null){
            String value=id1+"and"+id2;
            Object id = g.E().has(edgeLabel,edgeProperty, value).next().id();
            return id;
        }
        else{
            return "edge not exists";
        }
    }

    //以下功能适用于批量处理
    //1,数据批量增加
    public String putFile(MultipartFile file, String vertexProperty,String edgeProperty) throws Exception {
        System.out.println("test1");
        g = graph.traversal();
        //为新的property构建索引
        PropertyKey V_propertyKey = mgmt.makePropertyKey(vertexProperty).dataType(Integer.class).make();
        PropertyKey E_propertyKey = mgmt.makePropertyKey(edgeProperty).dataType(Integer.class).make();
        System.out.println("test2");
        mgmt.buildIndex(vertexProperty,Vertex.class).addKey(V_propertyKey).buildCompositeIndex();
       mgmt.buildIndex(edgeProperty,Edge.class).addKey(E_propertyKey).buildCompositeIndex();
       // ManagementSystem.awaitGraphIndexStatus(graph, "searchVertex").call();
        System.out.println("test3");
      mgmt.commit();
        mgmt = graph.openManagement();
       mgmt.updateIndex(mgmt.getGraphIndex(vertexProperty), SchemaAction.REINDEX).get();
        mgmt.updateIndex(mgmt.getGraphIndex(edgeProperty), SchemaAction.REINDEX).get();
        mgmt.commit();
        System.out.println("test4");
        mgmt = graph.openManagement();
        //导入数据
        List<Integer> list = new ArrayList<>();
        InputStream fis = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        System.out.println("test5");
        String s = null;
        Vertex vertexB, vertexA;
        System.out.println("开始添加数据:");
        int i = 1;
        while ((s = br.readLine()) != null) {
            System.out.println("test6");
            String[] str = s.split(",");
            Integer a = Integer.parseInt(str[0]);
            Integer b = Integer.parseInt(str[1]);
            if (list.contains(a)) {
                vertexA = g.V().has(vertexProperty, a).next();
            } else {
                vertexA = g.addV(vertexLabel).property(vertexProperty, a).next();

                list.add(a);
            }
            if (list.contains(b)) {
                vertexB = g.V().has(vertexProperty,b).next();
            } else {
                vertexB = g.addV(vertexLabel).property(vertexProperty, b).next();
                list.add(b);
            }
            vertexB.addEdge(edgeLabel, vertexA,edgeProperty,1);
        }

        System.out.println("vertex:"+g.V().count().next()+"  edge:"+g.E().count().next());
        graph.tx().commit();
        return "Putting database finished";
    }

    //2,数据批量查找
    public ArrayList<Pair> getGraph(String vertexProperty,String edgeProperty){
        GraphTraversalSource g = graph.traversal();
        ArrayList<Pair> arrayList = new ArrayList<>();
        List<Edge> list = g.E().has(edgeProperty).toList();
        Iterator<Edge> iterator = list.iterator();
        while(iterator.hasNext()){
            Edge edge=iterator.next();
            Long id1 = (Long) edge.inVertex().id();
            Long id2 =(Long) edge.outVertex().id();
            Pair pair = new Pair();
            pair.vertex1=id1;
            pair.vertex2=id2;
            arrayList.add(pair);
        }
        return arrayList;

    }

    //3,数据批量删除
    public String deleteGraph(String vertexProperty,String edgeProperty){
        GraphTraversalSource g = graph.traversal();
        JanusGraphManagement mgmt = graph.openManagement();
      /*  while (g.E().has(edgeProperty).hasNext()){
            System.out.println(2);
            g.E().has(edgeProperty).next().inVertex().remove();
            g.E().has(edgeProperty).next().outVertex().remove();
        }
        //  mgmt.getPropertyKey(vertexProperty).remove();
        //  mgmt.getPropertyKey(edgeProperty).remove();
        //删除顶点
       */
        GraphTraversal<Vertex, Vertex> iterate = g.V().has(vertexProperty).iterate();
        while (iterate.hasNext()){
            Vertex vertex = iterate.next();
            vertex.remove();
        }
        return "delete successfully!";

    }

    public String countGraph(){
        GraphTraversalSource g = graph.traversal();
        Long v = g.V().count().next();
        Long e =g.E().count().next();
        String res="vertex:"+v+" edge:"+e;
        return res;
    }

    //资源释放
      @PreDestroy
    public void closeGraph(){
mgmt.commit();
g.tx().commit();
        graph.close();
    }


}
