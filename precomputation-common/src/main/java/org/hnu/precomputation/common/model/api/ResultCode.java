package org.hnu.precomputation.common.model.api;

/**
 * 常用API返回对象
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "成功"),
    FAILED(500, "失败");


    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
