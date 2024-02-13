package com.upswing.repository;

import com.upswing.models.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {
    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    public Account createAccount(Account account){
        accounts.put(account.getAccountId(), account);
        return getAccount(account.getAccountId());
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public void updateAccount(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
}

