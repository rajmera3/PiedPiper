package com.piedpiper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sunwoo Yim on 11/19/17.
 */

public class RegisterActivityTest {
    @Test
    public void registerNoEmail() throws Exception {
        RegisterActivity register = new RegisterActivity();
        String[] ans = register.isValid(null, "password");
        assertEquals(ans[0], "Email is not entered");
        assertEquals(ans[1], "Please enter an email");
    }

    @Test
    public void registerNoPassword() throws Exception {
        RegisterActivity register = new RegisterActivity();
        String[] ans = register.isValid("test@gmail.com", null);
        assertEquals(ans[0], "Password is not entered");
        assertEquals(ans[1], "Please enter a password that is at least 6 " +
                "characters long");
    }

    @Test
    public void registerShortPassword() throws Exception {
        RegisterActivity register = new RegisterActivity();
        String[] ans = register.isValid("test@gmail.com", "pass");
        assertEquals(ans[0], "Password is not entered");
        assertEquals(ans[1], "Please enter a password that is at least 6 " +
                "characters long");
    }

    @Test
    public void registerSuccessfully() throws Exception {
        RegisterActivity register = new RegisterActivity();
        Object ans = register.isValid("test@gmail.com", "password");
        assertEquals(ans, null);
    }
}
