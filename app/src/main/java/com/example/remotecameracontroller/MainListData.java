package com.example.remotecameracontroller;

public class MainListData {
    private String location;
    private String url;
    private String modUrl;

    public MainListData(String location, String url, String modUrl) {
        this.location = location;
        this.url = url;
        this.modUrl = modUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModUrl() {
        return modUrl;
    }

    public void setModUrl(String modUrl) {
        this.modUrl = modUrl;
    }
}
