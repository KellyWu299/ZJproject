package org.hnu.precomputation.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hnu.precomputation.common.model.dataset.Task;
import org.hnu.precomputation.service.dao.TaskMapper;
import org.hnu.precomputation.service.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}
