package org.hnu.precomputation.web.controller;

import lombok.SneakyThrows;
import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.view.dataset.DatasetAddParam;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.JanusGraphService;
import org.janusgraph.core.JanusGraphFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.lang.Long;
import java.util.List;


@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private JanusGraphService janusGraphService;

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
     * 包含文件上传的post请求，需要额外的param参数
     */

    @PostMapping("/addDataset")
    public CommonResult<Dataset> addDataset(@RequestPart("file")MultipartFile file, @RequestPart("req") @Valid DatasetAddParam param) throws Exception {
        Dataset dataset = new Dataset();
        dataset.setName(file.getOriginalFilename());
        dataset.setSource(param.getSource());
        dataset.setDescription(param.getDescription());
        dataset.setVertexProperty(param.getVertexProperty());
        dataset.setEdgeProperty(param.getEdgeProperty());
        // janusgraph
        if(param.getSource()==2){
            janusGraphService.putFile(file, param.getVertexProperty(), param.getEdgeProperty());
        }
        datasetService.addDataset(dataset);
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

    /**
     * 删除数据集接口
     */
    @DeleteMapping("deleteById/{id}")
    public CommonResult<Dataset> delete(@PathVariable("id") Long id) {
        datasetService.delete(id);
        return CommonResult.success(null);
    }

    /**
     * 列出所有数据集接口
     */
    @GetMapping("/findAll")
    public CommonResult<List<Dataset>> getDataset(){
        List<Dataset> list = datasetService.getAll();
        return CommonResult.success(list);
    }

    /*@GetMapping("/schema")
    public CommonResult<String> Schema() {
        String schema = janusGraphService.printSchema();
        return CommonResult.success(schema);
    }*/
}
