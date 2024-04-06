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

public class MyRoomAdapter extends RecyclerView.Adapter<MyRoomAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<room_data>list;
    private ArrayList<room_data> originalList;
    private OnItemClickListener onItemClickListener;

    public MyRoomAdapter(Context context, ArrayList<room_data> list) {
        this.context = context;
        this.list = list;
        this.originalList = new ArrayList<>(list);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(room_data roomData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.floordesign,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        room_data roomName = list.get(position);
        holder.floorButton.setText(roomName.getRoomName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public ArrayList<room_data> getOriginalList() {
        return originalList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                ArrayList<room_data> filteredList = new ArrayList<>();

                for (room_data item : originalList) {
                    Log.d("Filter", "Original Floor Name: " + item.getRoomName());
                    if (item.getRoomName().toLowerCase().contains(searchText)) {
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
                    list.addAll((ArrayList<room_data>) filterResults.values);
                }

                notifyDataSetChanged();
                Log.d("Filter", "Publishing Results. List Size: " + list.size());
            }

        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView floorButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            floorButton=itemView.findViewById(R.id.button12);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(list.get(position));
                }
            });
        }
    }

}
