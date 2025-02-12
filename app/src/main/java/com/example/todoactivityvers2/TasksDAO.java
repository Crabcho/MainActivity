package com.example.todoactivityvers2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TasksDAO {


    //add a task to the database
    @Insert
    void insert(Task task);

    //Retrieve a list of all tasks from the database
    //@Query("SELECT * FROM task")
    //Single<List<Task>> getAll();

    //Observe for chnages to the tasks in the database
    @Query("SELECT * FROM task")
    LiveData<List<Task>> observeAll();
}

