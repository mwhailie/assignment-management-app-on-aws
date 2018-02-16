package com.csye6225.demo.repositories;

import com.csye6225.demo.pojos.User;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.*;
@Transactional

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
    public User findByName(String name);

}
