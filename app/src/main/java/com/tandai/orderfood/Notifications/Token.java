package com.tandai.orderfood.Notifications;

public class Token {
    private String token;
    private int checkToken;

    public Token() {
    }

    public Token(String token, int checkToken) {
        this.token = token;
        this.checkToken = checkToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCheckToken() {
        return checkToken;
    }

    public void setCheckToken(int checkToken) {
        this.checkToken = checkToken;
    }
}
