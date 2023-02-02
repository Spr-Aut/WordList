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