package com.rdc.bms.entity;



public class Book {
    //书名
    private String name;

    //作者
    private String author;

    //书封面
    private String coverUrl;

    //简介
    private String intro;

    //国际标准书号
    private String isbn;

    //馆藏序号
    private String bookId;

    //位置
    private String location;

    //状态：已借出、闲置、损坏、仅供阅读
    private int state;

    //还书时间
    private String returnTime;

    //出版社
    private String publishingHouse;

    public String getIntro() {
        return intro == null ? "" : intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? "" : intro;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? "" : author;
    }

    public String getCoverUrl() {
        return coverUrl == null ? "" : coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl == null ? "" : coverUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIsbn() {
        return isbn == null ? "" : isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? "" : isbn;
    }

    public String getReturnTime() {
        return returnTime == null ? "" : returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime == null ? "" : returnTime;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getBookId() {
        return bookId == null ? "" : bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId == null ? "" : bookId;
    }

    public String getLocation() {
        return location == null ? "" : location;
    }

    public void setLocation(String location) {
        this.location = location == null ? "" : location;
    }

    public String getPublishingHouse() {
        return publishingHouse == null ? "" : publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse == null ? "" : publishingHouse;
    }
}
