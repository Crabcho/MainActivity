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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;


public class ToDoListActivity extends AppCompatActivity {

    TasksDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = TasksDB.getInstance(this);

        LinearLayout linearLayout = findViewById(R.id.taskListRecyclerView);
        //Observe for changes in the list of all tasks in the database
        LiveData<List<Task>> tasks = db.tasksDAO().observeAll();

        //Handle any changes in the observer
        tasks.observe (this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("ToDoApp", "The task list has changed");

            //Wipe the layout so we can redraw the updated tasks.
            linearLayout.removeAllViews();
            //Loop through the list of tasks
            for (Task task : tasks) {
                //Create a simple TextView using the task title
                /*TextView textView = new TextView(getApplicationContext());
                textView.setText(task.title);
                //Add the TextView to the LinearLayout
                linearLayout.addView(textView);*/
                View listView =
                        getLayoutInflater().inflate(R.layout.task_layout, linearLayout, false);

                TextView titleView = listView.findViewById(R.id.taskListTitle);
                TextView descView = listView.findViewById(R.id.taskListDesc);
                ImageView imageView = listView.findViewById(R.id.taskListImage);

                titleView.setText(task.title);
                descView.setText(task.description);
                imageView.setImageURI(Uri.parse(task.imageURI));

                linearLayout.addView(listView);
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
