package org.hnu.precomputation.common.model.api;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
//定义一个nebula的返回类
public class NebulaResult<T> {
    private Integer code;
    private String message;
    private List<T> data;
    public void setData(List<T> data1){
        this.data=data1;
    }

    public boolean isSuccessed(){
        return code == 0;
    }

}
