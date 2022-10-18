package org.hnu.precomputation.common.model.Nebula;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
/*nebula测试边*/
@ClassAutoMapping("nebulaEdge")
public class nebulaEdge extends JSONObject {

    @FieldAutoMapping(method = "getAgainst" ,type = "against")
    private String weight;

    @FieldAutoMapping(method = "getLeftVid" ,type = "leftVid")
    private String leftVid;

    @FieldAutoMapping(method = "getRightVid" ,type = "rightVid")
    private String rightVid;
}