
package org.hnu.precomputation.service.web.controller;

import com.vesoft.nebula.client.graph.exception.IOErrorException;
import org.hnu.precomputation.common.model.Nebula.serviceEdge;
import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.GraphComputeService;
import org.hnu.precomputation.service.service.JanusGraphService;
import org.hnu.precomputation.service.Impl.Pair;
import org.hnu.precomputation.service.service.NebulaGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/algo")
public class AlgoController {

    @Autowired
    private GraphComputeService graphComputeService;
    @Autowired
    private JanusGraphService janusGraphService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private NebulaGraphService nebulaGraphService;


    /**
     * 根据id获取全局betweenness值
     * @param id （对应数据库表中元数据的id值）
     * @return betweenness Map
     */
    @GetMapping("/computeBcById")
    public CommonResult<Map<Object, Double>> computeBcById(@RequestParam("id") long id) {
        Map<Object, Double> res =  graphComputeService.getBetweenness(id);
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }
    //根据id查询数据集（仅用于测试）
    @GetMapping("/queryDatasetById")
    public CommonResult<ArrayList<Pair>> queryDataset(@RequestParam("id") long id) {
        Dataset dataset = datasetService.queryDataset(id);  //根据id获取图元数据
        ArrayList<Pair> res =  janusGraphService.getGraph(dataset.getEdgeProperty(),dataset.getJanusIdFileName());  //获取图数据集
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }
    //根据id查询数据集（仅用于测试）
//    @GetMapping("/queryDatasetById1")
//    public CommonResult<List<Pair>> queryDataset1(@RequestParam("id") long id) throws InterruptedException, IO
//    ErrorException {
//        Dataset dataset = datasetService.queryDataset(id);  //根据id获取图元数据
////        ArrayList<Pair> res =  janusGraphService.getGraph(dataset.getJanusIdFileName());  //获取图数据集
//        List<Pair> res = nebulaGraphService.GetServiceEdge(nebulaGraphService.getGraphName(dataset.getName()));
//        if (res == null) {
//            return CommonResult.failed();
//        }
//        return CommonResult.success(res);
//    }
    @GetMapping("/computeEgoByIdUsingBaseBSearch")
    public CommonResult<Map<Integer, Float>> computeEgoByIdUsingBaseBSearch(@RequestParam("id") long id) {
        Map<Integer, Float> res =  graphComputeService.gEgoUsingBaseBSearch(id);
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }

    @GetMapping("/computeEgoByIdUsingOptBSearch")
    public CommonResult<Map<Integer, Float>> computeEgoByIdUsingOptBSearch(@RequestParam("id") long id) {
        Map<Integer, Float> res =  graphComputeService.gEgoUsingOptBSearch(id);
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }

    @GetMapping("/computeEgoByIdUsingAdjMatrix")
    public CommonResult<Map<Integer, Float>> computeEgoByIdUsingAdjMatrix(@RequestParam("id") long id) {
        Map<Integer, Float> res =  graphComputeService.gEgoUsingAdjMatrix(id);
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }

    @GetMapping("/computeEgoById")
    public CommonResult<Map<Integer, Float>> computeEgoById(@RequestParam("id") long id) {
        Map<Integer, Float> res =  graphComputeService.gEgoRes(id);
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }
}

