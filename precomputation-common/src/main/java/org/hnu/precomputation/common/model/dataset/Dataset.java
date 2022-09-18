package org.hnu.precomputation.common.model.dataset;

import lombok.Data;

/**
 * 数据集模型
 */
@Data
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
     * 数据集点数
     */
    private Long vertexnum;

    /**
     * 数据集边数
     */
    private Long edgenum;
    private String vertexProperty;
    private String edgeProperty;

}
