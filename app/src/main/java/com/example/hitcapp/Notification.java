package com.example.hitcapp;

import java.io.Serializable;

public class Notification implements Serializable {
    private String title;
    private String content;
    private String type;
    private String targetId;
    private String imageUrl;
    private String timestamp;
    private String fullMessage;

    public Notification(String title, String content, String type, String targetId, String imageUrl, String timestamp, String fullMessage) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.targetId = targetId;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.fullMessage = fullMessage;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getType() { return type; }
    public String getTargetId() { return targetId; }
    public String getImageUrl() { return imageUrl; }
    public String getTimestamp() { return timestamp; }
    public String getFullMessage() { return fullMessage; }
}