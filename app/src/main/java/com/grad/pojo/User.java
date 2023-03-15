package com.grad.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class User {


    private long uid;
    private String username;
    private String password;
    private String email;
    private String communityName;
    private String avatarUrl = "";
    private int emailValid;
    private String createDate;
    public User() {
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", communityName='" + communityName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", emailValid=" + emailValid +
                ", createDate=" + createDate +
                '}';
    }

    public User(String username, String password, String email, String communityName, String avatarUrl, int emailValid, String createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.communityName = communityName;
        this.avatarUrl = avatarUrl;
        this.emailValid = emailValid;
        this.createDate = createDate;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public long getEmailValid() {
        return emailValid;
    }

    public void setEmailValid(int emailValid) {
        this.emailValid = emailValid;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
