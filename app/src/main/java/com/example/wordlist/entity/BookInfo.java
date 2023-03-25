package com.example.wordlist.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BookInfo {
    @PrimaryKey
    @NonNull
    private String name;
    private boolean hasDownload;

    public BookInfo() {
    }

    public BookInfo(String name, boolean hasDownload) {
        this.name = name;
        this.hasDownload = hasDownload;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return hasDownload
     */
    public boolean isHasDownload() {
        return hasDownload;
    }

    /**
     * 设置
     * @param hasDownload
     */
    public void setHasDownload(boolean hasDownload) {
        this.hasDownload = hasDownload;
    }

    public String toString() {
        return "BookInfo{name = " + name + ", hasDownload = " + hasDownload + "}";
    }
}
