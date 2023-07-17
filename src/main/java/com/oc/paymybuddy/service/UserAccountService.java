package com.oc.paymybuddy.service;

import com.oc.paymybuddy.dto.UserAccountDto;
import com.oc.paymybuddy.entity.UserAccount;

import java.util.List;

public interface UserAccountService {
    void saveUserAccount(UserAccountDto userAccountDto);

    UserAccount findUserAccountByEmail(String email);

    List<UserAccountDto> findAllUserAccounts();
}
