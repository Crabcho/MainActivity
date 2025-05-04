package com.example.todoactivityvers2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;


public class ToDoListActivity extends AppCompatActivity {

    TasksDB db;
    TaskListAdapter taskListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do_list);
        db = TasksDB.getInstance(this);
        RecyclerView recyclerView = findViewById(R.id.taskListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskListAdapter(this, db);
        recyclerView.setAdapter(taskListAdapter);
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                taskListAdapter.deleteTask(position);
            }
        };

        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });




        //Observe for changes in the list of all tasks in the database
        LiveData<List<Task>> tasks = db.tasksDAO().observeAll();

        //Handle any changes in the observer
        tasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                // Add null check
                if (taskListAdapter != null) {
                    taskListAdapter.setTaskList(db, tasks);
                } else {
                    Log.e("ToDoListActivity", "Adapter is null!");
                }
            }
        });
    }
    @Override
    protected void onResume(){

        super.onResume();
        Log.d("ToDoApp","onResume");
    }

    public void addTask(View view){
        Log.d("ToDoApp", "addTask");

        //create explicit intent for ToDo
        Intent taskIntent = new Intent(this, ToDo.class);
        startActivity(taskIntent);

    }
}
