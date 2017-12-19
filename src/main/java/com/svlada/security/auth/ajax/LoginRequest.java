package com.svlada.security.auth.ajax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model intended to be used for AJAX based authentication.
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */

public class LoginRequest {
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password,
                        @JsonProperty("clientId") String clientId, @JsonProperty("clientSecret") String clientSecret) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
