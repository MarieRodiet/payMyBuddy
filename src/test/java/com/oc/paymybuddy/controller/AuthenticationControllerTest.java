package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.security.CustomUserDetailsService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountService userAccountService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    private static UserAccount mockUser;
    @BeforeAll
    public static void setUpBeforeClass() {
        mockUser = new UserAccount();
        mockUser.setEmail("testUser@gmail.com");
        mockUser.setPassword("password");
        mockUser.setFirstname("firstname");
        mockUser.setLastname("lastname");
        mockUser.setBalance(BigDecimal.ONE);
        mockUser.setAccountNumber("0011AB");
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingAccessingRegisterPageShouldWork() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingRegisteringAnExistingUserShouldFail() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(null);

        mockMvc.perform(post("/register")
                        .flashAttr("user", mockUser)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingRegisteringInValidUserShouldRedirectToRegisterForm() throws Exception {
        mockUser.setFirstname(null);

        mockMvc.perform(post("/register")
                        .flashAttr("user", mockUser)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"))
                .andExpect(model().attributeExists("user"));

    }
    @Test
    public void tryingAccessingLoginPageShouldWork() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

}
