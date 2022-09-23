package org.hnu.precomputation.service.service;

import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hnu.precomputation.common.model.dataset.Task;
import org.hnu.precomputation.service.dao.TaskMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService{
}
