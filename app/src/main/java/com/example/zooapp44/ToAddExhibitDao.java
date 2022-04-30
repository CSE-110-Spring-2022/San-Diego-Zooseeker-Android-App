package com.example.zooapp44;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToAddExhibitDao {
    @Insert
    long insert(ToAddExhibits toAddExhibit);

    @Query("SELECT * FROM  `exhibit_items` WHERE `id`=:id")
    ToAddExhibits get(long id);

    @Query("SELECT * FROM `exhibit_items` ORDER BY `itemType`")
    List<ToAddExhibits> getAll();

    @Update
    int update(ToAddExhibits toAddExhibit);

    @Delete
    int delete(ToAddExhibits toAddExhibit);
}
