package org.hnu.precomputation.web.controller;

import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.GraphComputeService;
import org.hnu.precomputation.service.service.JanusGraphService;
import org.hnu.precomputation.service.service.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/algo")
public class AlgoController {

    @Autowired
    private GraphComputeService graphComputeService;
    @Autowired
    private JanusGraphService janusGraphService;
    @Autowired
    private DatasetService datasetService;

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
        ArrayList<Pair> res =  janusGraphService.getGraph(dataset.getVertexProperty(), dataset.getEdgeProperty());  //获取图数据集
        if (res == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(res);
    }

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

}
