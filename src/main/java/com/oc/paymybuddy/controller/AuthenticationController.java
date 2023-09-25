package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.security.CustomUserDetailsService;
import com.oc.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.apache.logging.log4j.LogManager.getLogger;

@Controller
public class AuthenticationController {
    private static final Logger logger = getLogger(AuthenticationController.class);
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserAccount user = new UserAccount();
        model.addAttribute("user", user);
        logger.info("register page");
        return "register";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserAccount user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model){
        if(result.hasErrors()){
            logger.error("register form contains errors");
            model.addAttribute("user", user);
            return "/register";
        }

        UserAccount isUserAlreadyRegistered = userAccountService.findCurrentUser();
        //isUserAlreadyRegistered should be null
        if(isUserAlreadyRegistered != null && isUserAlreadyRegistered.getEmail() != null && !isUserAlreadyRegistered.getEmail().isEmpty()){
            logger.error("user already exists");
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }
        logger.info("user successfully registered");
        customUserDetailsService.saveUserAccount(user);
        customUserDetailsService.authenticateUser(user);
        return "redirect:/transfers";
    }


    @GetMapping("/login")
    public String login(){
        logger.info("login page");
        return "login";
    }
}

