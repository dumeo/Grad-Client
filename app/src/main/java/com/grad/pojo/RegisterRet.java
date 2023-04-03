package com.grad.pojo;

import com.grad.pojo.User;

import java.lang.annotation.Retention;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class RegisterRet {
    private User user;
    private int statusCode;
    private String Msg;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    @Override
    public String toString() {
        return "RegisterRet{" +
                "user=" + user.toString() +
                ", statusCode=" + statusCode +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}