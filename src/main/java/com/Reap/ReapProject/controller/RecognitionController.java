package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class RecognitionController {

    @Autowired
    RecognitionService recognitionService;

    @Autowired
    UserService userService;

    @PostMapping("/recognizeNewer")
    public ModelAndView recognizeUser(@ModelAttribute("recognition") Recognition recognition, RedirectAttributes redirectAttributes){

        //setting up the receiver id

             String receiverName=recognition.getReceiverName();
             User user=userService.getUserByFullName(receiverName);
             if(user==null){
                 System.out.println("user does not exists so cannot be recognized");
                 ModelAndView modelAndView=new ModelAndView("redirect:/users/"+recognition.getSenderId());
                 redirectAttributes.addFlashAttribute("errorMessage","No such User");
                 return modelAndView;
             }

             if(user.getId().equals(recognition.getSenderId())){
                 System.out.println("user cannot recognize himself");
                 ModelAndView modelAndView=new ModelAndView("redirect:/users/"+recognition.getSenderId());
                 redirectAttributes.addFlashAttribute("errorMessage","User Can't recognize Himself");
                 return modelAndView;
             }

             recognition.setReceiverId(user.getId());
             recognitionService.addRecognition(recognition);
             User receiver=userService.getUserById(recognition.getReceiverId()).get();


             //setting up the redeemable badges
             recognition.setGoldRedeemable(receiver.getGoldRedeemable());
             recognition.setSilverRedeemable(receiver.getSilverRedeemable());
             recognition.setBronzeRedeemable(receiver.getBronzeRedeemable());
             recognitionService.updateRecognistion(recognition);


             ModelAndView modelAndView=new ModelAndView("redirect:/users/"+recognition.getSenderId());
             redirectAttributes.addFlashAttribute("successMessage","Newer Successfully Recognized");
             return modelAndView;
    }
}
