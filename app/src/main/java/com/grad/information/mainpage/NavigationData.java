package com.grad.information.mainpage;

public class NavigationData {
    private int avatarUri;
    private String title;

    public NavigationData(int avatarUri, String title) {
        this.avatarUri = avatarUri;
        this.title = title;
    }

    public int getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(int avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
