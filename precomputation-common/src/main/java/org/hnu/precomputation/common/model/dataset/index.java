package org.hnu.precomputation.common.model.dataset;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("IndexTest")
public class index {
    int id;
    String code;
    String dis;
    String pos;
}
