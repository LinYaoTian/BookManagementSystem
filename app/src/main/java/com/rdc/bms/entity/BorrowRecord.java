package com.rdc.bms.entity;

public class BorrowRecord {

    private String coverUrl;
    private String bookName;
    private String bookId;
    private String userId;
    private String returnTime;
    private String borrowTime;

    public String getCoverUrl() {
        return coverUrl == null ? "" : coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl == null ? "" : coverUrl;
    }

    public String getBookName() {
        return bookName == null ? "" : bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName == null ? "" : bookName;
    }

    public String getBookId() {
        return bookId == null ? "" : bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId == null ? "" : bookId;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? "" : userId;
    }

    public String getReturnTime() {
        return returnTime == null ? "" : returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime == null ? "" : returnTime;
    }

    public String getBorrowTime() {
        return borrowTime == null ? "" : borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime == null ? "" : borrowTime;
    }
}
