package com.samuelnunes.todosimple.services;

import com.samuelnunes.todosimple.models.Task;
import com.samuelnunes.todosimple.models.User;
import com.samuelnunes.todosimple.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Task not found, Id: "+ id + ", Type: "+ Task.class.getName()));
    }
    public List<Task> findAllByUserId(Long userId) {
        return this.taskRepository.findByUserId(userId);
    }
    @Transactional
    public Task create(Task task) {
        User user = this.userService.findById(task.getUser().getId());
        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);
        return task;
    }
    @Transactional
    public Task update(Task task) {
        Task taskToUpdate = this.findById(task.getId());
        taskToUpdate.setDescription(task.getDescription());
        return this.taskRepository.save(taskToUpdate);
    }
    public void deleteById(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Cannot delete as there are related entities");
        }
    }

}
