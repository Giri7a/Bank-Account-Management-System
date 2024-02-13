package com.upswing.test;

public class SimpleTestFramework {

    public static void assertEquals(Object expected, Object actual, String message) {
        if ((expected == null && actual != null) || (expected != null && !expected.equals(actual))) {
            throw new AssertionError("Assertion failed: " + message + ". Expected " + expected + ", but was " + actual);
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }

    public static void assertThrows(Class<? extends Throwable> expectedType, Runnable task, String message) {
        try {
            task.run();
            throw new AssertionError("Expected exception " + expectedType.getName() + " was not thrown: " + message);
        } catch (Throwable actualException) {
            if (!expectedType.isInstance(actualException)) {
                throw new AssertionError("Unexpected exception type thrown: " + actualException.getClass().getName() + ", expected: " + expectedType.getName());
            }
        }
    }

    public static void fail(String message) {
        throw new AssertionError("Test failed: " + message);
    }

    public static void assertEquals(double expected, double actual, double tolerance, String message) {
        if (Math.abs(expected - actual) > tolerance) {
            throw new AssertionError("Assertion failed: " + message + ". Expected " + expected + ", but was " + actual);
        }
    }

}
