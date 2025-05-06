package com.example.todoactivityvers2;

import android.content.Context;
import android.view.View;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TasksDB extends RoomDatabase {

    public abstract TasksDAO tasksDAO();

    private static TasksDB db;

    public static TasksDB getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(),
                            TasksDB.class, "Tasks_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db;
    }
}


