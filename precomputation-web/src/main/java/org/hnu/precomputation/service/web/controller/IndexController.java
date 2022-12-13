package org.hnu.precomputation.service.web.controller;

import com.vesoft.nebula.client.graph.exception.IOErrorException;
import lombok.extern.slf4j.Slf4j;
import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.service.dao.IndexDao;
import org.hnu.precomputation.service.service.PreComputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private PreComputeService preComputeService;
    @Autowired
    private IndexDao indexDao;
    @GetMapping("/makeIndex")
    public CommonResult<String> makeIndex(@RequestParam(value = "id") Integer id) throws IOException, IOErrorException, InterruptedException {
        String time=preComputeService.MakeIndex(id);
        return CommonResult.success(time);
    }
    @GetMapping("/getSingleIndex")
    public CommonResult<Map<Double, List<Long>>> getSingleIndex(@RequestParam(value = "v1",required = true) int v1, @RequestParam(value = "v2",required = true) int v2,@RequestParam(value = "id",required = true) Integer id) throws IOException, IOErrorException, InterruptedException {
        Map<Double, List<Long>> map=preComputeService.GetSingleIndex(v1,v2,id);
        if(map==null)
            return CommonResult.failed("基于索引查询和Dijks算法查询结果不同");
        return CommonResult.success(map);
    }
    @GetMapping("/delectAll")
    public CommonResult<String> delectAll(@RequestParam(value = "tableName",required = true)String tableName) throws IOException {
        String time=preComputeService.delect(tableName);
        return CommonResult.success(time);
    }
}
