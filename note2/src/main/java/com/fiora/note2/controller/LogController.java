package com.fiora.note2.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogController {
    @Autowired
    private DefaultSecurityManager defaultSecurityManager;

    {
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            ModelAndView mav = new ModelAndView("/netDiskSearch.html");
            return mav;
        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("/note00_catalogue.html");
            return mav;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        ModelAndView mav = new ModelAndView("/note00_catalogue.html");
        return mav;
    }
}
