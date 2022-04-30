package com.example.zooapp44;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ToAddExhibitsViewModel extends AndroidViewModel {
    private LiveData<List<ToAddExhibits>> toAddExhibits;
    private final ToAddExhibitDao toAddExhibitDao;

    public ToAddExhibitsViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ToAddDatabase db = ToAddDatabase.getSingleton(context);
        toAddExhibitDao = db.toAddExhibitDao();
    }

    public LiveData<List<ToAddExhibits>> getTodoListItems() {
        if (toAddExhibits == null) {
            loadUsers();
        }
        return toAddExhibits;
    }
    public void loadUsers() {
        toAddExhibits= toAddExhibitDao.getAllLive();
    }

    public void toggleSelected(ToAddExhibits toAddExhibits) {
        toAddExhibits.selected = !toAddExhibits.selected;
        toAddExhibitDao.update(toAddExhibits);
    }


}
