package com.tandai.orderfood.Notifications;

public class Sender {
    public Notification notification;
    public String to;

    public Sender(String to,Notification notification) {
        this.notification = notification;
        this.to = to;
    }

    public Sender() {
    }
}
