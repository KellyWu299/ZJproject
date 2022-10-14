package org.hnu.precomputation.common.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 常用API返回对象接口
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    Integer getCode();

    /**
     * 返回信息
     */
    String getMessage();


}
