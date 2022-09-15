package org.hnu.precomputation.web.controller;

import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.view.dataset.DatasetAddParam;
import org.hnu.precomputation.service.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

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
    public CommonResult<Dataset> addDataset(MultipartFile file, DatasetAddParam param) {
        Dataset dataset = new Dataset();
        dataset.setName(file.getOriginalFilename());
        dataset.setSource(param.getSource());
        dataset.setDescription(param.getDescription());
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
}
