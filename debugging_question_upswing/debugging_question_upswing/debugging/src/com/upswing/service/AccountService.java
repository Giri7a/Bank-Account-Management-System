package com.upswing.service;

import com.upswing.models.Account;
import com.upswing.repository.AccountRepository;

import java.util.List;
import java.util.Random;

public class AccountService {
    private final AccountRepository accountRepository;
    private final Random random = new Random();
    private final double annualInterestRate = 0.05; // 5% annual interest

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public synchronized void createAccount(Account account) {
        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("Initial balance must be non-negative");
        }
        accountRepository.createAccount(account);
    }


    public synchronized Account getAccount(String accountId) {
        return accountRepository.getAccount(accountId);
    }

    public synchronized void deposit(String accountId, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount must be non-negative");
        }
        Account account = accountRepository.getAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        accountRepository.updateAccount(account);
    }


    public synchronized void withdraw(String accountId, double amount) {
        Account account = accountRepository.getAccount(accountId);
        if (amount > account.getBalance()) {
            throw new IllegalStateException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.updateAccount(account);
    }



    public synchronized void transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAccount = accountRepository.getAccount(fromAccountId);
        Account toAccount = accountRepository.getAccount(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Source or destination account does not exist");
        }

        if (amount > fromAccount.getBalance()) {
            throw new IllegalStateException("Insufficient funds for transfer");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);

        try {
            Thread.sleep(random.nextInt(100)); // Simulate delay
        } catch (InterruptedException e) {

        }

        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.updateAccount(fromAccount);
        accountRepository.updateAccount(toAccount);
    }


    public synchronized void applyAnnualInterest() {
        List<Account> allAccounts = accountRepository.getAllAccounts();
        for (Account account : allAccounts) {
            double annualInterest = account.getBalance() * annualInterestRate; // Annual interest
            double monthlyInterest = annualInterest / 12; // Monthly interest

            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Original Balance: " + account.getBalance());
            System.out.println("Annual Interest: " + annualInterest);
            System.out.println("Monthly Interest: " + monthlyInterest);

            double newBalance = account.getBalance() + monthlyInterest;
            accountRepository.updateAccount(new Account(account.getAccountId(), account.getAccountHolderName(), newBalance));
        }
    }

}
