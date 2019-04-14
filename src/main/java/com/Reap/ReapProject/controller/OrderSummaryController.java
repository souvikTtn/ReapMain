package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Item;
import com.Reap.ReapProject.entity.OrderSummary;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.ItemService;
import com.Reap.ReapProject.service.OrderSummaryService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Controller
public class OrderSummaryController {
    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    OrderSummaryService orderSummaryService;
    // Add an item to user's cart
    @PostMapping("/addToCart/{itemId}")
    public ResponseEntity<String> addItemToCart(HttpServletRequest request, @PathVariable("itemId")String itemId){
        HttpSession session=request.getSession();
        Integer id=Integer.parseInt(itemId);
        List<Item> itemList=(List<Item>) session.getAttribute("itemList");
        Integer itemListPoints=0;
        for (Item item:itemList){
            itemListPoints+=item.getPoints();
        }

        //item to add in the cart
        Item item=itemService.findItemById(id).get();
        User activeUser=(User)session.getAttribute("loginUser");

        if (activeUser.getPoints() < itemListPoints+item.getPoints()) {
            System.out.println("Not enough points");
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.set("MyResponseHeader","insufficientPoints");
            return new ResponseEntity<String>("Could Add Item To the Cart Not Enough Points",httpHeaders, HttpStatus.OK);
        }
        itemList.add(item);
        session.setAttribute("currentCartTotal", itemListPoints + item.getPoints());
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.set("MyResponseHeader","cartAddSuccessful");
        return new ResponseEntity<String>("Item Successfully Added To The Cart",httpHeaders,HttpStatus.OK);
    }

    // Create a new order with items from the cart
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
        userService.deductPointsOnCheckout(user, points);
        System.out.println("order saved");
        itemList.clear();
        session.setAttribute("currentCartTotal", 0);

        ModelAndView modelAndView=new ModelAndView("redirect:users/"+user.getId()+"/cart");
        redirectAttributes.addFlashAttribute("orderSuccessfull","your Order is Successfull");
        return modelAndView;
    }

    // Remove an item from user's cart
    @PutMapping("/removeFromCart/{itemId}")
    @ResponseBody
    public void removeItemFromCart(HttpServletRequest request,@PathVariable("itemId")String itemId){
        Integer id=Integer.parseInt(itemId);
        HttpSession session=request.getSession();
        List<Item> itemList=(List<Item>) session.getAttribute("itemList");

        System.out.println("session item list"+itemList);
        System.out.println(itemService.findItemById(id).get());
        Item item=itemService.findItemById(id).get();

        ListIterator<Item> itemListIterator=itemList.listIterator();

        label1: while (itemListIterator.hasNext()){
            if(itemListIterator.next().getId()==item.getId()){
                itemListIterator.remove();
                break label1;
            }
        }

        Integer currentCartPointsWorth = 0;
        for (Item itemInCart : itemList) {
            currentCartPointsWorth += itemInCart.getPoints();
        }
        session.setAttribute("currentCartTotal", currentCartPointsWorth);
        session.setAttribute("itemList",itemList);
    }
}
