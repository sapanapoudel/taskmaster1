package com.poudel.taskmaster.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.poudel.taskmaster.model.Task;
import com.poudel.taskmaster.repository.DynamoDBConfig;
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
        return (List) taskRepository.findAll();
    }

    @PostMapping("/tasks")
    public Task addNewTask(@RequestBody Task task) {
        Task newTask = new Task(task.getTitle(), task.getDescription(),
                task.getAssignee());

        taskRepository.save(newTask);
        return newTask;
    }

    @GetMapping("/users/{name}/tasks")
    public Optional<Task> getTasksOfName(@PathVariable String name){
        Optional<Task> tasks = taskRepository.findByAssignee(name);
        return tasks;
    }

}
