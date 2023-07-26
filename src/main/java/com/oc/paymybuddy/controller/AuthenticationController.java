package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.security.CustomUserDetailsService;
import com.oc.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticationController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create empty model object to store form data
        UserAccount user = new UserAccount();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserAccount user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model){
        UserAccount isUserAlreadyRegistered = userAccountService.findUserAccountByEmail(user.getEmail());

        //isUserAlreadyRegistered should be null
        if(isUserAlreadyRegistered != null && isUserAlreadyRegistered.getEmail() != null && !isUserAlreadyRegistered.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        customUserDetailsService.saveUserAccount(user);
        customUserDetailsService.authenticateUser(user);
        return "redirect:/transfers";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }
}

