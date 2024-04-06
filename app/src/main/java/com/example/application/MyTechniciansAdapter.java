package com.example.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyTechniciansAdapter extends RecyclerView.Adapter<MyTechniciansAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<technicians_data> list;
    private ArrayList<technicians_data> originalList;
    private OnItemClickListener onItemClickListener;

    public MyTechniciansAdapter(Context context, ArrayList<technicians_data> list) {
        this.context = context;
        this.list = list;
        this.originalList = new ArrayList<>(list);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(technicians_data techniciansData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.technicians, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        technicians_data techniciansData = list.get(position);
        holder.name.setText("Name : " + techniciansData.getName());
        holder.empId.setText("Employee ID: " + techniciansData.getEmpId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<technicians_data> getOriginalList() {
        return originalList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d("Filter", "Performing filtering...");
                String searchText = charSequence.toString().toLowerCase();
                ArrayList<technicians_data> filteredList = new ArrayList<>();

                for (technicians_data item : originalList) {
                    Log.d("Filter", "Original Floor Name: " + item.getName());
                    if (item.getName().toLowerCase().contains(searchText) || String.valueOf(item.getEmpId()).contains(searchText)) {
                        filteredList.add(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                Log.d("Filter", "Search Text: " + searchText);
                Log.d("Filter", "Filtered List Size: " + filteredList.size());
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (charSequence.length() == 0) {
                    // If the search query is empty, restore the original list
                    list.clear();
                    list.addAll(originalList);
                } else {
                    // If there is a search query, update the list with filtered results
                    list.clear();
                    list.addAll((ArrayList<technicians_data>) filterResults.values);
                }

                notifyDataSetChanged();
                Log.d("Filter", "Publishing Results. List Size: " + list.size());
            }

        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, empId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView22);
            empId=itemView.findViewById(R.id.textView23);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(position));
                }
            });
            };
        }
    }

