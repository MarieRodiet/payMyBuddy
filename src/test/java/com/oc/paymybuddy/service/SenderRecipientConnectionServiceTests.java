package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.entity.SenderRecipientConnection;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.SenderRecipientConnectionRepository;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SenderRecipientConnectionServiceTests {

    @MockBean
    private SenderRecipientConnectionRepository senderRecipientConnectionRepository;
    @Autowired
    private SenderRecipientConnectionService senderRecipientConnectionService;

    private static SenderRecipientConnection connection;

    private static UserAccount sender = new UserAccount(
                        1,
                                "marie@gmail.com",
                                "password",
                                "marieLASTNAME",
                                "marieFIRSTNAME",
                                "ÀÀÀAAAA",
                                new BigDecimal(100),
                        Role.USER.name());
    private static UserAccount recipient = new UserAccount(
                    2,
                            "bob@gmail.com",
                            "password",
                            "marieLASTNAME",
                            "marieFIRSTNAME",
                            "ÀÀÀAAAA",
                    BigDecimal.ONE,
                    Role.USER.name());

    @WithMockUser(authorities = "USER")
    @Test
    public void testSaveSenderRecipientConnectionShouldSave() {
        when(senderRecipientConnectionRepository.save(connection)).thenReturn(connection);
        SenderRecipientConnection saved = senderRecipientConnectionService.saveSenderRecipientConnection(connection);
        assertEquals(connection, saved);

    }

    @WithMockUser(authorities = "USER")
    @Test
    public void testCreateSenderRecipientConnectionShouldCreate() {
        SenderRecipientConnection created = senderRecipientConnectionService.createSenderRecipientConnection(sender, recipient);
        SenderRecipientConnection newSenderRecipientConnection = new SenderRecipientConnection();
        newSenderRecipientConnection.setSender(sender);
        newSenderRecipientConnection.setRecipient(recipient);
        assertEquals(newSenderRecipientConnection, created);

    }


}
