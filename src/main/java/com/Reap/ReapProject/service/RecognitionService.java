package com.Reap.ReapProject.service;

import com.Reap.ReapProject.entity.Recognition;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
        recognition.setDate(new LocalDate());
        recognition.setTime(new Date());


        System.out.println(recognition.getBadge());

        Integer senderId=recognition.getSenderId();
        Integer receiverId=recognition.getReceiverId();
        Optional<User> sender=userService.getUserById(senderId);
        Optional<User> receiver=userService.getUserById(receiverId);

        if(sender.isPresent()){
            if(recognition.getBadge().equals("silver")){
                if(sender.get().getSilverSharable()>0){
                    sender.get().setSilverSharable(sender.get().getSilverSharable()-1);
                    receiver.get().setSilverRedeemable(receiver.get().getSilverRedeemable()+1);
                }
                else {
                    System.out.println("not enough badges");
                }
            }
            if (recognition.getBadge().equals("gold")) {
                if(sender.get().getGoldSharable()>0){
                    sender.get().setGoldSharable(sender.get().getGoldSharable()-1);
                    receiver.get().setGoldRedeemable(receiver.get().getGoldRedeemable()+1);
                }
                else {
                    System.out.println("not enough badges");
                }
            }

            if (recognition.getBadge().equals("bronze")){
                if (sender.get().getBronzeSharable()>0){
                    sender.get().setBronzeSharable(sender.get().getBronzeSharable()-1);
                    receiver.get().setBronzeRedeemable(receiver.get().getBronzeRedeemable()+1);
                }
                else {
                    System.out.println("not enough badges");
                }
            }
        }
        receiver.get().setPoints(userService.calculatePoints(receiver.get()));
        recognitionRepository.save(recognition);

        }

        public void updateRecognistion(Recognition recognition){
            Recognition newRecognition=recognitionRepository.findById(recognition.getId()).get();
            recognitionRepository.save(newRecognition);
        }

        public List<Recognition> getListOfRecognitions(){
            return recognitionRepository.findAll();
        }

        public List<Recognition> getListOfRecognitionsByReceiverName(String receiverName){
            return recognitionRepository.findRecognitionByReceiverName(receiverName);
        }

        public List<Recognition> findRecognitionByDateBetween(String dateString)  {
            DateTime today=new DateTime();
            final LocalDate todayDate=today.toLocalDate();
            final LocalDate yesterday=todayDate.minusDays(1);
            final LocalDate last7Days=todayDate.minusDays(7);
            final LocalDate last30Days=todayDate.minusDays(30);
            if(dateString.equals("today")){
                //System.out.println(recognitionRepository.findRecognitionByDateBetween(todayDate,todayDate));
                return recognitionRepository.findRecognitionByDateBetween(todayDate,todayDate);
            }
            if (dateString.equals("yesterday")) {
                //System.out.println(recognitionRepository.findRecognitionByDateBetween(yesterday,todayDate));
                return recognitionRepository.findRecognitionByDate(yesterday);
            }
            if (dateString.equals("last7Days")){
                //System.out.println(recognitionRepository.findRecognitionByDateBetween(last7Days,todayDate));
                return recognitionRepository.findRecognitionByDateBetween(last7Days,todayDate);
            }
            if (dateString.equals("last30Days")){
                //System.out.println(recognitionRepository.findRecognitionByDateBetween(last30Days,todayDate));
                return recognitionRepository.findRecognitionByDateBetween(last30Days,todayDate);
            }
            return null;
        }

        public List<Recognition> findRecognitionBySenderId(Integer senderId){
                return recognitionRepository.findRecognitionBySenderId(senderId);
        }

        public  List<Recognition> findRecognitionByReceiverId(Integer receiverId){
            return recognitionRepository.findRecognitionByReceiverId(receiverId);
        }

        public Optional<Recognition> findRecognitionById(Integer id){
            return recognitionRepository.findRecognitionById(id);
        }

        public void revokeRecognition(Recognition recognition){
            recognitionRepository.save(recognition);
        }
    }

