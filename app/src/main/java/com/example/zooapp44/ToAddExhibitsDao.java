package com.example.zooapp44;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToAddExhibitsDao{
    @Insert
    long insert(ToAddExhibits toaddExhibits);

    @Insert
    List<Long> insertAll(List<ToAddExhibits> toaddExhibits);

    @Query("SELECT * FROM `toadd_exhibits` WHERE `id`=:id")
    ToAddExhibits get(String id);

    @Update
    int update(ToAddExhibits todoListItem);

    @Delete
    int delete(ToAddExhibits todoListItem);
}