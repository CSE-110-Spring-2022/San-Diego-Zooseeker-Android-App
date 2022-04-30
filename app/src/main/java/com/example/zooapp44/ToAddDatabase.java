package com.example.zooapp44;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={ToAddExhibits.class},version=1)
public abstract class ToAddDatabase extends RoomDatabase {
    public abstract ToAddExhibitDao toAddExhibitDao();
}
