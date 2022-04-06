package com.ww;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private User user;

    public static String datePattern = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat dateFormat = new SimpleDateFormat(datePattern);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String outputBroadcastMessage() {
        return this.outputMessage("*");
    }

    public String outputMessage(String target) {
        Date date = Calendar.getInstance().getTime();
        return "[" + dateFormat.format(date) + "] " + this.user.getFullName() + " > " + target + " : \"" + this.text + "\"";
    }
}
