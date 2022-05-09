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

    public LiveData<List<ToAddExhibits>> getToAddExhibits() {
        if (toAddExhibits == null) {
            loadExhibits();
        }
        return toAddExhibits;
    }
    public void loadExhibits() {
        toAddExhibits= toAddExhibitDao.getAllLive();
    }

    public void toggleSelected(ToAddExhibits toAddExhibits) {
        toAddExhibits.selected = !toAddExhibits.selected;
        toAddExhibitDao.update(toAddExhibits);
    }


}
//public class ToAddExhibitsViewModel extends AndroidViewModel {
//    private LiveData<List<ToAddExhibits>> toaddExhibits;
//    private final ToAddExhibitsDao toaddExhibitsDao;
//
//    public ToAddExhibitsViewModel(@NonNull Application application){
//        super(application);
//        Context context = getApplication().getApplicationContext();
//        ToAddDatabase db = ToAddDatabase.getSingleton(context);
//        toaddExhibitsDao = db.tooaddExhibitsDao();
//    }
//
//    public LiveData<List<ToAddExhibits>> getToaddExhibits(){
//        if (toaddExhibits == null){
//            loadUsers();
//        }
//        return toaddExhibits;
//    }
//
//    private  void loadUsers(){
//        //toaddExhibits = ToAddExhibitsDao.getAllLive();
//    }
//}
//
