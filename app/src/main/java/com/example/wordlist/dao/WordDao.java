package com.example.wordlist.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteColumn;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wordlist.entity.BookInfo;
import com.example.wordlist.entity.WordInfo;
import com.example.wordlist.tuple.WordNameMemTuple;
import com.example.wordlist.tuple.WordNameTransTuple;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM WordInfo")
    List<WordInfo> getAllWord();//所有单词

    @Query("SELECT `name` FROM WordInfo ORDER BY time_stamp")
    List<String> getWordNameList();

    @Query("SELECT `name` FROM WordInfo WHERE time_stamp > :time LIMIT 1")
    String getWordNameRandom(long time);

    @Query("SELECT MAX(time_stamp) FROM WordInfo")
    Long getMaxTime();

    @Query("SELECT MIN(time_stamp) FROM WordInfo")
    Long getMinTime();

    @Query("SELECT * FROM WordInfo ORDER BY time_stamp desc")
    List<WordInfo> getAllWordDescByTime();//所有单词按时间排序

    @Query("SELECT * FROM WordInfo WHERE name = :name")
    WordInfo getWordByName(String name);//用name查词


    @Query("SELECT `name`,`desc`,`symbol_uk`,`symbol_us` FROM WordInfo")
    List<WordNameTransTuple> getWordList();

    @Query("SELECT `name`,`memory` FROM WordInfo WHERE word_operation = 0")
    List<WordNameMemTuple> getAllOpWord();

    @Query("SELECT COUNT(*) FROM WordInfo")
    int countAll();
    @Query("SELECT COUNT(*) FROM WordInfo WHERE memory==0")
    int countMem0();
    @Query("SELECT COUNT(*) FROM WordInfo WHERE memory==1")
    int countMem1();
    @Query("SELECT COUNT(*) FROM WordInfo WHERE memory==2")
    int countMem2();
    @Query("SELECT COUNT(*) FROM WordInfo WHERE memory>=3 AND word_operation==0")
    int countMem3Op0();
    @Query("SELECT COUNT(*) FROM WordInfo WHERE memory>=3 AND word_operation==1")
    int countMem3Op1();


    @Insert(onConflict = OnConflictStrategy.REPLACE) // 记录重复时替换原记录
    void insertOneWord(WordInfo word); // 插入一条信息

    @Insert
    void insertWordList(List<WordInfo> wordList); // 插入多条信息

    @Update(onConflict = OnConflictStrategy.REPLACE)// 出现重复记录时替换原记录
    int updateWord(WordInfo word); // 更新信息

    @Delete
    void deleteWord(WordInfo word); // 删除信息



    @Query("DELETE FROM WordInfo WHERE 1=1") // 设置删除语句
    void deleteAllWord(); // 删除所有信息
}
