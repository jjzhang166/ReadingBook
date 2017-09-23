package com.agenthun.readingroutine.datastore;

import cn.bmob.v3.BmobObject;


public class Feedback extends BmobObject {
    private String username;
    private String content;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
