package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.exception.UserNotFoundException;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/home/joyy/Documents/Reap/ReapProject/src/main/resources/static/userImages/";


    @PostMapping("/users")
    @ResponseBody
    public String addUser(@Valid @ModelAttribute("user") User user, @RequestParam("photo") MultipartFile file, BindingResult result){
        if(result.hasErrors()){
            System.out.println("error caught");
            return "index";
        }
        else {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                user.setImage(path.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            userService.addUser(user);

        }
        return ("hello world");
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

