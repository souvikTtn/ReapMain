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
    @ResponseBody
    public String recognizeUser(@ModelAttribute("recognition") Recognition recognition){
            String recieverName=recognition.getReceiverName();

             System.out.println(recieverName);

             User user=userService.getUserByFullName(recieverName);
             recognition.setReceiverId(user.getId());
             recognitionService.addRecognition(recognition);

            System.out.println("called controller");
            System.out.println(recognition);
            return "test";
    }
}
