package com.example.todoactivityvers2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<Task> tasks;
    private TasksDB db;


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
        holder.imageView.setImageURI(Uri.parse(task.imageURI));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.taskListTitle);
            descView = itemView.findViewById(R.id.taskListDesc);
            imageView = itemView.findViewById(R.id.taskListImage);
        }
    }
}




