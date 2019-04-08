package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Item;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping("/items")
    public ModelAndView getItemPage(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        HttpSession session=request.getSession();
        User activeUser=(User)session.getAttribute("loginUser");
        if(activeUser==null){
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","Unauthorized Access");
            return modelAndView;
        }

        List<Item> itemList = itemService.getAllItems();
        ModelAndView modelAndView=new ModelAndView("items");
        modelAndView.addObject("itemList",itemList);
        modelAndView.addObject("user",activeUser);
        return modelAndView;
    }
}
