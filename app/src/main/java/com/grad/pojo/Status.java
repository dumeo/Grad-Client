package com.grad.pojo;

public class Status {
    private String status;
    private String msg;

    public Status(String status) {
        this.status = status;
    }

    public Status(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}