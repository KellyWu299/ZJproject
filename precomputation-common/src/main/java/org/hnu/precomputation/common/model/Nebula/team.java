package org.hnu.precomputation.common.model.Nebula;

import lombok.Data;

@Data
//nebula测试用点类型
@ClassAutoMapping("team")
public class team {
    @FieldAutoMapping(method = "getId" ,type = "id")
    private String id ;
    @FieldAutoMapping(method = "getTeam_name",type = "team_name")
    private String team_name;
    @FieldAutoMapping(method = "getTeam_num",type = "team_num")
    private String team_num;

}
