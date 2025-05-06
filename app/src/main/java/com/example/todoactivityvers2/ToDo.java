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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ToDo extends AppCompatActivity {


    Uri imageURI;
    ActivityResultLauncher<Intent> launchCameraActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                activityResult -> {
                    if(activityResult.getResultCode() == RESULT_OK) {
                        ImageView taskImage = findViewById(R.id.taskImageView);
                        taskImage.setImageURI(imageURI);
                        Log.d("ToDoApp", "picture store in: " + imageURI);
                        if (imageURI != null) {
                            taskImage.setImageURI(imageURI);
                        }
                    }

                });
        Button saveButton = findViewById(R.id.saveTaskButton);

        saveButton.setOnClickListener(view -> {
            Log.d("ToDoActivity", "onSaveClick");
            EditText titleView = findViewById(R.id.taskTitleView);
            EditText descView = findViewById(R.id.taskDescriptionView);
            EditText dateView = findViewById(R.id.taskDueDateView);
            EditText timeView = findViewById(R.id.taskDueTimeView);


            String title = titleView.getText().toString();
            String desc = descView.getText().toString();
            String date = dateView.getText().toString();
            String time = timeView.getText().toString();

            //create a new Task
            final Task task = new Task();
            task.title = title;
            task.description = desc;
            task.duedate = date + " " + time;

            task.imageURI = (imageURI != null) ? imageURI.toString() : null;
            task.done = false;

            TasksDB db = TasksDB.getDatabase(this);



            //We have to create a separate thread to insert data into the database
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                db.tasksDAO().insert(task);

                // Add this debug code:
                List<Task> allTasks = db.tasksDAO().getAll();
                Log.d("DB_VERIFY", "Tasks in DB: " + allTasks.size());
                for (Task t : allTasks) {
                    Log.d("DB_VERIFY", "Task: " + t.title + " (ID: " + t.uid + ")");
                    finish();
                }
            });

        });
    }

    public void onDateClick(View view) {
        Log.d("ToDoActivity", "onDateClick");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) ->
                        ((EditText) findViewById(R.id.taskDueDateView)).setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    public void onTimeClick(View view) {
        Log.d("ToDoActivity", "onTimeClick");

        TimePickerDialog.OnTimeSetListener listener = (view1, hourOfDay, minute) -> {
            EditText timeView = findViewById(R.id.taskDueTimeView);

            timeView.setText(hourOfDay + ":" + minute);
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
    public void openMap(View view) {
        Intent intent = new Intent(this, MyLocation.class);
        startActivity(intent);
    }

}




