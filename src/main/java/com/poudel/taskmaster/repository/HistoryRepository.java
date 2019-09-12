package com.poudel.taskmaster.repository;

import com.poudel.taskmaster.model.Task;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface HistoryRepository extends CrudRepository<Task, String> {


}
