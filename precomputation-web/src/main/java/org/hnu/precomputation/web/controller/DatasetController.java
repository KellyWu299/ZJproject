package org.hnu.precomputation.web.controller;

import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.view.dataset.DatasetAddParam;
import org.hnu.precomputation.service.graphAlgo.preCompute.H2H_Index;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.JanusGraphService;
import org.hnu.precomputation.service.service.Pair;
import org.hnu.precomputation.service.service.PreComputeService;
import org.hnu.precomputation.service.service.NebulaGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

    @Autowired
    private JanusGraphService janusGraphService;

    @Autowired
    private PreComputeService preComputeService;

    @Autowired
    private NebulaGraphService nebulaGraphService;


    /**
     * 一般的get请求,接收一个param参数
     */
    @GetMapping("/queryById")
    public CommonResult<Dataset> queryById(@RequestParam("id") Long id) {
        Dataset dataset = datasetService.queryDataset(id);
        if (dataset == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(dataset);
    }

    /**
     * post请求，需要body作为参数
     */
    @PostMapping("/update")
    public CommonResult<Dataset> update(@RequestBody Dataset dataset) {
        datasetService.update(dataset);
        return CommonResult.success(dataset);
    }

//查看sql
    @GetMapping("/select")
    public CommonResult<List<Dataset>> selectAll() {
        List<Dataset> list = datasetService.list();
        return CommonResult.success(list);
    }
    @PostMapping("/delete/{id}")
    public CommonResult<String> delete(@PathVariable("id") Long id){
        Dataset dataset = datasetService.queryDataset(id);
        String result=null;
        //数据集为janusgraph数据集
        if(dataset.getSource()==2){
            result = janusGraphService.deleteGraph(dataset.getVertexProperty(), dataset.getEdgeProperty());
        }
        datasetService.delete(id);

        return CommonResult.success(result);
    }

    @PostMapping("getGraph")
    public CommonResult<ArrayList<Pair>> getGraph( @RequestPart("req") @Valid DatasetAddParam param){
        String vertexProperty = param.getVertexProperty();
        String edgeProperty = param.getEdgeProperty();
        ArrayList<Pair> graph = janusGraphService.getGraph(vertexProperty,edgeProperty);
        return CommonResult.success(graph);
    }
    @GetMapping("/printSchema")
    public CommonResult<String> printGraph(){
        return CommonResult.success(janusGraphService.printSchema());
    }
    @GetMapping("/countGraph")
    public CommonResult<String> countGraph(){
        return CommonResult.success(janusGraphService.countGraph());
    }

    @GetMapping("/makeIndex/{v}/{e}")
    public CommonResult<String> makeIndex(@PathVariable String v,@PathVariable String e) throws IOException {
        preComputeService.MakeIndex(v,e);
        return CommonResult.success(preComputeService.GetPath());
    }

}
