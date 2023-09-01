package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        UserAccount currentUser = userAccountService.findCurrentUser();

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
        UserAccount currentUser = userAccountService.findCurrentUser();

        if (currentUser != null) {
            UserAccount currentUserUpdatedBalance =  userAccountService.updateUserAccountBalance(currentUser, money);

            model.addAttribute("currentUser", currentUserUpdatedBalance );
            return "profile";
        } else {
            return "redirect:/register";
        }
    }

    @GetMapping("/editProfile")
    public String getEditProfilePage(Model model) {
        UserAccount currentUser = userAccountService.findCurrentUser();
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
            Model model,
            @ModelAttribute("userAccount") @Valid UserAccount updatedUserAccount,
            BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount currentUser = userAccountService.findUserAccountByEmail(authentication.getName());
        currentUser.setFirstname(updatedUserAccount.getFirstname());
        currentUser.setLastname(updatedUserAccount.getLastname());
        currentUser.setAccountNumber(updatedUserAccount.getAccountNumber());
        currentUser.setBalance(updatedUserAccount.getBalance());
        if (result.hasErrors()) {
            return "editProfile"; // Return to the edit profile page with errors
        }
        model.addAttribute("currentUser", currentUser);
        userAccountService.saveUserAccount(updatedUserAccount);
        return "redirect:/profile";
    }
}
