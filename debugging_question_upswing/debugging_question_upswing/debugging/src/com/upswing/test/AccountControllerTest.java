package com.upswing.test;

import com.upswing.controller.AccountController;
import com.upswing.repository.AccountRepository;
import com.upswing.service.AccountService;

public class AccountControllerTest {

    private static AccountRepository repository;
    private static AccountService service;
    private static AccountController controller;

    public void runTests() {
        setUp();
        testCreateAccountWithNegativeBalanceShouldFail();
    }

    private static void setUp() {
        repository = new AccountRepository();
        service = new AccountService(repository);
        controller = new AccountController(service);
    }

    private static void testCreateAccountWithNegativeBalanceShouldFail() {
        try {
            controller.createAccount("789", "Alice", -100);
            SimpleTestFramework.assertTrue(false, "Account creation should have failed for negative balance");
        } catch (IllegalArgumentException e) {
            SimpleTestFramework.assertTrue(true, "Caught expected exception for negative balance");
        }
    }

}

