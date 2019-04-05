package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    List<User> findAll();
    User findByFullName(String fullName);

    User findByEmailAndPassword(String email,String password);
}
