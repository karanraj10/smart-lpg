package com.example.lpgcontroller;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MapComparator implements Comparator<MyData> {
    DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Override
    public int compare(MyData myData, MyData t1) {
        Log.d("hello",myData.getDate()+"   "+t1.getDate());
        return LocalDate.parse(myData.getDate(),f).compareTo(LocalDate.parse(t1.getDate(),f));
    }
}
