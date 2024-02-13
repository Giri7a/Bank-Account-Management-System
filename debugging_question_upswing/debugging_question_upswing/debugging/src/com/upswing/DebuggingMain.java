package com.upswing;

import com.upswing.test.AccountControllerTest;
import com.upswing.test.AccountServiceTest;
import com.upswing.test.AccountServiceTestAdditional;

public class DebuggingMain {

    public static void main(String[] args) throws Exception {
        AccountServiceTest accountServiceTest = new AccountServiceTest();
        accountServiceTest.runTests();

        AccountControllerTest accountControllerTest = new AccountControllerTest();
        accountControllerTest.runTests();

        AccountServiceTestAdditional accountServiceTestAdditional = new AccountServiceTestAdditional();
        accountServiceTestAdditional.runTests();
    }
}
