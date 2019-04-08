package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.component.LoggedInUser;
import com.Reap.ReapProject.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class IndexController {

    @GetMapping("/")
    public String getIndexPage(Model model, RedirectAttributes redirectAttributes){
        User user=new User();
        model.addAttribute("user",user);
        model.addAttribute("loggedUser",new LoggedInUser());
        redirectAttributes.addAttribute("loginError");
        return "index";
    }
}
