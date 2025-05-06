package com.example.todoactivityvers2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.Executors;

public class AllTasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskListAdapter adapter;

    public AllTasksFragment() {
        super(R.layout.fragment_all_tasks);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.taskListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TaskListAdapter();
        recyclerView.setAdapter(adapter);

        loadRemindersSafely();
    }

    private void loadRemindersSafely() {
        TasksDB db = TasksDB.getDatabase(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Task> reminders = db.tasksDAO().getAll();

            // Ensure UI update only if fragment is attached
            new Handler(Looper.getMainLooper()).post(() -> {
                if (isAdded()) {
                    adapter.setTaskList(reminders);
                }
            });
        });
    }
}