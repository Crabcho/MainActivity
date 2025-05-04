package com.example.todoactivityvers2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TasksDAO {


    //add a task to the database
    @Insert
    void insert(Task task);

    //Observe for changes to the tasks in the database
    @Query("SELECT * FROM task")
    LiveData<List<Task>> observeAll();

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}

