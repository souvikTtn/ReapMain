package com.Reap.ReapProject.controller;

import com.Reap.ReapProject.entity.User;
import com.Reap.ReapProject.service.EmailService;
import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;


@Controller
public class PasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forget")
    public ModelAndView processForgotPasswordForm(@RequestParam("email")String email, HttpServletRequest request, RedirectAttributes redirectAttributes){
        System.out.println(email);
        Optional<User> optionalUser=userService.findByEmail(email);
        if(!optionalUser.isPresent()){
            System.out.println("no user is present");
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","No such User");
            return modelAndView;
        }
        else {
            User user=optionalUser.get();
            user.setResetToken(UUID.randomUUID().toString());
            userService.updateUser(user);

           String url=request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("support@demo.com");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + url
                    + "/reset?token=" + user.getResetToken());

            emailService.sendEmail(passwordResetEmail);

            ModelAndView modelAndView=new ModelAndView("redirect:/reset-password");
            redirectAttributes.addFlashAttribute("success","Email sent to "+user.getEmail());
            return modelAndView;
        }
    }

    @GetMapping("/reset-password")
    public ModelAndView showResetPasswordPage(RedirectAttributes redirectAttributes){
        ModelAndView modelAndView=new ModelAndView("reset-password");
        redirectAttributes.addFlashAttribute("success");
        return modelAndView;
    }
}
