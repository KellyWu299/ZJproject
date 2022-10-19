package org.hnu.precomputation.service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.hnu.precomputation.service.Impl.Pair;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class JanusGraphService  {
    private final static Logger logger = LoggerFactory.getLogger("JanusGraphService");
    String path="precomputation-service/src/main/resources/janusgraph-hbase01.properties";
    JanusGraph graph;
    String edgeLabel="E_Label";
    String vertexLabel="V_Label";
    String vertexProperty="V_property";
    String egdeProperty="E_property";
    List<Object> edgeIdlist=new ArrayList<>();
    JanusGraphManagement mgmt;
    GraphTraversalSource g;
    Graph threadedTx;
    @Autowired
     private HttpServletRequest httpServletRequest;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @PostConstruct
    private void init() throws Exception {

        JanusGraph graph = JanusGraphFactory.open(path);
        this.graph=graph;
        this.mgmt=graph.openManagement();
        this.g = graph.traversal();
        this.threadedTx = graph.tx().createThreadedTx();
        //  this.threadedTx = graph.tx().createThreadedTx();
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

    public void printThread(){
        System.out.println("active thread amount:" +threadPoolTaskExecutor.getActiveCount());
    }

    public String printSchema(){
        JanusGraphManagement mgmt = graph.openManagement();
        String s=mgmt.printSchema();
        mgmt.commit();
        System.out.println(s);
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
    //putIndex和putFile一起使用,先构建索引再导入数据集
    public void putIndex(String vertexProperty, String edgeProperty) throws Exception {
        //为新的property构建索引
        log.info("线程"+Thread.currentThread().getName()+"         构建索引...");
        PropertyKey V_propertyKey = mgmt.makePropertyKey(vertexProperty).dataType(Integer.class).make();
        PropertyKey E_propertyKey = mgmt.makePropertyKey(edgeProperty).dataType(Integer.class).make();
        mgmt.buildIndex(vertexProperty,Vertex.class).addKey(V_propertyKey).buildCompositeIndex();
        mgmt.buildIndex(edgeProperty,Edge.class).addKey(E_propertyKey).buildCompositeIndex();
        mgmt.commit();
        this.mgmt=graph.openManagement();
        log.info("线程"+Thread.currentThread().getName()+"构建索引"+vertexProperty+"   and   "+edgeProperty+"完成");
        this.mgmt = graph.openManagement();
    }

    public String putFile(MultipartFile file, String vertexProperty,String edgeProperty,String edgeIdFileName) throws Exception {
        //为新的property构建索引
        GraphTraversalSource gTx = threadedTx.traversal();
        //导入数据
        logger.info("线程"+Thread.currentThread().getName()+":         导入数据....");
       // List<Integer> list = new ArrayList<>();
        HashMap<Integer, Object> map = new HashMap<>();
        InputStream fis = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        //新建janusgraph边id文件
        File edgeIdFile = new File("precomputation-service/src/main/resources/janusEdgeIdFile/"+edgeIdFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(edgeIdFile);
        String s = null;
        Vertex vertexB, vertexA;
        int i = 1;
        while ((s = br.readLine()) != null) {
            String[] str = s.split(",");
            Integer a = Integer.parseInt(str[0]);
            Integer b = Integer.parseInt(str[1]);
            if (map.containsKey(a)) {
                vertexA = gTx.V(map.get(a)).next();
            } else {
                vertexA = gTx.addV(vertexLabel).property(vertexProperty, a).next();
                Object id = vertexA.id();
                // list.add(a);
                map.put(a,id);
            }
            if (map.containsKey(b)) {
                vertexB = gTx.V(map.get(b)).next();
            } else {
                vertexB =gTx.addV(vertexLabel).property(vertexProperty, b).next();
                Object id = vertexB.id();
              //  list.add(b);
                map.put(b,id);
            }
            Edge edge = vertexB.addEdge(edgeLabel, vertexA, edgeProperty, 1);
            //将边id写入文件中
           // String id = edge.id().toString();
            //将顶点id写入文件中
            String Aid = vertexA.id().toString();
            String Bid=vertexB.id().toString();
            String line=Aid+","+Bid+"\n";
            byte[] bytes = line.getBytes();
            fileOutputStream.write(bytes);
        }
        //关闭资源
        fileOutputStream.close();
        map.clear();
        return "Putting database finished";
    }

    public void commit(){
                threadedTx.tx().commit();
                this.threadedTx=graph.tx().createThreadedTx();
    }

    //2,数据批量查找
    public ArrayList<Pair> getGraph( String edgeProperty,String edgeIdFileName) throws Exception {
        logger.info("导出数据.....");
        GraphTraversalSource g = graph.traversal();
        ArrayList<Pair> arrayList = new ArrayList<>();
        //读取边id的文件,通过id取图
        String filePath="precomputation-service/src/main/resources/janusEdgeIdFile/"+edgeIdFileName;
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s=null;
        while ((s = br.readLine()) != null) {
            String[] str = s.split(",");
            Integer a = Integer.parseInt(str[0]);
            Integer b = Integer.parseInt(str[1]);
          //  Long id1 = (Long) g.E(s).inV().next().id();
          //  Long id2 = (Long) g.E(s).outV().next().id();
            Pair pair = new Pair();
            pair.vertex1=a;
            pair.vertex2=b;
            arrayList.add(pair);
        }
       /* List<Edge> list = g.E().has(edgeProperty).toList();
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
        */
        return arrayList;

    }

    //3,数据批量删除
    public String deleteGraph(String vertexProperty,String edgeProperty,String idFileName) throws Exception {
        logger.info("删除数据集....");
        String filePath="precomputation-service/src/main/resources/janusEdgeIdFile/"+idFileName;
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s=null;
        while ((s = br.readLine()) != null) {
            String[] str = s.split(",");
            Integer a = Integer.parseInt(str[0]);
            Integer b = Integer.parseInt(str[1]);
            g.V(a).drop();
            g.V(b).drop();
        }
        File file = new File(filePath);
        file.delete();
        JanusGraphIndex vertexIndex = mgmt.getGraphIndex(vertexProperty);
        JanusGraphIndex edgeIndex = mgmt.getGraphIndex(edgeProperty);
        mgmt.updateIndex(vertexIndex, SchemaAction.DISABLE_INDEX);
        mgmt.updateIndex(edgeIndex, SchemaAction.DISABLE_INDEX);
        mgmt.getPropertyKey(edgeProperty).remove();
        mgmt.getPropertyKey(vertexProperty).remove();
        mgmt.commit();
        mgmt=graph.openManagement();
        System.out.println(mgmt.printSchema());
        System.out.println("vertex:"+g.V().count().next()+"  edge:"+g.E().count().next());
        return "delete successfully!";

    }

    public String countGraph(){
        GraphTraversalSource g = graph.traversal();
        Long v = g.V().count().next();
        Long e = g.E().count().next();
        String res="vertex:"+v+" edge:"+e;
        return res;
    }

    //资源释放
//@PreDestroy
    public void closeGraph(){
        mgmt.commit();
        g.tx().commit();
        graph.close();
    }
}
