package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Item;
import com.Reap.ReapProject.entity.OrderSummary;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.ItemService;
import com.Reap.ReapProject.service.OrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderSummaryController {
    @Autowired
    ItemService itemService;

    @Autowired
    OrderSummaryService orderSummaryService;

    @PostMapping("/addToCart/{itemId}")
    @ResponseBody
    public void addItemToCart(HttpServletRequest request, @PathVariable("itemId")String itemId){
        HttpSession session=request.getSession();
        Integer id=Integer.parseInt(itemId);
        List<Item> itemList=(List<Item>) session.getAttribute("itemList");
        Item item=itemService.findItemById(id).get();
        itemList.add(item);
        System.out.println("item added successfully");
    }

    @GetMapping("/checkout")
    public ModelAndView checkout(HttpServletRequest request, RedirectAttributes redirectAttributes){


        HttpSession session=request.getSession();
        List<Item>itemList=(List<Item>) session.getAttribute("itemList");
        User user=(User)session.getAttribute("loginUser");
        OrderSummary orderSummary=new OrderSummary();
        orderSummary.setUserId(user.getId());
        Map<Integer,Integer> itemQuantity=new LinkedHashMap<>();
        Integer points=0;
        for (Item item:itemList){
            if(!itemQuantity.containsKey(item.getId())){
                itemQuantity.put(item.getId(),1);
            }

            else {
                itemQuantity.put(item.getId(),itemQuantity.get(item.getId())+1);
            }
            points+=item.getPoints();
        }
        orderSummary.setItemQuantity(itemQuantity);
        orderSummary.setTotalPointsRedeemed(points);
        orderSummaryService.addOrder(orderSummary);
        System.out.println("order saved");
        itemList.clear();
        ModelAndView modelAndView=new ModelAndView("redirect:users/"+user.getId()+"/cart");
        redirectAttributes.addFlashAttribute("orderSuccessfull","your Order is Successfull");
        return modelAndView;
    }
}
