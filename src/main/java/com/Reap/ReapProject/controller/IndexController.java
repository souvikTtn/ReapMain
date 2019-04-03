package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping("/")
    public String getIndexPage(Model model){
        User user=new User();
        model.addAttribute("user",user);
        return "index";
    }
}
