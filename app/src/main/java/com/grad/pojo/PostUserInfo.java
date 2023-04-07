package com.grad.pojo;

public class PostUserInfo {
    private String avatarUrl; //
    private String username; //
    private String userHouseAddr; //

    public PostUserInfo(String avatarUrl, String username, String userHouseAddr) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.userHouseAddr = userHouseAddr;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserHouseAddr() {
        return userHouseAddr;
    }

    public void setUserHouseAddr(String userHouseAddr) {
        this.userHouseAddr = userHouseAddr;
    }

}
