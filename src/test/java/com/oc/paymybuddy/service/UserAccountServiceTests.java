package com.oc.paymybuddy.service;

import com.oc.paymybuddy.entity.Role;
import com.oc.paymybuddy.entity.UserAccount;
import com.oc.paymybuddy.repository.UserAccountRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAccountServiceTests {
    @MockBean
    UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountService userAccountService;

    private UserAccount firstUserAccount  = new UserAccount(
            1,
            "marie@gmail.com",
            "password",
            "marieLASTNAME",
            "marieFIRSTNAME",
            "ÀÀÀAAAA",
            new BigDecimal(100),
            Role.USER.name());
    private UserAccount secondUserAccount  = new UserAccount(
            2,
            "bob@gmail.com",
            "password",
            "marieLASTNAME",
            "marieFIRSTNAME",
            "ÀÀÀAAAA",
            BigDecimal.ONE,
            Role.USER.name());
    private List<UserAccount> userAccountList  = Arrays.asList(firstUserAccount, secondUserAccount);

    @WithMockUser(authorities = "USER")
    @Test
    public void findUserAccountByEmailShouldReturnUser(){
        when(this.userAccountRepository.findByEmail("marie@gmail.com")).thenReturn(firstUserAccount);
        UserAccount marie = this.userAccountService.findUserAccountByEmail(firstUserAccount.getEmail());
        Assertions.assertEquals(firstUserAccount.getFirstname(), marie.getFirstname());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void findNonExistingUserAccountByEmailShouldReturnNull(){
        when(this.userAccountRepository.findByEmail("idontexist@gmail.com")).thenReturn(null);
        UserAccount iAmNull = this.userAccountService.findUserAccountByEmail("idontexist@gmail.com");
        Assertions.assertNull(iAmNull);
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void findAllUserAccountsShouldReturnList(){
        when(this.userAccountRepository.findAll()).thenReturn(userAccountList);
        List<UserAccount> userAccounts = this.userAccountService.findAllUserAccounts();
        Assertions.assertEquals(2, userAccounts.size());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void findNonExistingUserAccountsShouldReturnNull(){
        when(this.userAccountRepository.findAll()).thenReturn(null);
        Assertions.assertNull(this.userAccountService.findAllUserAccounts());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void savingValidUserAccountShouldReturnUser(){
        when(this.userAccountRepository.findByEmail("marie@gmail.com")).thenReturn(firstUserAccount);
        when(this.userAccountRepository.save(firstUserAccount)).thenReturn(firstUserAccount);
        UserAccount user = this.userAccountService.saveUserAccount(firstUserAccount);
        user.setFirstname("new firstname");
        user.setLastname("new lastname");
        Assertions.assertEquals("new lastname", firstUserAccount.getLastname());
        Assertions.assertEquals("new firstname", firstUserAccount.getFirstname());
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void savingInexistingUserAccountShouldReturnNull(){
        when(this.userAccountRepository.findByEmail("marie@gmail.com")).thenReturn(null);
        Assertions.assertNull(this.userAccountService.saveUserAccount(firstUserAccount));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void decreaseUserAccountBalanceShouldReturnCorrectAmount(){
        when(this.userAccountRepository.save(firstUserAccount)).thenReturn(firstUserAccount);
        UserAccount user = this.userAccountService.decreaseUserAccountBalance(firstUserAccount, new BigDecimal(10));
        BigDecimal expectedBalance = new BigDecimal(89.95);
        expectedBalance = expectedBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
        // The compareTo method returns 0 if the two values are equal with the specified precision.
        Assertions.assertEquals(0, expectedBalance.compareTo(user.getBalance()));
    }

    @WithMockUser(authorities = "USER")
    @Test
    public void updateUserAccountBalanceShouldAddMoneyToUserAccountBalance(){
        when(this.userAccountRepository.save(firstUserAccount)).thenReturn(firstUserAccount);
        UserAccount user = this.userAccountService.updateUserAccountBalance(firstUserAccount, 10);
        Assertions.assertEquals(new BigDecimal(110), user.getBalance());
    }

}
