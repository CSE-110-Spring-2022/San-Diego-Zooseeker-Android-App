package com.example.zooapp44;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities={ToAddExhibits.class},version=1)
@TypeConverters({Converter.class})
public abstract class ToAddDatabase extends RoomDatabase {
    public abstract ToAddExhibitDao toAddExhibitDao();
}