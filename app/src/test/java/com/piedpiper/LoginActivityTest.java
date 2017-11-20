package com.piedpiper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by rahulajmera on 11/19/17.
 */
public class LoginActivityTest {
    @Test
    public void login() throws Exception {
        LoginActivity login = new LoginActivity();
        String status = login.login("", "");
        assertEquals(status, "No username");
        status = login.login("User", "");
        assertEquals(status, "No password");
        status = login.login("test@test.com", "testing");
        assertEquals(status, "login");
    }
}