package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
    @GetMapping("/admin")
    public String getAdminPage(Model model){
        User user=new User();
        user.setFirstName("souvik");
        user.setLastName("chakraborty");
        user.setEmail("souvik.chakraborty@tothenew.com");
        model.addAttribute("user",user);
        model.addAttribute("isAdmin",true);
        return "UserPage";
    }

    @GetMapping("/userPage")
    public String getUserPage(@ModelAttribute("user")User user){
        System.out.println(user);
        return "UserPage";
    }

    @GetMapping("/forgetTest")
    public ModelAndView getForgetModel(){
        return new ModelAndView("/fragments/ForgotPasswordModal");
    }
}
