package org.hnu.precomputation.service.dao;


import jdk.internal.dynalink.linker.LinkerServices;
import org.apache.ibatis.annotations.Mapper;
import org.hnu.precomputation.service.graphAlgo.preCompute.entity_Index;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface IndexDao {
    void InsertAllIndex(List<entity_Index> entity_indices);
}
