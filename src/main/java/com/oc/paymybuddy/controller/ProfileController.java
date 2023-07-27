package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Controller
public class ProfileController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount currentUser = userAccountService.findUserAccountByEmail(email);

        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            return "profile";
        } else {
            return "redirect:/register";
        }
    }

    @PostMapping("/addmoney")
    public String addMoneyToAccount(
            Model model,
            Integer money) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount currentUser = userAccountService.findUserAccountByEmail(email);

        if (currentUser != null) {
        userAccountService.increaseUserAccountBalance(currentUser, BigDecimal.valueOf(money));
        UserAccount currentUserUpdatedBalance = userAccountService.findUserAccountByEmail(email);

            model.addAttribute("currentUser", currentUserUpdatedBalance );
            return "profile";
        } else {
            return "redirect:/register";
        }
    }

    @GetMapping("/editProfile")
    public String getEditProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserAccount currentUser = userAccountService.findUserAccountByEmail(email);
        model.addAttribute("currentUser", currentUser);
        if (currentUser != null) {
            model.addAttribute("userAccount", currentUser);
            return "editProfile";
        } else {
            return "redirect:/register";
        }
    }

    @PostMapping("/editProfile")
    public String editProfile(
            @ModelAttribute("currentUser") @Valid UserAccount updatedUserAccount,
            BindingResult result) {
        if (result.hasErrors()) {
            // Handle validation errors, e.g., show error messages in the form
            return "editProfile"; // Return to the edit profile page with errors
        }
        userAccountService.saveUserAccount(updatedUserAccount);
        return "redirect:/profile";
    }
}