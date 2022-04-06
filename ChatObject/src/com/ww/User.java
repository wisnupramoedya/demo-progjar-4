package com.ww;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
