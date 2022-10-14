package org.hnu.precomputation.web.controller;

import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.view.dataset.DatasetAddParam;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.NebulaGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

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
     * 接受一个id删除这行数据
     */
    @GetMapping("/deleteById")
    public CommonResult<Integer> deleteById(@RequestParam("id") Long id) {
        Dataset dataset = datasetService.queryDataset(id);
        nebulaGraphService.deleteSpace(dataset.getDescription());


        Integer temp=datasetService.delete(id);
        return CommonResult.success(temp);
    }
    /**
     * 列出所有数据集
     */
    @GetMapping("/queryAll")
    public CommonResult<List<Dataset>> queryAll(){
        List<Dataset> list = datasetService.queryAllDataset();
        return CommonResult.success(list);
    }
    /**
     * 包含文件上传的post请求，需要额外的param参数
     */
    @PostMapping("/addDataset")
    public CommonResult<Dataset> addDataset(MultipartFile file, DatasetAddParam param) throws InterruptedException, IOException {
        Dataset dataset = new Dataset();
        dataset.setName(file.getOriginalFilename());
        dataset.setSource(param.getSource());
        dataset.setDescription(param.getDescription());
        datasetService.addDataset(dataset);
        if(param.getSource()==1){
            nebulaGraphService.OpenNebula(file, "th");

        }
        return CommonResult.success(dataset);
    }


//    @PostMapping("/addDataset1")
//    public CommonResult<Dataset> addDataset1(MultipartFile file, int source,String description) throws InterruptedException, IOException {
//        Dataset dataset = new Dataset();
//        dataset.setName(file.getOriginalFilename());
//        dataset.setSource(source);
//        dataset.setDescription(description);
//        datasetService.addDataset(dataset);
//        if(source==1){
//            String fname= file.getName();
//            System.out.println(fname);
//            nebulaGraphService.OpenNebula(file);
//        }
//        return CommonResult.success(dataset);
//    }

    /**
     * post请求，需要body作为参数
     */
    @PostMapping("/update")
    public CommonResult<Dataset> update(@RequestBody Dataset dataset) {
        datasetService.update(dataset);
        return CommonResult.success(dataset);
    }


}
