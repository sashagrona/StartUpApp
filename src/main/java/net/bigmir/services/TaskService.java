package net.bigmir.services;


import net.bigmir.model.BusinessPlan;
import net.bigmir.model.Task;
import net.bigmir.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public void saveTask(Task task){
        taskRepository.save(task);
    }

    @Transactional
    public List<Task> getTasksFromPlan(BusinessPlan plan){
        return taskRepository.getTasksFromPlan(plan);
    }

    @Transactional
    public void deleteTasksById(List<Long> ids){
        taskRepository.deleteTasksById(ids);
    }

    @Transactional
    public void matchAsDone(List<Long> ids){
        taskRepository.matchAsDone(ids);
    }
}
