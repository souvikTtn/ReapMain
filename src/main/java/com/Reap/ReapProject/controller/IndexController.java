package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.component.LoggedInUser;
import com.Reap.ReapProject.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


@Controller
public class IndexController {

    @GetMapping("/")
    public String getIndexPage(Model model, HttpSession session){
        User user=new User();
        model.addAttribute("user",user);
        model.addAttribute("loggedUser",new LoggedInUser());
        return "index";
    }
}
