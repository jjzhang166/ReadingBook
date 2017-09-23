package com.agenthun.readingroutine.datastore;

import cn.bmob.v3.BmobObject;


public class BookInfo extends BmobObject {
    private static final long serialVersionUID = 1L;

    private UserData userData;
    private String bookName;
    private Integer bookColor;
    private String bookAlarmTime;

    public BookInfo() {

    }

    public BookInfo(UserData userData, String bookName, Integer bookColor, String bookAlarmTime) {
        this.userData = userData;
        this.bookName = bookName;
        this.bookColor = bookColor;
        this.bookAlarmTime = bookAlarmTime;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getBookColor() {
        return bookColor;
    }

    public void setBookColor(Integer bookColor) {
        this.bookColor = bookColor;
    }

    public String getBookAlarmTime() {
        return bookAlarmTime;
    }

    public void setBookAlarmTime(String bookAlarmTime) {
        this.bookAlarmTime = bookAlarmTime;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "ObjectId=" + getObjectId() + '\'' +
                "userData=" + userData +
                ", bookName='" + bookName + '\'' +
                ", bookColor=" + bookColor +
                ", bookAlarmTime='" + bookAlarmTime + '\'' +
                '}';
    }
}
