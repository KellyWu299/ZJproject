
package org.hnu.precomputation.service.web.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.model.dataset.Task;
import org.hnu.precomputation.common.view.dataset.DatasetAddParam;
import org.hnu.precomputation.service.Impl.Pair;
import org.hnu.precomputation.service.service.DatasetService;
import org.hnu.precomputation.service.service.JanusGraphService;
import org.hnu.precomputation.service.service.NebulaGraphService;
import org.hnu.precomputation.service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Autowired
    private DatasetService datasetService;

    @Autowired
    private JanusGraphService janusGraphService;

    @Autowired
    private NebulaGraphService nebulaGraphService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Autowired
    private TaskService taskService;


    // 一般的get请求,接收一个param参数

    @GetMapping("/queryById")
    public CommonResult<Dataset> queryById(@RequestParam("id") long id) {
        Dataset dataset = datasetService.queryDataset(id);
        if (dataset == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(dataset);
    }

    //包含文件上传的post请求，需要额外的param参数

    @PostMapping("/addDataset")
    public CommonResult<String> addDataset(@RequestParam("file")MultipartFile[] files, @RequestPart("req") @Valid List<DatasetAddParam> params) throws Exception {
        int i = 0;
        for (MultipartFile file : files) {
            //加入dataset的sql
            int finalI = i;
            CompletableFuture<Void> addDataset = CompletableFuture.runAsync(() -> {
                Dataset dataset = new Dataset();
                dataset.setName(file.getOriginalFilename());
                dataset.setSource(params.get(finalI).getSource());
                dataset.setDescription(params.get(finalI).getDescription());
                dataset.setVertexProperty(params.get(finalI).getVertexProperty());
                dataset.setEdgeProperty(params.get(finalI).getEdgeProperty());
                System.out.println(finalI + "addDataset初始化完成");
                datasetService.addDataset(dataset);
            }, threadPoolTaskExecutor);
            log.info("Dataset对象初始化完成...");

            //异步执行
            int finalI1 = i;
            CompletableFuture<Void> addTaskData = CompletableFuture.runAsync(() -> {
                log.info("加入任务Task...");
                Task task = new Task();
                task.setCreatedTime(new Date());
                task.setTaskName(params.get(finalI1).getTaskName());
                task.setReqParam(file.getName());
                task.setTaskStatus(1);
                task.setTaskType(2);
                taskService.save(task);
                //记录本条task的id
                Long id = task.getId();
                if (params.get(finalI1).getSource() == 1) {
                    //nebula待补充
                }

                //janusgraph:
                if (params.get(finalI1).getSource() == 2) {
                    log.info("线程名:" + Thread.currentThread().getName() + ":janusgraph 导入数据");
                    //利用构造器,将表中的deal_time和task_status进行更新
                    UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("id", id).set("task_status", 2).set("deal_time", new Date());
                    taskService.update(null, updateWrapper);
                    try {
                        janusGraphService.putFile(file, params.get(finalI1).getVertexProperty(), params.get(finalI1).getEdgeProperty());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //更新task表任务状态和完成时间,其他属性不变
                    updateWrapper.eq("id", id).set("task_status", 3).set("finish_time", new Date());
                    taskService.update(null, updateWrapper);
                    log.info(Thread.currentThread().getName() + ":janusgraph 导入数据完成");
                }


            }, threadPoolTaskExecutor);
            CompletableFuture.allOf(addTaskData, addDataset).get();
            i++;
        }
     return CommonResult.success("add dataset successfully!");
    }

    // post请求，需要body作为参数
    @PostMapping("/update")
    public CommonResult<Dataset> update(@RequestBody Dataset dataset) {
        datasetService.update(dataset);
        return CommonResult.success(dataset);
    }
//查看sql
    @GetMapping("/selectDataset")
    public CommonResult<List<Dataset>> selectDataset() {
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
        //nebula待补充
        datasetService.delete(id);

        return CommonResult.success(result);
    }

    @PostMapping("getJanusGraph")
    public CommonResult<ArrayList<Pair>> getGraph(@RequestPart("req") @Valid DatasetAddParam param){
        String vertexProperty = param.getVertexProperty();
        String edgeProperty = param.getEdgeProperty();
        ArrayList<Pair> graph = janusGraphService.getGraph(vertexProperty,edgeProperty);
        return CommonResult.success(graph);
    }
    @GetMapping("/printJanusGraphSchema")
    public CommonResult<String> printGraph(){
        return CommonResult.success(janusGraphService.printSchema());
    }
    @GetMapping("/countJanusGraph")
    public CommonResult<String> countGraph(){
        return CommonResult.success(janusGraphService.countGraph());
    }

    //查看task表
    @GetMapping("/selectTask")
    public CommonResult<List<Task>> selectTask() {
        List<Task> list = taskService.list();
        return CommonResult.success(list);
    }
    //每10秒打印一次
 //   @Scheduled(cron = "0/10 * * * * ?")
public void printTask(){
        System.out.println(taskService.list());
    }
}





