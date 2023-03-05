package com.example.wordlist.util;

import com.example.wordlist.dao.BookDao;
import com.example.wordlist.entity.WordInfo;

import java.util.ArrayList;

/**
 * 用于暂时性的存储消息
 * 如为TranslateFragment和WordDetailFragment存储WordInfo
 * */
public class TempMsg {
    public static ArrayList<String>list=new ArrayList<>();
    public static WordInfo WordInfo=new WordInfo();
    public static WordInfo WordLearn=new WordInfo();
    public static String LastWordLearn="";
    private static int learnNum=5;//每天学几个
    private static int maxCount=5;//背几个换队列

    public static int getLearnNum() {
        return learnNum;
    }

    public static void setLearnNum(int learn) {
        learnNum = learn;
    }

    public static int getMaxCount() {
        return maxCount;
    }

    public static void setMaxCount(int max) {
        maxCount = max;
    }
}
