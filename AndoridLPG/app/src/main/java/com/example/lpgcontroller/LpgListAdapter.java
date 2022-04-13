package com.example.lpgcontroller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LpgListAdapter extends RecyclerView.Adapter<LpgListAdapter.LpgListHolder> {

    ArrayList<LPGData> arrayList;
    ArrayList<LPGData> arrayListTemp;
    Context context;

    public LpgListAdapter(Context context, ArrayList<LPGData> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListTemp = arrayList;
    }

    @NonNull
    @Override
    public LpgListAdapter.LpgListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.lpg_history_list_item,parent,false);
        return new LpgListAdapter.LpgListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LpgListAdapter.LpgListHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.id.setText(new StringBuffer("LPG  ").append(arrayList.get(position).getLpgID()));
        holder.joinDate.setText(arrayList.get(position).getJoinDate());
        holder.joinTime.setText(arrayList.get(position).getJoinTime());
        holder.capacity.setText(String.valueOf(arrayList.get(position).getInitWeight()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("lpgID",arrayList.get(position).getLpgID());

                UsagesHistoryFragment fragment2 = new UsagesHistoryFragment();
                fragment2.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).addToBackStack(null).commit();

            }
        });
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {
                    arrayList = arrayListTemp;

                } else {


                    ArrayList<LPGData> filteredList = new ArrayList<>();

                    for (LPGData lpgData : arrayList) {

                        if(lpgData.getJoinDate().toLowerCase().contains(charString)){
                            filteredList.add(lpgData);
                        }
                    }
                    arrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                arrayList = (ArrayList<LPGData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class LpgListHolder extends RecyclerView.ViewHolder
    {
        TextView id, joinDate, joinTime, capacity;

        public LpgListHolder(@NonNull View itemView) {
            super(itemView);

            id=itemView.findViewById(R.id.lpgID);
            joinDate=itemView.findViewById(R.id.lpgJoinDate);
            joinTime=itemView.findViewById(R.id.lpgJoinTime);
            capacity=itemView.findViewById(R.id.lpgCapacity);
        }
    }
}
