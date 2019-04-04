package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class RecognitionController {

    @Autowired
    RecognitionService recognitionService;

    @Autowired
    UserService userService;

    @PostMapping("/recognizeNewer")
    public String recognizeUser(@ModelAttribute("recognition") Recognition recognition){
            String recieverName=recognition.getReceiverName();
             User user=userService.getUserByFullName(recieverName);
             recognition.setReceiverId(user.getId());
             recognitionService.addRecognition(recognition);
             User receiver=userService.getUserById(recognition.getReceiverId()).get();
             System.out.println(receiver);

             recognition.setReceiver(receiver);

             System.out.println("GOLD "+recognition.getReceiver().getGoldRedeemable());
             System.out.println("SILVER "+recognition.getReceiver().getSilverRedeemable());
             System.out.println("BRONZE "+recognition.getReceiver().getBronzeRedeemable());
             System.out.println("called controller");
             System.out.println(recognition);
            return "redirect:/users/"+recognition.getSenderId();
    }
}
