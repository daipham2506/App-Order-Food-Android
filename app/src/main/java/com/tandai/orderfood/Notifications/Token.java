package com.tandai.orderfood.Notifications;

public class Token {
    private String token;
    private boolean checkToken;

    public Token() {
    }

    public Token(String token, boolean checkToken) {
        this.token = token;
        this.checkToken = checkToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isCheckToken() {
        return checkToken;
    }

    public void setCheckToken(boolean checkToken) {
        this.checkToken = checkToken;
    }
}
