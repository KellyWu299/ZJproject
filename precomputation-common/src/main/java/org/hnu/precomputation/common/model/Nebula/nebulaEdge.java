package org.hnu.precomputation.common.model.Nebula;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
/*nebula测试边*/
@ClassAutoMapping("nebulaEdge")
public class nebulaEdge extends JSONObject {

    @FieldAutoMapping(method = "getAgainst" ,type = "against")
    private String against;

    @FieldAutoMapping(method = "getLeftVid" ,type = "leftVid")
    private String leftVid;

    @FieldAutoMapping(method = "getRightVid" ,type = "rightVid")
    private String rightVid;

    @Override
    public String toString() {
        return "nebulaEdge{" +
                "against='" + against + '\'' +
                ", leftVid='" + leftVid + '\'' +
                ", rightVid='" + rightVid + '\'' +
                '}';
    }
}