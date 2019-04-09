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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
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
                    + ":8080/reset-password?resetToken=" + user.getResetToken());

            emailService.sendEmail(passwordResetEmail);

            HttpSession session=request.getSession();
            session.setAttribute("userToken",user.getResetToken());
            redirectAttributes.addFlashAttribute("success","Email sent to "+user.getEmail()+" click the link to reset password");
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            return modelAndView;
        }
    }

    @GetMapping("/reset-password")
    public ModelAndView showResetPasswordPage(HttpServletRequest request,@RequestParam("resetToken")String token,RedirectAttributes redirectAttributes){
        ModelAndView modelAndView=new ModelAndView("reset-password");
        redirectAttributes.addAttribute("error");
        HttpSession session=request.getSession();
        String sessionToken=(String) session.getAttribute("userToken");
        try {
            if(!sessionToken.equals(token)){
                ModelAndView modelAndView1=new ModelAndView("redirect:/");
                redirectAttributes.addFlashAttribute("loginError","Invalid Token");
                return modelAndView1;
            }
        }
        catch (NullPointerException e){
            ModelAndView modelAndView1=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("loginError","UnAuthorized Access");
            return modelAndView1;
        }
        return modelAndView;
    }

    @PostMapping("/reset-password")
    public ModelAndView processResetFrom(HttpServletRequest request, @RequestParam Map<String, String> requestParams, RedirectAttributes redirectAttributes){
        HttpSession session=request.getSession();
        String token=(String) session.getAttribute("userToken");
        System.out.println(token);
        Optional<User> user=userService.findByResetToken(token);
        System.out.println(user.get());

        if(!user.isPresent()){
            ModelAndView modelAndView=new ModelAndView("redirect:/reset-password?resetToken="+token);
            redirectAttributes.addFlashAttribute("error","No User With this Token Exists");
            return modelAndView;
        }

        if(requestParams.get("passwordField").length()<=6){
            ModelAndView modelAndView=new ModelAndView("redirect:/reset-password?resetToken="+token);
            redirectAttributes.addFlashAttribute("error","password should be atleast 6 characters long");
            return modelAndView;
        }
        else {
            User user1=user.get();
            user1.setPassword(requestParams.get("passwordField"));
            user1.setResetToken(null);
            userService.updateUser(user1);
            ModelAndView modelAndView=new ModelAndView("redirect:/");
            redirectAttributes.addFlashAttribute("success","You Have SuccessFully reset your Password.Login With New Password");
            return modelAndView;
        }
    }
}
