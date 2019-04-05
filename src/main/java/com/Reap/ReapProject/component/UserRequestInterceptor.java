package com.Reap.ReapProject.component;

import com.Reap.ReapProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class UserRequestInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    HttpSession session;
    LoggedInUser loggedInUser;

    public LoggedInUser getLoggedInUser(HttpSession session) {
        System.out.println((LoggedInUser) session.getAttribute("loginUser"));
        return (LoggedInUser) session.getAttribute("loginUser");
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(request.getRequestURI().equals("/login")){
            session=request.getSession();
            loggedInUser=(LoggedInUser) session.getAttribute("loginUser");
            System.out.println("login user "+loggedInUser);
            System.out.println("login finished");
        }
    }
}