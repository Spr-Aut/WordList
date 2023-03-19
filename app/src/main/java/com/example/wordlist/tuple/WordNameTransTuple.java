package com.example.wordlist.tuple;

import androidx.room.ColumnInfo;

public class WordNameTransTuple {
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "desc")
    private String desc;
    @ColumnInfo(name = "symbol_uk")
    private String symbol_uk;
    @ColumnInfo(name = "symbol_us")
    private String symbol_us;

    public String getSymbol_uk() {
        return symbol_uk;
    }

    public void setSymbol_uk(String symbol_uk) {
        this.symbol_uk = symbol_uk;
    }

    public String getSymbol_us() {
        return symbol_us;
    }

    public void setSymbol_us(String symbol_us) {
        this.symbol_us = symbol_us;
    }

    public WordNameTransTuple() {
    }

    public WordNameTransTuple(String name, String desc) {
        this.name = name;
        this.desc = desc;
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
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return "WordNameTransTuple{name = " + name + ", desc = " + desc + "}";
    }
}
