package com.example.wordlist.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wordlist.entity.BookInfo;

import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM BookInfo")
    List<BookInfo> getAllBook();

    @Query("SELECT * FROM BookInfo WHERE name = :name")
    BookInfo getBookByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 记录重复时替换原记录
    void insertOneBook(BookInfo book); // 插入一条书籍信息

    @Insert
    void insertBookList(List<BookInfo> bookList); // 插入多条书籍信息

    @Update(onConflict = OnConflictStrategy.REPLACE)// 出现重复记录时替换原记录
    int updateBook(BookInfo book); // 更新书籍信息

    @Delete
    void deleteBook(BookInfo book); // 删除书籍信息



    @Query("DELETE FROM BookInfo WHERE 1=1") // 设置删除语句
    void deleteAllBook(); // 删除所有书籍信息
}
