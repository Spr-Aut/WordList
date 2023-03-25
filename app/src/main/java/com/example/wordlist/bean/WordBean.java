package com.example.wordlist.bean;

import java.util.ArrayList;

public class WordBean {
    int code;
    String msg;
    ArrayList<WordDataBean> datas;

    public class WordDataBean{
        String name;
        String symbol;
        String sound;
        String desc;
        String course;
        String class_id;
        String class_title;

        public WordDataBean() {
        }

        public WordDataBean(String name, String symbol, String sound, String desc, String course, String class_id, String class_title) {
            this.name = name;
            this.symbol = symbol;
            this.sound = sound;
            this.desc = desc;
            this.course = course;
            this.class_id = class_id;
            this.class_title = class_title;
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
         * @return symbol
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * 设置
         * @param symbol
         */
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        /**
         * 获取
         * @return sound
         */
        public String getSound() {
            return sound;
        }

        /**
         * 设置
         * @param sound
         */
        public void setSound(String sound) {
            this.sound = sound;
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
         * @return course
         */
        public String getCourse() {
            return course;
        }

        /**
         * 设置
         * @param course
         */
        public void setCourse(String course) {
            this.course = course;
        }

        /**
         * 获取
         * @return class_id
         */
        public String getClass_id() {
            return class_id;
        }

        /**
         * 设置
         * @param class_id
         */
        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        /**
         * 获取
         * @return class_title
         */
        public String getClass_title() {
            return class_title;
        }

        /**
         * 设置
         * @param class_title
         */
        public void setClass_title(String class_title) {
            this.class_title = class_title;
        }

        public String toString() {
            return "WordDataBean{name = " + name + ", symbol = " + symbol + ", sound = " + sound + ", desc = " + desc + ", course = " + course + ", class_id = " + class_id + ", class_title = " + class_title + "}";
        }
    }

    public WordBean() {
    }

    public WordBean(int code, String msg, ArrayList<WordDataBean> datas) {
        this.code = code;
        this.msg = msg;
        this.datas = datas;
    }

    /**
     * 获取
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取
     * @return datas
     */
    public ArrayList<WordDataBean> getDatas() {
        return datas;
    }

    /**
     * 设置
     * @param datas
     */
    public void setDatas(ArrayList<WordDataBean> datas) {
        this.datas = datas;
    }

    public String toString() {
        return "WordBean{code = " + code + ", msg = " + msg + ", datas = " + datas + "}";
    }



}
