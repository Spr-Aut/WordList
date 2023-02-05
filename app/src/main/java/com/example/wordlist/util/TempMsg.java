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
    public static int x=0;
}
