package com.r.appinesstest.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.r.appinesstest.R;
import com.r.appinesstest.databinding.ItemTaskBinding;
import com.r.appinesstest.repo.model.ResponseModel;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.TaskViewHolder> {
    private List<ResponseModel> taskList;

    public RecylerViewAdapter(List<ResponseModel> allTaskList) {
        this.taskList = allTaskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_task, parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addAll(List<ResponseModel> list){
        taskList.clear();
        taskList.addAll(list);
        Collections.sort(taskList);
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private ItemTaskBinding binding;
        TaskViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ResponseModel task) {
            binding.textViewTitle.setText(task.getTitle());
            binding.textViewNoOfBackers.setText(task.getNumBackers());
            binding.textViewBy.setText(task.getBy());
        }
    }
}
