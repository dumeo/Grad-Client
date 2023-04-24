package com.grad.information.reserve;

public class ReserveItem {
    private String reserveId;
    private String uid;
    private String qrUrl;
    private String reserveContent;
    private String phoneNum;
    private String reserveDate;

    public ReserveItem(String reserveId, String uid, String qrUrl, String reserveContent, String phoneNum, String reserveDate) {
        this.reserveId = reserveId;
        this.uid = uid;
        this.qrUrl = qrUrl;
        this.reserveContent = reserveContent;
        this.phoneNum = phoneNum;
        this.reserveDate = reserveDate;
    }

    public String getReserveId() {
        return reserveId;
    }

    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getReserveContent() {
        return reserveContent;
    }

    public void setReserveContent(String reserveContent) {
        this.reserveContent = reserveContent;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }
}
