package com.example.todoactivityvers2;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    public static void setListener(OnTaskClickListener listener) {
        TaskListAdapter.listener = listener;
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);

    }

    private static OnTaskClickListener listener;
    private static List<Task> tasks = new ArrayList<>();
    private TasksDB db;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task task = tasks.get(position);
        Log.d("TaskAdapter", "Binding task: " + task.title);

        holder.titleView.setText(task.title);
        holder.descView.setText(task.description);

        if (task.imageURI != null && !task.imageURI.isEmpty()) {
            Log.d("TaskAdapter", "Image URI: " + task.imageURI);
            holder.imageView.setImageURI(Uri.parse(task.imageURI));
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.doneCheckBox.setChecked(task.done);

        holder.doneCheckBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    task.done = isChecked;
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(() -> db.tasksDAO().updateTask(task));
                }
        );
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setTaskList(List<Task> tasks) {
        this.db = db;
        this.tasks = tasks;
        notifyDataSetChanged(); // Force UI update
        Log.d("ADAPTER_DEBUG", "Updated with " + tasks.size() + " tasks");
    }

    public void deleteTask(int position) {


        // Get the task safely
        final Task task = tasks.get(position);
        if (task == null) {
            Log.e("TaskListAdapter", "No task found at position: " + position);
            return;
        }


        // Delete from database in background thread
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> db.tasksDAO().deleteTask(task));
    }


    @Override
    public int getItemCount() {
        if(tasks == null) return 0;
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView descView;
        ImageView imageView;
        CheckBox doneCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.taskListTitle);
            descView = itemView.findViewById(R.id.taskListDesc);
            imageView = itemView.findViewById(R.id.taskListImage);
            doneCheckBox = itemView.findViewById(R.id.taskCheckBox);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(tasks.get(pos));
                }
            });
        }
    }
}