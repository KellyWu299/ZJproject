package org.hnu.precomputation.common.model.dataset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 数据集模型
 */
@Data
@TableName(value = "dataset")
public class Dataset {
    /**
     * 自增id
     */
    private Long id;
    /**
     * 数据集名称
     */
    private String name;
    /**
     * 数据源 1->nebula graph  2->janus graph
     */
    private Integer source;
    /**
     * 数据集描述
     */
    private String description;
    /**
     * 属性,用来区分不同数据集
     */
    private String vertexProperty;
    private String edgeProperty;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * janusgraph的id文件名,命名要以.txt结尾
     */
    private String janusIdFileName;

}
