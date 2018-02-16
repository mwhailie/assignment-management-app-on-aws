package com.csye6225.demo.repositories;

import com.csye6225.demo.pojos.Task;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.*;
import java.util.List;

@Transactional
public interface TaskRepository extends CrudRepository<Task, String>{
    public List<Task> findTaskByUser_id(Integer id);
}
