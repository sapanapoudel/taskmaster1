package com.poudel.taskmaster.controller;
import com.poudel.taskmaster.model.History;
import com.poudel.taskmaster.model.Task;
import com.poudel.taskmaster.repository.S3Client;
import com.poudel.taskmaster.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api1/v2")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    private S3Client s3Client;

    @Autowired
    TaskController(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    @GetMapping("/tasks")
    public List<Task> getTasks(){
        return (List) taskRepository.findAll();
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

    @PostMapping("tasks/images")
    public Task addNewTaskWithImage(@RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("assignee") String assignee,
                                    @RequestPart(value = "file") MultipartFile file
                                    ){

        String pic = this.s3Client.uploadFile(file);
        String date = new Date().toString();
        History history = new History("task is assigned to: " + assignee);
        Task newTask = new Task(title, description,assignee, pic);

        newTask.getHistoryList().add(history);

        taskRepository.save(newTask);

        return newTask;
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskAtId(@PathVariable String id){
        Task task = taskRepository.findById(id).get();
        return task;
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
