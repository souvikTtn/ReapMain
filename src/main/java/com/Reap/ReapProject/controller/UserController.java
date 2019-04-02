package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.exception.UserNotFoundException;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/users")
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Integer id){
        Optional<User> user=userService.getUserById(id);
        if(user.isPresent()){
            return user.get();
        }
        else throw new UserNotFoundException("no user with the given id exists");
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id,@RequestBody @Valid User user){
        Optional<User> user1=userService.getUserById(id);
        if(user1.isPresent()){
            return userService.addUser(user);
        }
        else throw new UserNotFoundException("no user with the given id exists");
    }
}

