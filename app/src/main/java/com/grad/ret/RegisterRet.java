package com.grad.ret;

import java.lang.annotation.Retention;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class RegisterRet {

    private int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    private int statusCode;

    @Override
    public String toString() {
        return "RegisterRet{" +
                "uid=" + uid +
                ", statusCode=" + statusCode +
                ", Msg='" + Msg + '\'' +
                '}';
    }

    private String Msg;

}