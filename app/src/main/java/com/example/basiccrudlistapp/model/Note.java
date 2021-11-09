package com.example.basiccrudlistapp.model;

import android.graphics.Bitmap;

public class Note {
    private String id;
    private String title;
    private String content;
    private Bitmap bitmap;
    private boolean hasNewImage = false;

    public Note(String title, String content, String id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public boolean hasNewImage() {
        return hasNewImage;
    }

    public void setHasNewImage(boolean hasNewImage) {
        this.hasNewImage = hasNewImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
