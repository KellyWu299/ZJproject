package org.hnu.precomputation.service.web.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hnu.precomputation.common.model.api.CommonResult;
import org.hnu.precomputation.common.model.dataset.Dataset;
import org.hnu.precomputation.common.model.dataset.Task;
import org.hnu.precomputation.common.model.Nebula.serviceEdge;
import org.hnu.precomputation.common.model.api.NebulaResult;
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
import java.io.IOException;
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
        Thread[] threads = new Thread[params.size()];
        for (int i = 0; i < files.length && i < params.size(); i++) {
            int finalI = i;
            janusGraphService.putIndex(params.get(finalI).getVertexProperty(), params.get(finalI).getEdgeProperty());
            threads[i] = new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    CompletableFuture<Void> addDataset = CompletableFuture.runAsync(() -> {
                        Dataset dataset = new Dataset();
                        dataset.setName(files[finalI].getOriginalFilename());
                        dataset.setSource(params.get(finalI).getSource());
                        dataset.setDescription(params.get(finalI).getDescription());
                        dataset.setVertexProperty(params.get(finalI).getVertexProperty());
                        dataset.setEdgeProperty(params.get(finalI).getEdgeProperty());
                        dataset.setJanusIdFileName(params.get(finalI).getJanusIdFileName());
                        System.out.println(finalI + "addDataset初始化完成");
                        datasetService.addDataset(dataset);
                    }, threadPoolTaskExecutor);
                    log.info("Dataset对象初始化完成...");

                    //异步执行
                    CompletableFuture<Void> addTaskData = CompletableFuture.runAsync(() -> {
                        log.info("加入任务Task...");
                        Task task = new Task();
                        task.setCreatedTime(new Date());
                        task.setTaskName(params.get(finalI).getTaskName());
                        task.setReqParam(files[finalI].getName());
                        task.setTaskStatus(1);
                        task.setTaskType(2);
                        taskService.save(task);
                        //记录本条task的id
                        Long id = task.getId();
                        if (params.get(finalI).getSource() == 1) {
                            //nebula待补充
                            try {
                                nebulaGraphService.OpenNebula(files[finalI],files[finalI].getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //janusgraph:
                        if (params.get(finalI).getSource() == 2) {
                            //1,构建索引
                            log.info("线程名:" + Thread.currentThread().getName() + ":janusgraph 导入数据");
                            //2,利用构造器,将表中的d
                            UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
                            updateWrapper.eq("id", id).set("task_status", 2).set("deal_time", new Date());
                            taskService.update(null, updateWrapper);
                            //3,导入数据
                            try {
                                janusGraphService.putFile(files[finalI], params.get(finalI).getVertexProperty(), params.get(finalI).getEdgeProperty(),params.get(finalI).getJanusIdFileName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //4,更新task表任务状态和完成时间,其他属性不变
                            updateWrapper.eq("id", id).set("task_status", 3).set("finish_time", new Date());
                            taskService.update(null, updateWrapper);
                            log.info(Thread.currentThread().getName() + ":janusgraph 导入数据完成");
                        }
                    }, threadPoolTaskExecutor);
                    CompletableFuture.allOf(addTaskData, addDataset).get();
                }
            });
            threads[i].start();
        }
        log.info("等待线程全部执行完毕...   ");
        while (true) {
            if(threadPoolTaskExecutor.getActiveCount()==0)break;
        }
        log.info("提交任务....");
        janusGraphService.commit();
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
    public CommonResult<String> delete(@PathVariable("id") Long id) throws Exception {
        Dataset dataset = datasetService.queryDataset(id);
        String result=null;
        //数据集为janusgraph数据集
        if(dataset.getSource()==2){
            result = janusGraphService.deleteGraph(dataset.getVertexProperty(), dataset.getEdgeProperty(), dataset.getJanusIdFileName());
        }
        //nebula待补充
        datasetService.delete(id);

        return CommonResult.success(result);
    }

    @SneakyThrows
    @PostMapping("getJanusGraph")
    public CommonResult<ArrayList<Pair>> getGraph(@RequestPart("req") @Valid DatasetAddParam param){
        String edgeIdFileName = param.getJanusIdFileName();
        String edgeProperty = param.getEdgeProperty();
        ArrayList<Pair> graph = janusGraphService.getGraph(edgeProperty,edgeIdFileName);
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





