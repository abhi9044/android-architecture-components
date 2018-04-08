package com.example.abhisheksingh.fireapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class RepairDataAdapter extends RecyclerView.Adapter<RepairDataAdapter.ViewHolder> {

    private List<RepairData> objects = new ArrayList<RepairData>();

    private Context context;
    private LayoutInflater layoutInflater;

    public RepairDataAdapter(Context context,List<RepairData> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repair_data_recycler_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       RepairData repairData = objects.get(position);
       initializeViews(repairData,holder);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return  objects.size();
    }

    public void setData(List<RepairData> objects)
    {
        this.objects = objects;
        notifyDataSetChanged();
    }

    private void initializeViews(RepairData data, ViewHolder holder) {
        holder.tvHallId.setText(data.hallId);

        //TODO implement
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHallId;
        private TextView tvUnRepairedChairs;
        private TextView tvUnRepairedDoors;
        private TextView tvUnrepairedTables;

        public ViewHolder(View view) {
            super(view);
            tvHallId = (TextView) view.findViewById(R.id.tv_hall_id);
            tvUnRepairedChairs = (TextView) view.findViewById(R.id.tv_seats);
            tvUnRepairedDoors = (TextView) view.findViewById(R.id.tv_doors);
            tvUnrepairedTables = (TextView) view.findViewById(R.id.tv_tables);
        }
    }
}
