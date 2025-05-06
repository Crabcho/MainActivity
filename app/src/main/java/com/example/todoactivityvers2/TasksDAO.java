package com.example.todoactivityvers2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
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

    //Observe for changes to the completed tasks
    @Query("SELECT * FROM task WHERE done = '1'")
    LiveData<List<Task>> observeDone();
    //Observe for changes to the pending tasks
    @Transaction
    @Query("SELECT * FROM task WHERE done = 0")
    LiveData<List<Task>> observerPending();
    @Query("SELECT * FROM task")
    List<Task> getAll();


    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}

