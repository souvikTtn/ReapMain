package com.Reap.ReapProject.service;

import com.Reap.ReapProject.entity.Role;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User addUser(User user){
       User user1=setBadges(user);
       Integer points=calculatePoints(user1);
       user1.setPoints(points);
       user1.setFullName(user1.getFirstName()+" "+user1.getLastName());
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

    public User getUserByFullName(String fullName){
        return userRepository.findByFullName(fullName);
    }
}
