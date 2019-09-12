package com.poudel.taskmaster.controller;
import com.poudel.taskmaster.model.History;
import com.poudel.taskmaster.model.Task;
import com.poudel.taskmaster.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api1/v2")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/tasks")
    public List<Task> getTasks(){
        return (    List) taskRepository.findAll();
    }

    @PostMapping("/tasks")
    public Task addNewTask(@RequestBody Task task) {
        Task newTask = new Task(task.getTitle(), task.getDescription(),
                task.getAssignee());

        String date = new Date().toString();
        History history = new History("task is assigned to: " + task.getAssignee());
        newTask.getHistoryList().add(history);

        taskRepository.save(newTask);
        return newTask;
    }

    @GetMapping("/users/{name}/tasks")
    public Optional<Task> getTasksOfName(@PathVariable String name){
        Optional<Task> tasks = taskRepository.findByAssignee(name);
        return tasks;
    }

    @PutMapping("/tasks/{id}/state")
    public Task updateStatus(@PathVariable String id){
        Task task = taskRepository.findById(id).get();
        String status = task.getStatus();
        String date = new Date().toString();
        History history = new History("task is assigned to: " + task.getAssignee());
        if(status.equals("available")){
            task.setStatus("assigned");
        }else if(status.equals("assigned")){
            task.setStatus("accepted");
        }else if(status.equals("accepted")){
            task.setStatus("finished");
        }
        task.getHistoryList().add(history);
        taskRepository.save(task);
        return task;
    }

    @PutMapping("/tasks/{id}/assign/{assignee}")
    public Task updateAssignee(@PathVariable String id, @PathVariable String assignee) {
        Task task = taskRepository.findById(id).get();
        task.setAssignee(assignee);
        task.setStatus("assigned");
        String status = task.getStatus();
        Date date = new Date();
        History history = new History("task is assigned to: " + task.getAssignee());
        task.getHistoryList().add(history);
        taskRepository.save(task);
        return task;
    }

}
