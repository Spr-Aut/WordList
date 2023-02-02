package com.example.wordlist.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BookInfo {
    @PrimaryKey
    @NonNull
    private String name;

    private int price;

    private String text;

    private long time=1675341973622L;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BookInfo() {
    }

    /*public RoomTest(String name, int price) {
        this.name = name;
        this.price = price;
    }*/

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
     * @return price
     */
    public int getPrice() {
        return price;
    }

    /**
     * 设置
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    public String toString() {
        return "RoomTest{name = " + name + ", price = " + price + "}";
    }
}
