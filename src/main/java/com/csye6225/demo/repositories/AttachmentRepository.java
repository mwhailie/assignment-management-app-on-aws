package com.csye6225.demo.repositories;

import com.csye6225.demo.pojos.Attachment;
import com.csye6225.demo.pojos.Task;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import javax.transaction.*;
@Transactional

public interface AttachmentRepository extends CrudRepository<Attachment, Integer> {
    public List<Attachment> findByTask(Task task);

}
