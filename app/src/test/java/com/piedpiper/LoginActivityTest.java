package com.piedpiper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by rahulajmera on 11/19/17.
 */
public class LoginActivityTest {
    @Test
    public void noCredentials() throws Exception {
        LoginActivity login = new LoginActivity();
        String status = login.login("", "");
        assertEquals(status, "No credentials");
    }
    @Test
    public void noUsername() throws Exception {
        LoginActivity login = new LoginActivity();
        String status = login.login("", "password");
        assertEquals(status, "No username");

    }
    @Test
    public void noPassword() throws Exception {
        LoginActivity login = new LoginActivity();
        String status = login.login("username", "");
        assertEquals(status, "No password");
    }
    @Test
    public void login() throws Exception {
        LoginActivity login = new LoginActivity();
        String status = login.login("test@test.com", "testing");
        assertEquals(status, "login");
    }

}