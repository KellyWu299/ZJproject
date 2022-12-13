package org.hnu.precomputation.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import org.checkerframework.checker.units.qual.A;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.model.dataset.index;
import org.hnu.precomputation.service.Impl.Pair;
import org.hnu.precomputation.service.dao.IndexDao;
import org.hnu.precomputation.service.graphAlgo.preCompute.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import javax.xml.transform.Source;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PreComputeService extends ServiceImpl<IndexDao, index> {
    private final static Logger logger = LoggerFactory.getLogger("PreComputeService");
    @Autowired
    private JanusGraphService janusGraphService;
    @Autowired
    private IndexDao indexDao;
    @Autowired
    private  NebulaGraphService nebulaGraphService;
    private QuerryByDatebase querryByDatebase=new QuerryByDatebase();



    //    public String MakeIndex(String edgeProperty, String janusIdFileName) throws IOException {
//        H2H_Index h2H_index=new H2H_Index();
//        long startTime=System.currentTimeMillis();
//        ArrayList<Pair> pairs=janusGraphService.getGraph(edgeProperty ,janusIdFileName);
//        long mid1=System.currentTimeMillis();
//        h2H_index.generate_H2H_Index(pairs);
//        long midTime = System.currentTimeMillis();
//        List<index> IndexList = new ArrayList<>();
//        for (Integer id:h2H_index.v_map.keySet()
//             ) {
//            index index = new index();
//            index.setId(id);
//            index.setCode(h2H_index.node_map.get(id).code);
//            index.setPos(OperateStringAndArray.ArrayToListForInt(h2H_index.node_map.get(id).pos));
//            index.setDis(OperateStringAndArray.ArrayToListForDouble(h2H_index.node_map.get(id).dis));
//            IndexList.add(index);
//        }
//        saveBatch(IndexList);
//        long endTime = System.currentTimeMillis();
//        return "取图数据时间"+(mid1-startTime)+"索引生成时间："+(midTime-mid1)+"\n"+"索引储存时间："+(endTime-midTime);
//    }
    public String MakeIndex(Integer Id) throws IOException, IOErrorException, InterruptedException {
        Dataset dataset=indexDao.getFileId(Id);
        int source=dataset.getSource();
        String TableName=null;
        String FileName=null;
        ArrayList<Pair> pairs=new ArrayList<>();
        long startTime=System.currentTimeMillis();
        if(source==2)
        {
            FileName=dataset.getJanusIdFileName();
            if((indexDao.existTable("janus_"+Id))==0)
            {
                indexDao.creatTable("janus_"+Id);
            }
            pairs=janusGraphService.getGraph("1" ,FileName);
            TableName="janus_"+Id;
        }
        else if (source==1)
        {
            FileName=dataset.getName();
            if((indexDao.existTable("nebula_"+Id))==0)
            {
                indexDao.creatTable("nebula_"+Id);
            }
            pairs= (ArrayList<Pair>) nebulaGraphService.GetServiceEdge(FileName);
            TableName="nebula_"+Id;
        }
        H2H_Index h2H_index=new H2H_Index();
        long mid1=System.currentTimeMillis();
        h2H_index.generate_H2H_Index(pairs);
        long midTime = System.currentTimeMillis();
        List<index> IndexList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        int i=0;
        int batch=2000;
        try {
            conn = jdbcUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into "+TableName+" (id,code,dis,pos) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            for (Integer id:h2H_index.v_map.keySet()
            ) {
                i++;
                ps.setInt(1, id); //填充占位符
                ps.setString(2, h2H_index.node_map.get(id).code);
                ps.setString(3, OperateStringAndArray.ArrayToStringForDouble(h2H_index.node_map.get(id).dis));
                ps.setString(4, OperateStringAndArray.ArrayToStringForInt(h2H_index.node_map.get(id).pos));

                //1."攒"sql
                ps.addBatch();
                if (i % batch == 0){
                    //2.执行Batch
                    ps.executeBatch();
                    //3.清空Batch
                    ps.clearBatch();
                }
            }
            if(i%batch!=0)
            {
                ps.executeBatch();
                ps.clearBatch();
            }
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jdbcUtils.close(conn, ps);
        }

        long endTime = System.currentTimeMillis();
        return "取图数据时间"+(mid1-startTime)+"索引生成时间："+(midTime-mid1)+"\n"+"索引储存时间："+(endTime-midTime);
    }
    public Map<Double,List<Long>> GetSingleIndex(int v1, int v2,Integer Id) throws IOException, IOErrorException, InterruptedException {
        Dataset dataset=indexDao.getFileId(Id);
        int source=dataset.getSource();
        String TableName=null;
        String FileName=null;
        ArrayList<Pair> pairs=new ArrayList<>();
        if(source==2)
        {
            FileName=dataset.getJanusIdFileName();
            pairs=janusGraphService.getGraph("1" ,FileName);
            TableName="janus_"+Id;
        }
        else if (source==1)
        {
            FileName=dataset.getName();
            pairs= (ArrayList<Pair>) nebulaGraphService.GetServiceEdge(FileName);
            TableName="nebula_"+Id;
        }

        long startTime=System.currentTimeMillis();
        double result_index=querryByDatebase.singleQuery(v1,v2,indexDao,TableName);
        long midTime=System.currentTimeMillis();

        Map<Object, List<EdgeWrapper>> map_Dijks=RoadProcess.generate_g_Dijks(pairs);
        DijkstraShortestPath dijkstraShortestPath=new DijkstraShortestPath(v1,map_Dijks);
        double result_Dijks=dijkstraShortestPath.execute(v2);

        long endTime = System.currentTimeMillis();

        Map<Double,List<Long>> map=new HashMap<>();
        if(result_index==result_Dijks)
        {
            List<Long> list=new ArrayList<>();
            list.add(midTime-startTime);
            list.add(endTime-midTime);
            map.put(result_index,list);
            return map;
        }
        return null;
    }

    public String delect(String tableName) {
        long startTime=System.currentTimeMillis();
        indexDao.delectAll(tableName);
        long endTime = System.currentTimeMillis();
        return "删除所有数据用时"+(endTime-startTime);
    }
}
