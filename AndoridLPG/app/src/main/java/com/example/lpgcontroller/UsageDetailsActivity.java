package com.example.lpgcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsageDetailsActivity extends AppCompatActivity {

    BarChart barChart;
    PieChart pieChart;
    ArrayList<BarEntry> valuesList = new ArrayList<>();
    ArrayList<PieEntry> valuesList1 = new ArrayList<>();
    ArrayList<String> keysList = new ArrayList<>();
    Intent intent;
    HashMap<String,Float> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_details);

        barChart = findViewById(R.id.usagedDetailsBarChart);
        pieChart = findViewById(R.id.pieChart_view);

        intent = getIntent();
        hashMap = (HashMap<String, Float>) intent.getSerializableExtra("usagesDetailsMap");

        int i=0;

        //valueList = BarChart, keyList = BarChart Labels, valueList1 = PieChart
        for(Map.Entry<String,Float> timeSlotSet: hashMap.entrySet()){
            float f = Float.parseFloat(String.valueOf(timeSlotSet.getValue()));
            if(f>0){
                valuesList.add(new BarEntry(i++,f));
                valuesList1.add(new PieEntry(f,timeSlotSet.getKey()));
                keysList.add(timeSlotSet.getKey());
            }
        }


        BarDataSet barDataSet = new BarDataSet(valuesList,"");
        barDataSet.setColor(this.getColor(R.color.colorAccent));
        barDataSet.setValueTextColor(this.getColor(R.color.colorWhite));
        barDataSet.setValueTextSize(12);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat df = new DecimalFormat("#.#");
                return df.format(value);
            }
        });

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(keysList));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);    //prevent overlapping label when zoomed
        xAxis.setGranularityEnabled(true);


        barChart.animateY(1000);
        barChart.setData(barData);
        barChart.setNoDataTextColor(this.getColor(R.color.colorAccent));

        barChart.getAxisLeft().setTextColor(this.getColor(R.color.colorAccent));
        barChart.getXAxis().setTextColor(this.getColor(R.color.colorAccent));

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.invalidate();

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        PieDataSet pieDataSet = new PieDataSet(valuesList1,"");
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat df = new DecimalFormat("#.#");
                return df.format(value);
            }
        });

        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.animateX(1000);
        pieChart.getDescription().setEnabled(false);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}