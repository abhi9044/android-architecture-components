package com.example.abhisheksingh.fireapp.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;

/**
 * Created by abhisheksingh on 03/04/18.
 */

@IgnoreExtraProperties
public class RepairData {

    public String hallId;
    public String workCat1 = "";
    public String workCat2 = "";
    public String workCat3 = "";
    public String issueDescription = "";
    public String imageUrl;
    public Boolean isPending = true;


    public String getWorkCat1() {
        return workCat1;
    }

    public void setWorkCat1(String workCat1) {
        this.workCat1 = workCat1;
    }

    public String getWorkCat2() {
        return workCat2;
    }

    public void setWorkCat2(String workCat2) {
        this.workCat2 = workCat2;
    }

    public String getWorkCat3() {
        return workCat3;
    }

    public void setWorkCat3(String workCat3) {
        this.workCat3 = workCat3;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public RepairData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public RepairData(String hallId, int unRepairedChairs,int unRepairedTables,int unRepairedDoors) {
       this.hallId = hallId;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }
}