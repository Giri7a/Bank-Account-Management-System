package com.upswing.test;

import com.upswing.models.Account;
import com.upswing.repository.AccountRepository;
import com.upswing.service.AccountService;

public class AccountServiceTest {

    private static AccountRepository repository;
    private static AccountService service;

    public void runTests() {
        setUp();
        testDepositShouldFailForNegativeAmount();
        testWithdrawShouldFailForInsufficientFunds();
        testTransferShouldFailForNonExistentAccount();
        testTransferShouldNotAllowOverdraft();
    }

    private static void setUp() {
        repository = new AccountRepository();
        service = new AccountService(repository);
        repository.updateAccount(new Account("123", "John Doe", 1000.0));
        repository.updateAccount(new Account("456", "Jane Doe", 500.0));
    }

    private static void testDepositShouldFailForNegativeAmount() {
        try {
            service.deposit("123", -100);
            SimpleTestFramework.assertTrue(false, "Deposit should have failed for negative amount");
        } catch (IllegalArgumentException e) {
            SimpleTestFramework.assertTrue(true, "Caught expected exception for negative deposit");
        }
    }


    private static void testWithdrawShouldFailForInsufficientFunds() {
        try {
            service.withdraw("123", 1500);
            SimpleTestFramework.assertTrue(false, "Withdraw should have failed for insufficient funds");
        } catch (IllegalStateException e) {
            SimpleTestFramework.assertTrue(true, "Caught expected exception for insufficient funds");
        }
    }

    private static void testTransferShouldFailForNonExistentAccount() {
        try {
            service.transfer("123", "999", 100);
            SimpleTestFramework.assertTrue(false, "Transfer should have failed for non-existent account");
        } catch (IllegalArgumentException e) {
            SimpleTestFramework.assertTrue(true, "Caught expected exception for non-existent account");
        }
    }

    private static void testTransferShouldNotAllowOverdraft() {
        try {
            service.transfer("123", "456", 1500);
            SimpleTestFramework.assertTrue(false, "Transfer should have failed due to overdraft");
        } catch (IllegalStateException e) {
            SimpleTestFramework.assertTrue(true, "Caught expected exception for overdraft");
        }
    }

}

