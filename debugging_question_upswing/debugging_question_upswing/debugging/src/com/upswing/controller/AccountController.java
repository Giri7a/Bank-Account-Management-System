package com.upswing.controller;

import com.upswing.models.Account;
import com.upswing.service.AccountService;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createAccount(String accountId, String accountHolderName, double initialBalance) {
        accountService.createAccount(new Account(accountId, accountHolderName, initialBalance));
    }

    public void deposit(String accountId, double amount) {
        accountService.deposit(accountId, amount);
    }

    public void withdraw(String accountId, double amount) {
        accountService.withdraw(accountId, amount);
    }


}

