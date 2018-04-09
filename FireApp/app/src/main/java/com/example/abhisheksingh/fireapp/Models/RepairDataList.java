package com.example.abhisheksingh.fireapp.Models;

import com.example.abhisheksingh.fireapp.Models.RepairData;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class RepairDataList {
      List<RepairData> repairDataList;

    public List<RepairData> getRepairDataList() {
        return repairDataList;
    }

    public void setRepairDataList(List<RepairData> repairDataList) {
        this.repairDataList = repairDataList;
    }
}