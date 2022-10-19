package org.hnu.precomputation.common.view.dataset;

import lombok.Data;

/**
 * 数据集添加请求参数
 */
@Data
public class DatasetAddParam {
    /**
     * 数据源 1->nebula graph  2->janus graph
     */
    private Integer source;

    /**
     * 数据集描述
     */
    private String description;
    /**
     * 顶点属性
     */
    private String vertexProperty;
    /**
     * 边属性
     */
    private String edgeProperty;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * janusgraph数据集的边id文件名,命名要以.txt结尾
     */
    private String janusIdFileName;

}
