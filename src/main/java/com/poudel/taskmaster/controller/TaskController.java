package com.poudel.taskmaster.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.poudel.taskmaster.model.History;
import com.poudel.taskmaster.model.Task;
import com.poudel.taskmaster.repository.DynamoDBConfig;
import com.poudel.taskmaster.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
//        newTask.setTitle(task.getTitle());
//        newTask.setDescription(task.getDescription());
//        newTask.setStatus(task.getStatus());
//        DynamoDBConfig dynamoDBConfig = new DynamoDBConfig();
//
//        AmazonDynamoDB dynamo = new AmazonDynamoDBClient(dynamoDBConfig.credentialsProvider().
//                getCredentials());
//        DynamoDBMapper  mapper = new DynamoDBMapper(dynamo);
//        mapper.save(newTask);
        DynamoDBConfig dynamoDBConfig = new DynamoDBConfig();
        AmazonDynamoDB client  = dynamoDBConfig.amazonDynamoDB();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(newTask);
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
        Date date = new Date();
        History history = new History(date, status);
        if(status.equals("available")){
            task.setStatus("assigned");
        }else if(status.equals("assigned")){
            task.setStatus("accepted");
        }else if(status.equals("accepted")){
            task.setStatus("finished");
        }
        //TODO: call historyCreation() on task and save it
        task.getHistoryList().add(history);
        DynamoDBConfig dynamoDBConfig = new DynamoDBConfig();
        AmazonDynamoDB client  = dynamoDBConfig.amazonDynamoDB();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(task);
        taskRepository.save(task);
        return task;

    }

}
