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
            Set<Role> roles=new HashSet<>();
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            user.setRoleSet(roles);
            System.out.println(user+" initilaized");
            userService.addUser(user);

            User user1 = new User();
            user1.setActive(true);
            user1.setEmail("deepika.tiwari@tothenew.com");
            user1.setFirstName("Deepika");
            user1.setLastName("Tiwari");
            user1.setPassword("deesssooo");
            userService.addUser(user1);

            User user3 = new User();
            user3.setActive(true);
            user3.setEmail("divya.arora@tothenew.com");
            user3.setFirstName("Divya");
            user3.setLastName("Arora");
            user3.setPassword("deesssooo");
            userService.addUser(user3);

            User user2 = new User();
            user2.setActive(true);
            user2.setEmail("dhruv.oberoi@tothenew.com");
            user2.setFirstName("Dhruv");
            user2.setLastName("Oberoi");
            user2.setPassword("deesssooo");
            userService.addUser(user2);

            User user4 = new User();
            user4.setActive(true);
            user4.setEmail("kanchan.sinha@tothenew.com");
            user4.setFirstName("Kanchan");
            user4.setLastName("Sinha");
            user4.setPassword("deesssooo");
            userService.addUser(user4);
        }
    }
}
