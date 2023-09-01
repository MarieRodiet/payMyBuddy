package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.UserAccountService;
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
public class ProfileController {

    private static final Logger logger = getLogger(ProfileController.class);
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        UserAccount currentUser = userAccountService.findCurrentUser();

        if (currentUser != null) {
            logger.info("getting profile for current user");
            model.addAttribute("currentUser", currentUser);
            return "profile";
        } else {
            logger.error("could not authenticate user");
            return "redirect:/register";
        }
    }

    @PostMapping("/addmoney")
    public String addMoneyToAccount(
            Model model,
            Integer money) {
        UserAccount currentUser = userAccountService.findCurrentUser();

        if (money == null || money <= 0) {
            logger.error("could not add money to account");
            model.addAttribute("error", "Invalid amount. Please enter a positive value.");
            return "profile";
        }
        if (currentUser != null) {
            logger.info("balance successfully updated for current user");
            UserAccount currentUserUpdatedBalance =  userAccountService.updateUserAccountBalance(currentUser, money);
            model.addAttribute("currentUser", currentUserUpdatedBalance );
            model.addAttribute("success", "Your balance was successfully updated");
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
            logger.info("profile successfully updated");
            model.addAttribute("userAccount", currentUser);
            return "editProfile";
        } else {
            return "redirect:/register";
        }
    }

    @PostMapping("/editProfile")
    public String editProfile(
            Model model,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("userAccount") UserAccount updatedUserAccount,
            BindingResult result) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Your profile could not be updated");
            return "editProfile"; // Return to the edit profile page with errors
        }
        model.addAttribute("currentUser", userAccountService.findCurrentUser());
        userAccountService.saveUserAccount(updatedUserAccount);
        return "redirect:/profile";
    }
}
