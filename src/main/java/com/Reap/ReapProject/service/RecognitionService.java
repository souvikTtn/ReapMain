package com.Reap.ReapProject.service;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.repository.RecognitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecognitionService {
    @Autowired
    RecognitionRepository recognitionRepository;

    @Autowired
    UserService userService;

    public void addRecognition(Recognition recognition){
        recognition.setDate(new Date());
        recognition.setTime(new Date());


        System.out.println(recognition.getBadge());

        Integer senderId=recognition.getSenderId();
        Integer receiverId=recognition.getReceiverId();
        Optional<User> sender=userService.getUserById(senderId);
        Optional<User> receiver=userService.getUserById(receiverId);


        if(recognition.getBadge().equals("silver")){
            sender.get().setSilverSharable(sender.get().getSilverSharable()-1);
            receiver.get().setSilverRedeemable(receiver.get().getSilverRedeemable()+1);

        }
         if (recognition.getBadge().equals("gold")) {
             sender.get().setGoldSharable(sender.get().getGoldSharable()-1);
             receiver.get().setGoldRedeemable(receiver.get().getGoldRedeemable()+1);
         }

         if (recognition.getBadge().equals("bronze")){
                sender.get().setBronzeSharable(sender.get().getBronzeSharable()-1);
             receiver.get().setBronzeRedeemable(receiver.get().getBronzeRedeemable()+1);
         }
        receiver.get().setPoints(userService.calculatePoints(receiver.get()));
        recognitionRepository.save(recognition);

        }
        public List<Recognition> getListOfRecognitions(){
            return recognitionRepository.findAll();
        }
    }

