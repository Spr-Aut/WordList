package com.example.wordlist.tuple;

import androidx.room.ColumnInfo;

public class WordNameMemTuple {
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "memory")
    private int memory;


    public WordNameMemTuple() {
    }

    public WordNameMemTuple(String name, int memory) {
        this.name = name;
        this.memory = memory;
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
     * @return memory
     */
    public int getMemory() {
        return memory;
    }

    /**
     * 设置
     * @param memory
     */
    public void setMemory(int memory) {
        this.memory = memory;
    }

    public String toString() {
        return "WordNameMemTuple{name = " + name + ", memory = " + memory + "}";
    }
}
