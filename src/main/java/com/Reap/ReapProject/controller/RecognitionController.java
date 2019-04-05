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
             recognition.setReceiver(receiver);
            return "redirect:/users/"+recognition.getSenderId();
    }
}
