package com.example.todoactivityvers2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @ColumnInfo(name = "done")
    public boolean done;

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name="duedate")
    public String duedate;

    @ColumnInfo(name = "image")
    public String imageURI;

    public Task(){}

}