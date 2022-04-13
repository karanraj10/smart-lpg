package com.example.lpgcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LPGData {

    String lpgID;
    float currentWeight;
    float initWeight;
    String joinDate;
    String joinTime;
    boolean leakage;

    public LPGData() {
    }

    public String getLpgID() {
        return lpgID;
    }

    public void setLpgID(String lpgID) {
        this.lpgID = lpgID;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public float getInitWeight() {
        return initWeight;
    }

    public void setInitWeight(float initWeight) {
        this.initWeight = initWeight;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public boolean getLeakage() {
        return leakage;
    }

    public void setLeakage(boolean leakage) {
        this.leakage = leakage;
    }

    public String getJoinTime() {
        return joinTime;
    }
    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }
}
