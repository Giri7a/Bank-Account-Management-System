package com.upswing.test;

import com.upswing.models.Account;
import com.upswing.repository.AccountRepository;
import com.upswing.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AccountServiceTestAdditional {

    private static AccountRepository repository;
    private static AccountService service;

    public void runTests() throws InterruptedException {
        setUpWithMultipleAccounts();
        testApplyAnnualInterestFailure();
        testConcurrentTransferShouldNotCauseInconsistency();
    }

    private static void setUpWithMultipleAccounts() {
        repository = new AccountRepository();
        service = new AccountService(repository);
        repository.updateAccount(new Account("123", "John Doe", 1000.0));
        repository.updateAccount(new Account("456", "Jane Doe", 500.0));
        repository.updateAccount(new Account("789", "Alice", 50.0)); // Low balance account
    }

    private static void testApplyAnnualInterestFailure() {
        setUpWithMultipleAccounts();
        List<Account> originalAccounts = new ArrayList<>(repository.getAllAccounts());

        service.applyAnnualInterest();

        for (int i = 0; i < originalAccounts.size(); i++) {
            Account originalAccount = originalAccounts.get(i);
            Account updatedAccount = repository.getAllAccounts().get(i);

            double expectedInterest = originalAccount.getBalance() * (0.05 / 12);
            double actualInterest = updatedAccount.getBalance() - originalAccount.getBalance();

            System.out.println("Account ID: " + originalAccount.getAccountId());
            System.out.println("Original Balance: " + originalAccount.getBalance());
            System.out.println("Updated Balance: " + updatedAccount.getBalance());
            System.out.println("Expected Interest: " + expectedInterest);
            System.out.println("Actual Interest: " + actualInterest);

            double tolerance = 1e-10;  // Adjust the tolerance based on precision requirements
            SimpleTestFramework.assertEquals(expectedInterest, actualInterest, tolerance, "Interest calculation is incorrect for Account " + originalAccount.getAccountId());
        }
        SimpleTestFramework.assertTrue(true, "Interest applied correctly");
    }





    /**
     * @throws InterruptedException
     * This function tries to simulate real-world concurrent transactions on the same accounts
     */

    private static void testConcurrentTransferShouldNotCauseInconsistency() throws InterruptedException {
        AccountRepository repository = new AccountRepository();
        AccountService service = new AccountService(repository);
        String fromAccountId = "123";
        String toAccountId = "456";
        double initialBalance = 10000.0;
        repository.updateAccount(new Account(fromAccountId, "John Doe", initialBalance));
        repository.updateAccount(new Account(toAccountId, "Jane Doe", 500.0));

        int numberOfThreads = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch latch = new CountDownLatch(numberOfThreads * 5);

        for (int i = 0; i < numberOfThreads * 5; i++) {
            executorService.execute(() -> {
                try {
                    service.transfer(fromAccountId, toAccountId, 100);
                } finally {
                    latch.countDown();
                }
            });
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(10, TimeUnit.SECONDS); // Increase the timeout to 10 seconds

        latch.await(); // Wait for all transfers to complete

        if (!finished) {
            throw new AssertionError("Not all transfers completed in time");
        }

        Account fromAccount = repository.getAccount(fromAccountId);
        Account toAccount = repository.getAccount(toAccountId);

        // Log the final balances for debugging
        System.out.println("Final Balance - Account " + fromAccountId + ": " + fromAccount.getBalance());
        System.out.println("Final Balance - Account " + toAccountId + ": " + toAccount.getBalance());

        // Assert that the total amount transferred does not exceed the initial balance
        SimpleTestFramework.assertTrue(fromAccount.getBalance() >= 0, "Account balance should not be negative");
        SimpleTestFramework.assertEquals(initialBalance - fromAccount.getBalance(), toAccount.getBalance() - 500.0,
                "Transferred amount should match the difference in balances");
    }

}
