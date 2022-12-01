package org.hnu.precomputation.service.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*一个工具类，简化使用*/
public class SqlBuild {

    private String id;

    private String name;

    private String field;

    private String values;

}
