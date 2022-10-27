package org.hnu.precomputation.service.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.service.graphAlgo.preCompute.H2H_Index;
import org.hnu.precomputation.service.service.PreComputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private PreComputeService preComputeService;


    @GetMapping("/makeIndex")
    public CommonResult<String> makeIndex(@RequestParam(value = "v",required = true) String v, @RequestParam(value = "e",required = true) String e) throws IOException {
        long StartTime=System.currentTimeMillis();
        preComputeService.MakeIndex(v,e);
        long EndTime=System.currentTimeMillis();
        long time=EndTime-StartTime;
        return CommonResult.success("生成索引用时："+time);
    }
}
