package com.ww;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private User userSender, userReceiver;
    private boolean isBroadcast, isCommand;

    public static String datePattern = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat dateFormat = new SimpleDateFormat(datePattern);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public User getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(User userReceiver) {
        this.userReceiver = userReceiver;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public void setBroadcast(boolean broadcast) {
        isBroadcast = broadcast;
    }

    public boolean isCommand() {
        return isCommand;
    }

    public void setCommand(boolean command) {
        isCommand = command;
    }

    public String outputMessage() {
        Date date = Calendar.getInstance().getTime();
        if (this.isCommand()) {
            return this.text;
        }

        String message = "[" + dateFormat.format(date) + "] %s > %s : \"" + this.text + "\"";
        if (this.isBroadcast) {
            return String.format(message, this.userSender.getFullName(), "*");
        }
        return String.format(message, this.userSender.getFullName(), this.userReceiver.getFullName());
    }
}
