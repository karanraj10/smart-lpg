package com.example.lpgcontroller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;


public class DashboardFragment extends Fragment {

    View view;
    Context context;
    Intent intent;
    TextView currentWeightPercentageView, currentWeightView, initWeightView, leakageStatusView, joinDateView, joinTimeView;
    String formattedDate;

    DatabaseReference lpgDataRef;

    private DonutProgressView weightChart;
    private BarChart todayUsageBarChartView;
    PieChart pieChart;

    CardView controlView;

    ProgressAlertDialog progressAlertDialog;
    String lpgID;

    // To Get Context
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate Layout
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        currentWeightView = view.findViewById(R.id.currentWeight);
        currentWeightPercentageView = view.findViewById(R.id.currentWeightPercentage);
        initWeightView = view.findViewById(R.id.initWeight);
        leakageStatusView = view.findViewById(R.id.leakageStatus);
        joinDateView = view.findViewById(R.id.joinDate);
        joinTimeView = view.findViewById(R.id.joinTime);
        weightChart = view.findViewById(R.id.currentWeightChart);
        pieChart = view.findViewById(R.id.pieChart_view);

        weightChart.setCap(100f);

        todayUsageBarChartView = view.findViewById(R.id.todayUsageBarChart);
        controlView = view.findViewById(R.id.controlValveCardView);

        controlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ControlValveActivity.class);
                intent.putExtra("lpgID",lpgID);
                startActivity(intent);
            }
        });

        progressAlertDialog = new ProgressAlertDialog(getActivity());
        progressAlertDialog.startProgressAlertDialog();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);

        loadDataFromFirebase();

        return view;
    }

    private void loadDataFromFirebase(){
        String macAddress = SplashActivity.sharedpreferencesHandler.getUidFirebase();

        // Reference to particular User in database by it's MAC
        FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.users)).child(macAddress).child(context.getString(R.string.lpg)).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot s:snapshot.getChildren()) {

                    Log.d("tag",s.getKey());
                    lpgID = s.getKey();

                    float initWeight = Float.parseFloat(String.valueOf(s.child(context.getString(R.string.initWeight)).getValue()));
                    float currentWeight = Float.parseFloat(String.valueOf(s.child(context.getString(R.string.currentWeight)).getValue()));
                    String joinDate = String.valueOf(s.child(context.getString(R.string.joinDate)).getValue());
                    String joinTime = String.valueOf(s.child(context.getString(R.string.joinTime)).getValue());
                    boolean onOffStatus = Boolean.parseBoolean(String.valueOf(s.child(context.getString(R.string.onOffStatus)).getValue()));
                    boolean leakage = Boolean.parseBoolean(String.valueOf(s.child(context.getString(R.string.leakage)).getValue()));

                    int percentage = (int) ((currentWeight/initWeight)*100);

                    ArrayList<BarEntry> dataVals = new ArrayList<>();
                    ArrayList<PieEntry> dataVals1 = new ArrayList<>();
                    ArrayList<String> labelsX = new ArrayList<>();

                    int i=0;

                    DataSnapshot ds = (DataSnapshot) s.child(context.getString(R.string.usage)).child(formattedDate);
                    if(ds.getChildrenCount()>0){
                        for (DataSnapshot ss:ds.getChildren()) {
                            float f = Float.parseFloat(String.valueOf(ss.getValue()));
                            if(f>0){
                                dataVals.add(new BarEntry(i++,f));
                                dataVals1.add(new PieEntry(f,ss.getKey()));
                                labelsX.add(ss.getKey());
                            }
                        }
                    }

                    currentWeightView.setText(new StringBuilder().append(currentWeight).append(" Kg."));
                    currentWeightPercentageView.setText(new StringBuilder().append(percentage).append("  %"));
                    initWeightView.setText(new StringBuilder().append(initWeight).append(" Kg."));
                    joinDateView.setText(new StringBuilder().append(joinDate));
                    leakageStatusView.setText(new StringBuilder().append(leakage));
                    joinTimeView.setText(new StringBuilder().append(joinTime));

                    DonutSection section = new DonutSection("", ContextCompat.getColor(context, R.color.colorAccent), percentage);
                    weightChart.submitData(new ArrayList<>(Collections.singleton(section)));

                    BarDataSet barDataSet = new BarDataSet(dataVals,"");
                    barDataSet.setColor(context.getColor(R.color.colorAccent));
                    barDataSet.setValueTextColor(context.getColor(R.color.colorWhite));
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

                    XAxis xAxis = todayUsageBarChartView.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsX));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);

                    todayUsageBarChartView.animateY(1000);
                    todayUsageBarChartView.setData(barData);
                    todayUsageBarChartView.setNoDataTextColor(context.getColor(R.color.colorAccent));

                    todayUsageBarChartView.getAxisLeft().setTextColor(context.getColor(R.color.colorAccent));
                    todayUsageBarChartView.getXAxis().setTextColor(context.getColor(R.color.colorAccent));

                    todayUsageBarChartView.getAxisLeft().setAxisMinimum(0);
                    todayUsageBarChartView.getAxisRight().setEnabled(false);
                    todayUsageBarChartView.getAxisRight().setDrawGridLines(false);
                    todayUsageBarChartView.getAxisLeft().setDrawGridLines(false);
                    todayUsageBarChartView.getXAxis().setDrawGridLines(false);
                    todayUsageBarChartView.getDescription().setEnabled(false);
                    todayUsageBarChartView.setTouchEnabled(false);
                    todayUsageBarChartView.getLegend().setEnabled(false);

                    todayUsageBarChartView.invalidate();

                    ArrayList<Integer> colors = new ArrayList<>();
                    colors.add(Color.parseColor("#304567"));
                    colors.add(Color.parseColor("#309967"));
                    colors.add(Color.parseColor("#476567"));
                    colors.add(Color.parseColor("#890567"));
                    colors.add(Color.parseColor("#a35567"));
                    colors.add(Color.parseColor("#ff5f67"));
                    colors.add(Color.parseColor("#3ca567"));

                    PieDataSet pieDataSet = new PieDataSet(dataVals1,"");
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
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateY(1000);
                    pieChart.animateX(1000);
                    pieChart.setData(pieData);
                    pieChart.invalidate();
                }
                progressAlertDialog.stopProgressAlertDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressAlertDialog.stopProgressAlertDialog();
                Toast.makeText(context,context.getString(R.string.login_fetch_error),Toast.LENGTH_SHORT).show();
            }
        });
    }
}