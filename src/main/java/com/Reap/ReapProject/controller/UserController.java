package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.component.LoggedInUser;
import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.Role;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.exception.UserNotFoundException;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    RecognitionService recognitionService;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/home/joyy/Documents/Reap/ReapProject/src/main/resources/static/userImages/";


    @PostMapping("/users")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam("photo") MultipartFile file){
        if(result.hasErrors()){
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
            return ("redirect:/users/"+user.getId());
        }

    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable("id") Integer id, Model model, HttpServletRequest request){
        Optional<User> user=userService.getUserById(id);
        if(user.isPresent()){
            model.addAttribute("user",user.get());
            model.addAttribute("recognition",new Recognition());
            //HttpSession session=request.getSession();
            //session.setAttribute("id",id);
            model.addAttribute("recognitions",recognitionService.getListOfRecognitions());
            if(user.get().getRoleSet().contains(Role.ADMIN)){
                model.addAttribute("isAdmin",true);
                List<User> users=userService.getAllUser();
                model.addAttribute("users",users);
            }
            return "UserPage";
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

    @PostMapping("/login")
    public String userLogin(@ModelAttribute("loggedUser")LoggedInUser loggedInUser,HttpServletRequest request){
        User user=userService.getUserByEmailAndPassword(loggedInUser.getEmail(),loggedInUser.getPassword());
        loggedInUser.setId(user.getId());
        HttpSession session=request.getSession();
        session.setAttribute("loginUser",loggedInUser);
        return  "redirect:/users/"+user.getId();
    }

}

