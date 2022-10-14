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
}
