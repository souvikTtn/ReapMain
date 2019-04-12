package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.exception.UnauthorisedAccessException;
import com.Reap.ReapProject.repository.UserRepository;
import com.Reap.ReapProject.service.EmailService;
import com.Reap.ReapProject.service.RecognitionService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;


@Controller
public class RecognitionController {

    @Autowired
    RecognitionService recognitionService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

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
                 HttpHeaders httpHeaders=new HttpHeaders();
                 httpHeaders.set("MyResponseHeader","notExist");
                 return new ResponseEntity<String>("User Does Not Exists click to continue",httpHeaders,HttpStatus.OK);
             }

             if(user.getId().equals(recognition.getSenderId())){
                 HttpHeaders httpHeaders=new HttpHeaders();
                 httpHeaders.set("MyResponseHeader","selfRecognition");
                 return new ResponseEntity<String>("User Cant recognize Himself click to continue",httpHeaders,HttpStatus.OK);
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
        httpHeaders.set("MyResponseHeader","successful");
        return new ResponseEntity<String>("User Successfully Recognized click to continue",httpHeaders, HttpStatus.CREATED);
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

    @PutMapping("/revokeBadges/{id}")
    @ResponseBody
    public void revokeBadge(@PathVariable("id")String id){
        Integer recognitionId=Integer.parseInt(id);
        Recognition recognition=recognitionService.findRecognitionById(recognitionId).get();
        recognition.setRevoked(true);
        recognitionService.revokeRecognition(recognition);

        Integer receiverId=recognition.getReceiverId();
        User user=userRepository.findById(receiverId).get();
        SimpleMailMessage badgeRevokedMail = new SimpleMailMessage();
        badgeRevokedMail.setFrom("support@demo.com");
        badgeRevokedMail.setTo(user.getEmail());
        badgeRevokedMail.setSubject("Badge Revoked");
        badgeRevokedMail.setText("dear User Your Recognition "+recognition+" has been revoked");
        emailService.sendEmail(badgeRevokedMail);
    }
}
