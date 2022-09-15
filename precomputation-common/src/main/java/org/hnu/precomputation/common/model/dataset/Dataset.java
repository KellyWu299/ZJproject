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
}
