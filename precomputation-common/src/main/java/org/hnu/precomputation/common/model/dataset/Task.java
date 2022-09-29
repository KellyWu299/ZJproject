package org.hnu.precomputation.common.model.dataset;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 异步导入导出任务表
 */
@Data
@TableName(value = "task")
public class Task {

    /**
     * 任务id type = IdType.ID_WORKER
     */
    @TableId
    private Long id;

    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 请求参数:文件名称
     */
    private String reqParam;
    /**
     * 任务状态
     * 1：init
     * 2: running
     * 3: finish(success)
     * 4: finish(fail)
     */

    private Integer taskStatus;
    /**
     * 任务类型
     * 1：上传（导入）
     * 2：下载（导出）
     * 3:删除数据集
     */
    private Integer taskType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dealTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;


}