package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomTaskAdapter extends RecyclerView.Adapter<CustomTaskAdapter.CustomTaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private OnTaskLongClickListener onTaskLongClickListener;

    public CustomTaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }
    public List<Task> getTaskList() {
        return taskList;
    }
    public void updateTaskList(List<Task> newTaskList) {
        taskList.clear();
        taskList.addAll(newTaskList);
        notifyDataSetChanged();
    }

    public interface OnTaskLongClickListener {
        void onTaskLongClick(Task task);
    }

    public void setOnTaskLongClickListener(OnTaskLongClickListener listener) {
        this.onTaskLongClickListener = listener;
    }

    @NonNull
    @Override
    public CustomTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        return new CustomTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomTaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);
        holder.checkBoxTask.setChecked(currentTask.isCompleted());
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDescription.setText(currentTask.getDescription());
        holder.textViewTime.setText(currentTask.getTime());

        holder.itemView.setOnLongClickListener(v -> {
            if (onTaskLongClickListener != null) {
                onTaskLongClickListener.onTaskLongClick(currentTask);
                return true;
            }
            return false;
        });

        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTask.setCompleted(isChecked);
            if (context instanceof TaskListDisplay) {
                ((TaskListDisplay) context).saveCheckBoxState(currentTask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class CustomTaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTask;
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewTime;
        public CustomTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTime=itemView.findViewById(R.id.textViewTime);
        }
    }
}
