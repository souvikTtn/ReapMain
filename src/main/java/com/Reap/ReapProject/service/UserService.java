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
            user.setGold(9);
            user.setSilver(6);
            user.setBronze(3);
            return user;
        }
        if(user.getRoleSet().contains(Role.SUPERVISOR)){
            user.setGold(6);
            user.setSilver(3);
            user.setBronze(2);
            return user;
        }
         if(user.getRoleSet().contains(Role.USER)){
            user.setGold(3);
            user.setSilver(2);
            user.setBronze(1);
            return user;
        }
        return user;
    }

    public Integer calculatePoints(User user){
        return  user.getBronze()*10+user.getSilver()*20+user.getGold()*30;
    }
}
