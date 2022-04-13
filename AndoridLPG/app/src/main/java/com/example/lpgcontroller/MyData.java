package com.example.lpgcontroller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class MyData implements Comparable<MyData> {
    String date;
    HashMap<String,Float> timeSlots;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Float> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(HashMap<String, Float> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public int compareTo(MyData myData) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd:MM:yyyy");
        return LocalDateTime.parse(this.getDate(),f).compareTo(LocalDateTime.parse(myData.getDate(),f));
    }
}
