package com.example.wordlist.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
* 单词的详情包含：单词名name、释义desc、
* 英式音标symbol_uk、英式读音sound_uk、美式音标symbol_us、美式读音sound_us、
* 例句sentence、
* 时间戳time_stamp、是否收藏favorite、记忆值memory、熟记程度word_operation、
* 来源网络或本地source、单词分类class_name
* */

@Entity
public class WordInfo {
    @PrimaryKey
    @NonNull
    private String name;//单词名
    private String desc;//释义
    private String symbol_uk;//英式音标
    private String sound_uk;//英式读音
    private String symbol_us;//美式音标
    private String sound_us;//美式读音
    private String sentence;//例句
    private long time_stamp=1675341973622L;//时间戳
    private int favorite;//是否收藏
    private int memory;//记忆值
    private int word_operation;//熟记程度
    private int source;//来源网络或本地
    private int class_name;//单词分类

    public WordInfo() {
    }

    public WordInfo(String name, String desc, String symbol_uk, String sound_uk, String symbol_us, String sound_us, String sentence, long time_stamp, int favorite, int memory, int word_operation, int source, int class_name) {
        this.name = name;
        this.desc = desc;
        this.symbol_uk = symbol_uk;
        this.sound_uk = sound_uk;
        this.symbol_us = symbol_us;
        this.sound_us = sound_us;
        this.sentence = sentence;
        this.time_stamp = time_stamp;
        this.favorite = favorite;
        this.memory = memory;
        this.word_operation = word_operation;
        this.source = source;
        this.class_name = class_name;
    }

    /**
     * 清空WordInfo存储的信息
     * */
    public void initWord(){
        name = "";
        desc = "";
        symbol_uk = "";
        sound_uk = "";
        symbol_us = "";
        sound_us = "";
        sentence = "";
        time_stamp = 1675341973622L;
        favorite = 0;
        memory = 0;
        word_operation = 0;
        source = 0;
        class_name = 0;

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

    /**
     * 获取
     * @return symbol_uk
     */
    public String getSymbol_uk() {
        return symbol_uk;
    }

    /**
     * 设置
     * @param symbol_uk
     */
    public void setSymbol_uk(String symbol_uk) {
        this.symbol_uk = symbol_uk;
    }

    /**
     * 获取
     * @return sound_uk
     */
    public String getSound_uk() {
        return sound_uk;
    }

    /**
     * 设置
     * @param sound_uk
     */
    public void setSound_uk(String sound_uk) {
        this.sound_uk = sound_uk;
    }

    /**
     * 获取
     * @return symbol_us
     */
    public String getSymbol_us() {
        return symbol_us;
    }

    /**
     * 设置
     * @param symbol_us
     */
    public void setSymbol_us(String symbol_us) {
        this.symbol_us = symbol_us;
    }

    /**
     * 获取
     * @return sound_us
     */
    public String getSound_us() {
        return sound_us;
    }

    /**
     * 设置
     * @param sound_us
     */
    public void setSound_us(String sound_us) {
        this.sound_us = sound_us;
    }

    /**
     * 获取
     * @return sentence
     */
    public String getSentence() {
        return sentence;
    }

    /**
     * 设置
     * @param sentence
     */
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    /**
     * 获取
     * @return time_stamp
     */
    public long getTime_stamp() {
        return time_stamp;
    }

    /**
     * 设置
     * @param time_stamp
     */
    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    /**
     * 获取
     * @return favorite
     */
    public int getFavorite() {
        return favorite;
    }

    /**
     * 设置
     * @param favorite
     */
    public void setFavorite(int favorite) {
        this.favorite = favorite;
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

    /**
     * 获取
     * @return word_operation
     */
    public int getWord_operation() {
        return word_operation;
    }

    /**
     * 设置
     * @param word_operation
     */
    public void setWord_operation(int word_operation) {
        this.word_operation = word_operation;
    }

    /**
     * 获取
     * @return source
     */
    public int getSource() {
        return source;
    }

    /**
     * 设置
     * @param source
     */
    public void setSource(int source) {
        this.source = source;
    }

    /**
     * 获取
     * @return class_name
     */
    public int getClass_name() {
        return class_name;
    }

    /**
     * 设置
     * @param class_name
     */
    public void setClass_name(int class_name) {
        this.class_name = class_name;
    }

    public String toString() {
        return "WordInfo{name = " + name + ", desc = " + desc + ", symbol_uk = " + symbol_uk + ", sound_uk = " + sound_uk + ", symbol_us = " + symbol_us + ", sound_us = " + sound_us + ", sentence = " + sentence + ", time_stamp = " + time_stamp + ", favorite = " + favorite + ", memory = " + memory + ", word_operation = " + word_operation + ", source = " + source + ", class_name = " + class_name + "}";
    }
}
