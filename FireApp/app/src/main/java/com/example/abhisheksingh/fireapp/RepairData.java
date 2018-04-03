package com.example.abhisheksingh.fireapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by abhisheksingh on 03/04/18.
 */

@IgnoreExtraProperties
public class RepairData {

    public String hallId;
    public int unRepairedDoors;
    public int unRepairedTables;
    public int unRepairedChairs;


    public RepairData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RepairData(String hallId, int unRepairedChairs,int unRepairedTables,int unRepairedDoors) {
       this.hallId = hallId;
       this.unRepairedChairs = unRepairedChairs;
       this.unRepairedDoors = unRepairedDoors;
       this.unRepairedTables = unRepairedTables;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public int getUnRepairedDoors() {
        return unRepairedDoors;
    }

    public void setUnRepairedDoors(int unRepairedDoors) {
        this.unRepairedDoors = unRepairedDoors;
    }

    public int getUnRepairedTables() {
        return unRepairedTables;
    }

    public void setUnRepairedTables(int unRepairedTables) {
        this.unRepairedTables = unRepairedTables;
    }

    public int getUnRepairedChairs() {
        return unRepairedChairs;
    }

    public void setUnRepairedChairs(int unRepairedChairs) {
        this.unRepairedChairs = unRepairedChairs;
    }
}