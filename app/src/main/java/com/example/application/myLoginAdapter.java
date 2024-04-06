package com.example.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myLoginAdapter extends RecyclerView.Adapter<myLoginAdapter.MyViewHolder> {
        Context context;
        ArrayList<login_data> list;
        public myLoginAdapter(Context context,ArrayList<login_data> list){
            this.context=context;
            this.list=list;
        }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.manage_user,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("MyLoginAdapter", "Binding data at position: " + position);
        if (list != null && position < list.size()) {
            login_data loginData = list.get(position);
            if (loginData != null) {
                holder.username.setText(loginData.getUsername());
            } else {
                holder.username.setText("Unknown"); // Placeholder for null data
            }
        }
    }




    @Override
    public int getItemCount() {
        Log.d("MyLoginAdapter", "getItemCount called. List size: " + (list != null ? list.size() : 0));
        return list != null ? list.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.textView22);
        }
    }
}

