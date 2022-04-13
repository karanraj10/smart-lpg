package com.example.lpgcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.DailyListHolder> {

    private Intent intent;
    private final Context context;
    private List<MyData> dailyList;
    private final List<MyData> dailyListTemp;

    public DailyListAdapter(Context context, List<MyData> dailyList)
    {
        this.context = context;
        this.dailyList = dailyList;
        this.dailyListTemp = dailyList;
    }

    @NonNull
    @Override
    public DailyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.daily_usage_list_item,parent,false);
        return new DailyListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyListAdapter.DailyListHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setIsRecyclable(false);

        holder.date.setText(dailyList.get(position).getDate());

        Map<String, Float> valueMap = dailyList.get(position).getTimeSlots();

        float total=0;

        for(Map.Entry<String,Float> timeSlotSet: valueMap.entrySet()){

            float f = Float.parseFloat(String.valueOf(timeSlotSet.getValue()));
            total += f;

            TextView tv = new TextView(context);

            SpannableStringBuilder builder = new SpannableStringBuilder();

            SpannableString str1= new SpannableString(timeSlotSet.getKey()+" : ");
            str1.setSpan(new ForegroundColorSpan(context.getColor(R.color.colorWhite)), 0, str1.length(), 0);
            builder.append(str1);

            SpannableString str2= new SpannableString(timeSlotSet.getValue()+" Kg.");
            str2.setSpan(new ForegroundColorSpan(context.getColor(R.color.colorAccent)), 0, str2.length(), 0);
            builder.append(str2);

            tv.setText( builder, TextView.BufferType.SPANNABLE);
            tv.setTextAppearance(R.style.normalText16);
            holder.layout.addView(tv);
        }

        holder.totalUsage.setText(new StringBuffer().append(String.format("%.2f", total)).append(" Kg."));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Float> valueMap = dailyList.get(position).getTimeSlots();

                Intent intent = new Intent(context,UsageDetailsActivity.class);
                intent.putExtra("usagesDetailsMap",valueMap);
                context.startActivity(intent);
            }
        });

    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {
                    dailyList = dailyListTemp;

                } else {

                List<MyData> filteredList = new ArrayList<>();

                for (MyData data: dailyList) {
                    if(data.getDate().toLowerCase().contains(charString)){
                        filteredList.add(data);
                    }
                }

//                    for (HashMap<String, HashMap<String, Float>> usageEntrySet : dailyList) {
//                        for (String key: usageEntrySet.keySet()) {
//                            if(key.toLowerCase().contains(charString)){
//                                filteredList.add(usageEntrySet);
//                            }
//                        }
//                    }
                    dailyList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dailyList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                dailyList = (List<MyData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class DailyListHolder extends RecyclerView.ViewHolder
    {
        TextView date, totalUsage;
        LinearLayout layout;

        public DailyListHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            layout = itemView.findViewById(R.id.usageTimeSlotLayout);
            totalUsage = itemView.findViewById(R.id.totalUsage);
        }
    }
}
