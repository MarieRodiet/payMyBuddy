package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.UserAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountService userAccountService;

    private static UserAccount mockUser;
    @BeforeAll
    public static void setUpBeforeClass() {
        mockUser = new UserAccount();
        mockUser.setId(1);
        mockUser.setEmail("testUser");
        mockUser.setPassword("password");
        mockUser.setFirstname("firstname");
        mockUser.setLastname("lastname");
        mockUser.setBalance(BigDecimal.ONE);
        mockUser.setAccountNumber("0011AB");
    }
    @Test
    public void tryingAccessingUserProfilePageWithoutLoginShouldBeUnauthorized() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(null);
        mockMvc.perform(get("/profile"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingAccessingUserProfilePageAfterLoginShouldWork() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        mockMvc.perform(get("/profile")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("currentUser"));

    }

    @Test
    public void tryingAccessingEditProfilePageWithoutLoginShouldBeUnauthorized() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(null);
        mockMvc.perform(get("/editProfile"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingAccessingEditProfilePageAfterLoginShouldWork() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        mockMvc.perform(get("/editProfile")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("editProfile"))
                .andExpect(model().attributeExists("currentUser"));

    }

    @WithMockUser(authorities = "USER")
    @Test
    public void editingProfileShouldUpdateUser() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(userAccountService.saveUserAccount(mockUser)).thenReturn(mockUser);

        mockMvc.perform(post("/editProfile")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()) // Expect a redirection status code
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void editingProfileWithErrorsShouldReturnToProfilePage() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        UserAccount invalidUserAccount =  new UserAccount();
        invalidUserAccount .setEmail("");
        invalidUserAccount .setPassword("");
        invalidUserAccount .setFirstname("");
        invalidUserAccount .setLastname("");
        invalidUserAccount .setBalance(BigDecimal.ONE);
        invalidUserAccount .setAccountNumber("0011AB");

        mockMvc.perform(post("/editProfile")
                        .flashAttr("userAccount", invalidUserAccount)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void addingValidAmountShouldUpdateBalance() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(userAccountService.updateUserAccountBalance(mockUser, 100)).thenReturn(mockUser);

        mockMvc.perform(post("/addmoney")
                        .param("money", "100")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("currentUser", "success"))
                .andExpect(model().attribute("success", "Your balance was successfully updated"));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void addingInvalidAmountShouldReturnErrorMessage() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(userAccountService.updateUserAccountBalance(mockUser, 0)).thenReturn(mockUser);

        mockMvc.perform(post("/addmoney")
                        .param("money", "0")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attribute("error", "Invalid amount. Please enter a positive value."));
    }

    @Test
    public void addMoneyToAccountWithoutLoginShouldRedirectToRegister() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(null);

        mockMvc.perform(post("/addmoney")
                        .param("money", "100"))
                .andExpect(status().isForbidden());
    }

}
