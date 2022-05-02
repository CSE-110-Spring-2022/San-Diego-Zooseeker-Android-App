package com.example.zooapp44;



import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities={ToAddExhibits.class},version=1)
@TypeConverters({Converter.class})
public abstract class ToAddDatabase extends RoomDatabase {
    private static ToAddDatabase singleton = null;

    public abstract ToAddExhibitDao toAddExhibitDao();
    public synchronized static ToAddDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = ToAddDatabase.makeDatabase(context);
        }
        return singleton;
    }
    private static ToAddDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, ToAddDatabase.class, "todo_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<ToAddExhibits> todos = ToAddExhibits
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).toAddExhibitDao().insertAll(todos);
                        });


                    }
                })
                .build();

    }
    @VisibleForTesting
    public static void injectTestDatabase(ToAddDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
