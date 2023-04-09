package com.grad.pojo;

public class Status {
    private String status;
    private Object msg;

    public Status(String status) {
        this.status = status;
    }

    public Status(String status, Object msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}