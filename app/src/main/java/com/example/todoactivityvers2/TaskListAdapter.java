package com.example.todoactivityvers2;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<Task> tasks;
    private TasksDB db;

    public TaskListAdapter(ToDoListActivity toDoListActivity, TasksDB db) {
        this.db = db;
        this.tasks = new ArrayList<>();
    }


    public void setTaskList(TasksDB db, List<Task> tasks) {
        this.db = db;
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        final Task task = tasks.get(position);
        holder.titleView.setText(task.title);
        holder.descView.setText(task.description);
        if (task.imageURI != null && !task.imageURI.isEmpty()) {
            holder.imageView.setImageURI(Uri.parse(task.imageURI));
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        holder.doneCheckBox.setChecked(task.done);

        holder.doneCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        task.done = isChecked;
                        Executor myExecutor = Executors.newSingleThreadExecutor();
                        myExecutor.execute(new Runnable(){
                            @Override
                            public void run(){
                                db.tasksDAO().updateTask(task);
                            }
                        });
                    }
                }
        );
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
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                    db.tasksDAO().deleteTask(task);
            }
        });
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
        }
    }
}