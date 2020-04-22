package net.gradle.com.springbootexpample.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import net.gradle.com.springbootexpample.model.User;
import net.gradle.com.springbootexpample.service.UserService;
import net.gradle.com.springbootexpample.service.SecurityService;
import net.gradle.com.springbootexpample.validator.UserValidator;

@RestController
@RequestMapping("")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/signup")
    public ModelAndView registration() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView createNewUser(@Valid final User UserDetails, BindingResult bindingResult) {
        System.out.println(UserDetails);
        userValidator.validate(UserDetails, bindingResult);
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        }
        userService.save(UserDetails);

        securityService.autoLogin(UserDetails.getUsername(), UserDetails.getPasswordConfirm());
        modelAndView.setViewName("home");
        return modelAndView;
    }
}