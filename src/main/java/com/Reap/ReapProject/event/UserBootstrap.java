package com.Reap.ReapProject.event;

import com.Reap.ReapProject.entity.Role;
import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.repository.UserRepository;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserBootstrap {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void init(){
        System.out.println("your application is up and running");

        if(!userRepository.findAll().iterator().hasNext()){
            User user=new User();
            user.setFirstName("Souvik");
            user.setLastName("Chakraborty");
            user.setEmail("souvikc40@gmail.com");
            user.setPassword("souvikdgreat");
            user.setActive(true);
            //user.setRoleList(Arrays.asList(Role.ADMIN,Role.USER));
            Set<Role> roles=new HashSet<>();
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            user.setRoleSet(roles);
            System.out.println(user+" initilaized");
            userService.addUser(user);
        }
    }
}
