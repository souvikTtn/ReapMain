package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.exception.UnauthorisedAccessException;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;


@Controller
public class RecognitionController {

    @Autowired
    RecognitionService recognitionService;

    @Autowired
    UserService userService;

    @PostMapping("/recognizeNewer")
    public ResponseEntity<String> recognizeUser(@ModelAttribute("recognition") Recognition recognition,HttpServletRequest request){

        //setting up the receiver id

            HttpSession session=request.getSession();
            if(session==null){
                throw new UnauthorisedAccessException("Unauthorized Access");
            }

             String receiverName=recognition.getReceiverName();
             User user=userService.getUserByFullName(receiverName);
             if(user==null){
                 System.out.println("user does not exists so cannot be recognized");
                 return new ResponseEntity<String>("User Does Not Exists",HttpStatus.OK);
             }

             if(user.getId().equals(recognition.getSenderId())){
                 System.out.println("user cannot recognize himself");
                 return new ResponseEntity<String>("User Cant recognize Himself",HttpStatus.OK);
             }

             recognition.setReceiverId(user.getId());
             recognitionService.addRecognition(recognition);
             User receiver=userService.getUserById(recognition.getReceiverId()).get();


             //setting up the redeemable badges
             recognition.setGoldRedeemable(receiver.getGoldRedeemable());
             recognition.setSilverRedeemable(receiver.getSilverRedeemable());
             recognition.setBronzeRedeemable(receiver.getBronzeRedeemable());
             recognitionService.updateRecognistion(recognition);


        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.set("MyResponseHeader","MyResponseValue");
        return new ResponseEntity<String>("User Successfully Recognized",httpHeaders, HttpStatus.CREATED);
    }


    @GetMapping("/sharedRecognitions/{id}")
    public List<Recognition> userSharedRecognitions(@PathVariable("id") Integer id, HttpServletRequest request){
       /* HttpSession session=request.getSession();
        User user=(User) session.getAttribute("loginUser");
        System.out.println(user);*/
        return recognitionService.findRecognitionBySenderId(id);
    }

    @GetMapping("/receivedRecognitions/{id}")
    public List<Recognition> userReceivedRecognitions(@PathVariable("id") Integer id,HttpServletRequest request){
        /*HttpSession session=request.getSession();
        User user=(User) session.getAttribute("loginUser");
        System.out.println(user);*/

        return recognitionService.findRecognitionByReceiverId(id);
    }

}
