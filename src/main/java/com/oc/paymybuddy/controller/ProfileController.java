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

/**
 * The ProfileController class is responsible for handling HTTP requests related to a user's profile and account.
 * It provides methods for displaying showing the profile page, editing the information of the profile and adding
 * money to the account.
 */
@Controller
public class ProfileController {

    private static final Logger logger = getLogger(ProfileController.class);
    @Autowired
    private UserAccountService userAccountService;

    /**
     * Handles GET requests to the "/profile" endpoint, allowing users to view their user account profile.
     *
     * @param model  The Spring MVC model used for storing data to be rendered in the view.
     * @return the view name "/profile" or Redirects to "register" after failing authentication.
     */
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

    /**
     * Handles POST requests to the "/addmoney" endpoint, allowing users to add money to their balance.
     *
     * @param money    The amount in Integer they wish to add
     * @param model    The Spring MVC model used for storing data to be rendered in the view.
     * @return Redirects to "profile" or Redirects to "register" after failing authentication.
     */
    @PostMapping("/addmoney")
    public String addMoneyToAccount(
            Model model,
            Integer money) {
        UserAccount currentUser = userAccountService.findCurrentUser();

        if (money == null || money <= 0) {
            logger.error("could not add money to account");
            model.addAttribute("currentUser", currentUser);
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

    /**
     * Handles GET requests to the "/editProfile" endpoint, allowing users to edit their user account profile.
     *
     * @param model  The Spring MVC model used for storing data to be rendered in the view.
     * @return the view name "/editProfile" or Redirects to "register" after failing authentication.
     */
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

    /**
     * Handles POST requests to the "/editProfile" endpoint, allowing users to submit the changes to their profile
     *
     * @param result              The binding result for validation errors.
     * @param redirectAttributes  Used for adding flash attributes to the redirect.
     * @param  updatedUserAccount  UserAccount Object with modified properties
     * @param model               The Spring MVC model used for storing data to be rendered in the view.
     * @return Redirects to "profile" after successfully saving changes or back to "editProfile" with an error message if the form has errors.
     */
    @PostMapping("/editProfile")
    public String editProfile(
            Model model,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("userAccount") UserAccount updatedUserAccount,
            BindingResult result) {
        UserAccount currentUser = userAccountService.findCurrentUser();
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Your profile could not be updated");
            model.addAttribute("currentUser", currentUser);
            return "redirect:/editProfile"; // Return to the edit profile page with errors
        }
        UserAccount updated = userAccountService.saveUserAccount(updatedUserAccount);
        model.addAttribute("currentUser", updated);

        return "redirect:/profile";
    }
}
