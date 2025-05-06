package com.example.todoactivityvers2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class ToDoListActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private AllTasksFragment allTasksFragment;
    private CompletedTaskFragment completedTasksFragment;
    private PendingTasksFragment pendingTasksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        // Initialize fragments
        allTasksFragment = new AllTasksFragment();
        completedTasksFragment = new CompletedTaskFragment();
        pendingTasksFragment = new PendingTasksFragment();

        // Set default fragment
        loadFragment(allTasksFragment);

        // Setup bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.page_all) {
                loadFragment(allTasksFragment);
                return true;
            } else if (id == R.id.page_done) {
                loadFragment(completedTasksFragment);
                return true;
            } else if (id == R.id.page_todo) {
                loadFragment(pendingTasksFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, fragment)
                .commit();
    }

    public void addTask(View view) {
        Intent taskIntent = new Intent(this, ToDo.class);
        startActivity(taskIntent);
    }
}
