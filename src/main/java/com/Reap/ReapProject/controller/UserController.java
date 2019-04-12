package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.component.LoggedInUser;
import com.Reap.ReapProject.component.SearchUser;
import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.Role;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.exception.UnauthorisedAccessException;
import com.Reap.ReapProject.exception.UserNotFoundException;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RecognitionService recognitionService;


    @PostMapping("/users")
    public ModelAndView addUser(@Valid @ModelAttribute("user") User user, BindingResult result,@ModelAttribute("loggedUser")LoggedInUser loggedInUser, @RequestParam("photo") MultipartFile file,HttpServletRequest request,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return new ModelAndView("index");
        }
        else {
                List<String> emails=userService.findAllEmails();
                //unique email id
                if(emails.contains(user.getEmail())){
                    System.out.println("email id already exists");
                    ModelAndView modelAndView=new ModelAndView("redirect:/");
                    redirectAttributes.addFlashAttribute("registrationError","Email id already Taken Try a Different One");
                    return modelAndView;
                }
            HttpSession session=request.getSession();
            session.setAttribute("loginUser",user);
            try {
                String path=saveImagePath(file);
                user.setImage(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            userService.addUser(user);
            return new ModelAndView("redirect:/users/"+user.getId());
        }
    }

    public String saveImagePath(MultipartFile file) throws IOException {
        String UPLOADED_FOLDER = "/home/joyy/Documents/Reap/ReapProject/out/production/resources/static/images/userImages/";
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                return "/images/userImages/"+file.getOriginalFilename();
    }


    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/users/{id}")
    public ModelAndView getUserById(@PathVariable("id") Integer id, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes){
        HttpSession session=request.getSession();
        User user1=(User) session.getAttribute("loginUser");
        try {
            if(!id.equals(user1.getId())){
                System.out.println("id differs");
                ModelAndView modelAndView=new ModelAndView("redirect:/");
                redirectAttributes.addFlashAttribute("loginError","Please login to continue");
                return modelAndView;
            }
        }
        catch (NullPointerException e){
            System.out.println("null pointer");
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","Please login to continue");
            return modelAndView;
        }


        Optional<User> user=userService.getUserById(id);
        if(user.isPresent()){
            ModelAndView modelAndView=new ModelAndView("UserPage");
            model.addAttribute("user",user.get());
            model.addAttribute("recognition",new Recognition());
            model.addAttribute("searchUser",new SearchUser());

            List<Recognition> recognitions=recognitionService.getListOfRecognitions();
            Collections.reverse(recognitions);
            model.addAttribute("recognitions",recognitions);
            if(user.get().getRoleSet().contains(Role.ADMIN)){
                model.addAttribute("isAdmin",true);
                List<User> users=userService.getAllUser();
                model.addAttribute("users",users);
            }
            return modelAndView;
        }
        else throw new UserNotFoundException("no user with the given id exists");
    }


    //this path variable id is coming from form and differs from session id
    @PutMapping("/users/{id}")
    public ModelAndView updateUser(@PathVariable Integer id,@RequestParam Map<String, String> requestParams,HttpServletRequest request){
        HttpSession session=request.getSession();
        User loggedUser=(User)session.getAttribute("loginUser");

        if(session==null){
            throw new UnauthorisedAccessException("Unauthorized Access");
        }

        System.out.println("path variable id "+id);
        System.out.println("session id "+loggedUser.getId());
        Optional<User> user1=userService.getUserById(id);
        if(user1.isPresent()){
            //updating users active status
            if(requestParams.get("active")==null){
                user1.get().setActive(false);
            }
             else {
                user1.get().setActive(true);
            }

            Set<Role> roles=user1.get().getRoleSet();
            roles=roleChecker(roles,requestParams.get("adminCheck"),Role.ADMIN);
            roles=roleChecker(roles,requestParams.get("practiceHeadCheck"),Role.PRACTICE_HEAD);
            roles=roleChecker(roles,requestParams.get("supervisorCheck"),Role.SUPERVISOR);
            roles=roleChecker(roles,requestParams.get("userCheck"),Role.USER);

            user1.get().setRoleSet(roles);
            userService.adminEditUser(user1.get());
            ModelAndView modelAndView=new ModelAndView("redirect:/users/"+loggedUser.getId());
            return modelAndView;
        }
        else throw new UserNotFoundException("no user with the given id exists");
    }

    @PostMapping("/login")
    public ModelAndView userLogin(@ModelAttribute("loggedUser")LoggedInUser loggedInUser, HttpServletRequest request, RedirectAttributes redirectAttributes){

        User user=userService.getUserByEmailAndPasswordAndActive(loggedInUser.getEmail(),loggedInUser.getPassword());

        if(user!=null){
            HttpSession session=request.getSession();
            session.setAttribute("loginUser",user);
            return  new ModelAndView("redirect:/users/"+user.getId());
        }
        else {
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","Invalid Credentials");
            return modelAndView;
        }
    }


    @PostMapping("/searchRecogByName")
    @ResponseBody
    public List<Recognition> getUserRecogByName(@ModelAttribute("searchUser")SearchUser searchUser){
        System.out.println("controller called");
        System.out.println("search user "+searchUser);
        searchUser.getCurrentUserId();
        List<Recognition> recognitions=recognitionService.getListOfRecognitionsByReceiverName(searchUser.getFullName());
        System.out.println( recognitionService.getListOfRecognitionsByReceiverName(searchUser.getFullName()));
        return  recognitions;
    }

    @GetMapping("/searchRecogByDate/{date}")
    @ResponseBody
    public List<Recognition> getUserRecodByName(@PathVariable("date") String date){
        List<Recognition> recognitions=recognitionService.findRecognitionByDateBetween(date);
        return recognitions;
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<User> autoComplete(@RequestParam("pattern")String namePattern){
        System.out.println(userService.findByFullNameLike(namePattern+"%"));
        return userService.findByFullNameLike(namePattern+"%");
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request)
    {
        HttpSession session=request.getSession();
        session.invalidate();
        return "redirect:/";
    }


    //user role checking utility methods
    public Set<Role> roleChecker(Set<Role> roles,String status,Role role){
        if(status==null){
            roles.remove(role);
            return roles;
        }
        else {
            roles.add(role);
            return roles;
        }
    }
}

