package com.Reap.ReapProject.service;

import com.Reap.ReapProject.component.LoggedInUser;
import com.Reap.ReapProject.entity.Role;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    private static int nameCounter = 0;

    public User addUser(User user){
       User user1=setBadges(user);
       Integer points=calculatePoints(user1);
       user1.setPoints(points);

       //appending counter in case od user with same name
       String fullName=user1.getFirstName()+" "+user1.getLastName();
       List<String>fullnames=userRepository.findAllFullName();
        if (fullnames.contains(fullName)) {
            nameCounter += 1;
            fullName = fullName + " " + nameCounter;
        }
        user1.setFullName(fullName);
        userRepository.save(user1);
        return user1;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id){
        return userRepository.findById(id);
    }

    public User setBadges(User user){

        if (user.getRoleSet().contains(Role.PRACTICE_HEAD)){
            user.setGoldSharable(9);
            user.setSilverSharable(6);
            user.setBronzeSharable(3);
            return user;
        }
        if(user.getRoleSet().contains(Role.SUPERVISOR)){
            user.setGoldSharable(6);
            user.setSilverSharable(3);
            user.setBronzeSharable(2);
            return user;
        }
         if(user.getRoleSet().contains(Role.USER)){
            user.setGoldSharable(3);
            user.setSilverSharable(2);
            user.setBronzeSharable(1);
            return user;
        }
        return user;
    }

    public Integer calculatePoints(User user){
        return  user.getBronzeRedeemable()*10+user.getSilverRedeemable()*20+user.getGoldRedeemable()*30;
    }

    public User getUserByEmailAndPasswordAndActive(String email,String password){
        return userRepository.findByEmailAndPasswordAndActive(email,password,true);
    }

    public User getUserByFullName(String fullName){
        return userRepository.findByFullName(fullName);
    }

    public List<User> findByFullNameLike(String pattern){
       return userRepository.findByFullNameLike(pattern);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByResetToken(String token){
        return userRepository.findByResetToken(token);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public List<String> findAllEmails(){
        return userRepository.findAllEmails();
    }

    public void adminEditUser(User user){
        //setting uo the badges as per role
        User userToSave=setBadges(user);
        Integer points=calculatePoints(userToSave);
        userToSave.setPoints(points);
        updateUser(userToSave);
    }

    public void revokeUserBadge(User sender,User receiver, String badge) {
        if (badge.equals("gold")) {
            receiver.setGoldRedeemable(receiver.getGoldRedeemable() - 1);
            sender.setGoldSharable(sender.getGoldSharable()+1);
        } else if (badge.equals("silver")) {
            receiver.setSilverRedeemable(receiver.getSilverRedeemable() - 1);
            sender.setSilverSharable(sender.getSilverSharable()+1);
        } else {
            receiver.setBronzeRedeemable(receiver.getBronzeRedeemable() - 1);
            sender.setBronzeSharable(sender.getBronzeSharable()+1);
        }
        receiver.setPoints(calculatePoints(receiver));
        userRepository.save(receiver);
        userRepository.save(sender);
    }

    //deduct points on checkout
    public void deductPointsOnCheckout(User user, Integer deductiblePoints) {
        Integer currentPoints = user.getPoints();
        user.setPoints(currentPoints - deductiblePoints);
        userRepository.save(user);
    }


}
