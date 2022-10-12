package org.hnu.precomputation.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hnu.precomputation.common.model.dataset.Task;

@Mapper
public interface TaskMapper extends BaseMapper<Task>{

}
