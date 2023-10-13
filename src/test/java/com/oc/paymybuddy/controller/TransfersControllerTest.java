package com.oc.paymybuddy.controller;

import com.oc.paymybuddy.entity.SenderRecipientConnection;
import com.oc.paymybuddy.entity.Transaction;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.service.SenderRecipientConnectionService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserAccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransfersController.class)
public class TransfersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountService userAccountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private SenderRecipientConnectionService senderRecipientConnectionService;

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
    public void tryingAccessingTransfersPageWithoutLoginShouldBeUnauthorized() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(null);
        mockMvc.perform(get("/transfers"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingAccessingTransfersPageWihLoginShouldBeAuthorized() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(transactionService.getTransactionsBySender(mockUser, PageRequest.of(0, 10))).thenReturn(Page.empty());
        when(senderRecipientConnectionService.getSenderRecipientConnectionBySender(mockUser, userAccountService.findAllUserAccounts())).thenReturn(List.of());

        mockMvc.perform(get("/transfers")
                        .with(csrf())) // Include CSRF token
                .andExpect(status().isOk())
                .andExpect(view().name("transfers"))
                .andExpect(model().attributeExists("connections", "transactions", "pages", "totalPages", "currentPage"));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void tryingAccessingTransfersPageAfterLoginShouldWork() throws Exception {
        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(1);
        mockTransaction.setAmount(new BigDecimal(100));

        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(transactionService.getTransactionsBySender(mockUser, PageRequest.of(0, 10))).thenReturn(Page.empty());
        when(senderRecipientConnectionService.getSenderRecipientConnectionBySender(mockUser, userAccountService.findAllUserAccounts())).thenReturn(List.of());

        mockMvc.perform(post("/transfers")
                        .param("recipient", "recipientUsername")
                        .param("amount", "100")
                        .param("description", "Test transaction")
                        .with(csrf())) // Include CSRF token
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transfers"));

    }

    @WithMockUser(authorities = "USER")
    @Test
    public void testAddNewConnectionFailure() throws Exception {
        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(userAccountService.findUserAccountByEmail(any())).thenReturn(null);

        mockMvc.perform(
                        post("/addconnection")
                                .param("email", "invalid@example.com")
                                .with(csrf())) // Include CSRF token
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfers"))
                .andExpect(flash().attributeExists("error"))
                .andReturn();
    }


    @WithMockUser(authorities = "USER")
    @Test
    public void testAddNewConnectionSuccess() throws Exception {
        UserAccount recipientUserAccount = new UserAccount();
        recipientUserAccount.setId(2);
        recipientUserAccount.setEmail("recipient@example.com");

        SenderRecipientConnection newSenderRecipientConnection = new SenderRecipientConnection();
        newSenderRecipientConnection.setId(1);
        newSenderRecipientConnection.setSender(mockUser);
        newSenderRecipientConnection.setRecipient(recipientUserAccount);

        when(userAccountService.findCurrentUser()).thenReturn(mockUser);
        when(userAccountService.findUserAccountByEmail(recipientUserAccount.getEmail())).thenReturn(recipientUserAccount);
        when(senderRecipientConnectionService.createSenderRecipientConnection(mockUser, recipientUserAccount)).thenReturn(newSenderRecipientConnection);
        when(senderRecipientConnectionService.checkIfsenderRecipientConnectionRepositoryExists(newSenderRecipientConnection)).thenReturn(false);

        mockMvc.perform(
                        post("/addconnection")
                                .param("email", recipientUserAccount.getEmail())
                                .with(csrf())) // Include CSRF token
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfers"))
                .andExpect(flash().attributeExists("success"))
                .andReturn();
    }
}
