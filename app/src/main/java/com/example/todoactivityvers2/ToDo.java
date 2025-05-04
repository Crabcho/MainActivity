package com.example.todoactivityvers2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.SharedPreferences;
import android.content.Context;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ToDo extends AppCompatActivity {
    Uri imageURI;
    ActivityResultLauncher<Intent> launchCameraActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });

        launchCameraActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {
                        if(activityResult.getResultCode() == RESULT_OK) {
                            ImageView taskImage = findViewById(R.id.taskImageView);
                            taskImage.setImageURI(imageURI);
                            Log.d("ToDoApp", "picture store in: " + imageURI);
                            if (imageURI != null) {
                                taskImage.setImageURI(imageURI);
                            }
                        }

                    }
                });
        Button saveButton = findViewById(R.id.saveTaskButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ToDoActivity", "onSaveClick");
                EditText titleView = findViewById(R.id.taskTitleView);
                EditText descView = findViewById(R.id.taskDescriptionView);
                EditText dateView = findViewById(R.id.taskDueDateView);
                EditText timeView = findViewById(R.id.taskDueTimeView);


                String title = titleView.getText().toString();
                String desc = descView.getText().toString();
                String date = dateView.getText().toString();
                String time = timeView.getText().toString();



                //create an instance of the Database
                TasksDB db = TasksDB.getInstance(view.getContext());

                //create a new Task
                final Task task1 = new Task();
                task1.title = title;
                task1.description = desc;
                task1.duedate = date + " " + time;

                task1.imageURI = (imageURI != null) ? imageURI.toString() : null;
                task1.done = false;

                //this won't work
                //db.tasksDAO().insert(task1);

                //We have to create a separate thread to insert data into the database
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //add the task to the database
                        db.tasksDAO().insert(task1);
                    }
                });
                finish();



                /*
                //retrieve all the tasks stored in the database
                LiveData<List<Task>> tasks = db.tasksDAO().observeAll();

                //We need to observe the LiveData object and wait for it to change
                tasks.observe(this,  new Observer<List<Task>>(){
                    @Override
                    public void onChanged(List<Task> tasks) {

                        //Loop over the list and log the title and description to the console
                        for (Task task: tasks) {
                            Log.d("ToDoAPP", task.title + " : " + task.description);
                        }
                    }
                });



                 */




////                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
////                SharedPreferences.Editor editor = sharedPref.edit();
////                editor.putString("title", title);
////                editor.putString("desc",desc);
////                editor.putString("date",date);
////                editor.putString("time",time);
////                editor.commit();
//
//                String storedTitle = sharedPref.getString("title", "None");
            }
        });
    }

    public void onClick(View view) {
        Log.d("ToDoActivity", "onDateClick");

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText dateView = findViewById(R.id.taskDueDateView);

                dateView.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(this, listener, 2020, 1, 1);
        dialog.show();
    }

    public void onTimeClick(View view) {
        Log.d("ToDoActivity", "onTimeClick");

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                EditText timeView = findViewById(R.id.taskDueTimeView);

                timeView.setText(hourOfDay + ":" + minute);
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(this, listener, 00, 00, true);
        dialog.show();


    }
    public void onCameraClick (View view) {

        Log.d("ToDoApp", "onCameraClick");

        String filename = "JPEG_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(getFilesDir(), filename);

        imageURI = FileProvider.getUriForFile(this, "com.example.todoactivityvers2.fileprovider", imageFile);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);

        launchCameraActivity.launch(takePictureIntent);
    }

}




