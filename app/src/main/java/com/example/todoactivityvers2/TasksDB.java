package com.example.todoactivityvers2;

import android.content.Context;
import android.view.View;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TasksDB extends RoomDatabase {

    public abstract TasksDAO tasksDAO();

    private static final String DB_NAME="tasks_database_name";
    private static TasksDB db;

    //Return an database instance.
    //If the database instance already exists it return the existing instance.
    //If not then it creates a new instance and returns that.
    public static TasksDB getInstance(View.OnClickListener context)
    {
        if (db == null) db =buildDatabaseInstance((Context) context);
        return db;
    }
    private static TasksDB buildDatabaseInstance(Context context)
    {
        return Room.databaseBuilder(context, TasksDB.class, DB_NAME).build();
    }
}
