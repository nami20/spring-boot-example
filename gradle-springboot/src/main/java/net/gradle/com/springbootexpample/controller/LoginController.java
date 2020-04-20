package net.gradle.com.springbootexpample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import net.gradle.com.springbootexpample.service.SecurityService;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("")
public class LoginController {
//    private static final String jwtTokenCookieName = "JWT-TOKEN";
//    private static final String signingKey = "signingKey";
//    private static final Map<String, String> credentials = new HashMap<>();
    @Autowired
    private SecurityService securityService;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

//    @PostMapping("/login")
//    public ModelAndView login(HttpServletResponse httpServletResponse, String username,
//                        String password, String redirect, ModelAndView modelAndView) {
//
//        System.out.println("username,qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
//        System.out.println(username);
//        securityService.autoLogin(username, password);
//        modelAndView.setViewName("home");
//        return modelAndView;
//    }


}