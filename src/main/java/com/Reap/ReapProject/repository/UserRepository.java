package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    List<User> findAll();
    User findByFullName(String fullName);

    User findByEmailAndPassword(String email,String password);

    List<User> findByFullNameLike(String pattern);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);
}
