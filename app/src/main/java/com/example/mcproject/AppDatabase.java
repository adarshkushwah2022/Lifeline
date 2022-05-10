package com.example.mcproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {notficationEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public static AppDatabase myDatabaseObj;

    public static AppDatabase getDatabaseInstance(Context context){
        if(myDatabaseObj == null){
            myDatabaseObj = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"myCustomDatabaseFinal").
                            fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return myDatabaseObj;
    }
}
