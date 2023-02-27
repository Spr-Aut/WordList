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
import com.example.wordlist.tuple.WordNameTransTuple;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM WordInfo")
    List<WordInfo> getAllWord();//所有单词

    @Query("SELECT * FROM WordInfo ORDER BY time_stamp desc")
    List<WordInfo> getAllWordDescByTime();//所有单词按时间排序

    @Query("SELECT * FROM WordInfo WHERE name = :name")
    WordInfo getWordByName(String name);//用name查词

    @Query("SELECT * FROM WordInfo WHERE word_operation = 0")
    List<WordInfo> getAllOpWord();//所有op==0的词

    @Query("SELECT `name`,`desc` FROM WordInfo")
    List<WordNameTransTuple> getWordList();



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
