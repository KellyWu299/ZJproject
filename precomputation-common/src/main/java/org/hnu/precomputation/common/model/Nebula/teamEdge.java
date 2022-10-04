package org.hnu.precomputation.common.model.Nebula;

import lombok.Data;

@ClassAutoMapping("teamEdge")
@Data
public class teamEdge extends Edge {

    @FieldAutoMapping(method = "getAgainst",type = "String")
    private String against;



}
