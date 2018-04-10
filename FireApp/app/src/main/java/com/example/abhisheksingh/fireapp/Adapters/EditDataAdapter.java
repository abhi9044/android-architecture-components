package com.example.abhisheksingh.fireapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.abhisheksingh.fireapp.Models.RepairData;
import com.example.abhisheksingh.fireapp.R;

import java.util.ArrayList;
import java.util.List;

public class EditDataAdapter extends RecyclerView.Adapter<EditDataAdapter.ViewHolder> {

    private List<RepairData> objects = new ArrayList<RepairData>();

    private Context context;
    private LayoutInflater layoutInflater;
    EditData editData;

    public EditDataAdapter(Context context, List<RepairData> objects,EditData editData) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.editData = editData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_data_recycler_view, parent, false);

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
        holder.edtHallId.setText(data.hallId);
        // holder.edtUnRepairedData.setText(context.getString(R.string.next_line,data.workCat1,data.workCat2,data.workCat3));
        holder.edtUnRepairedData.setText(data.workCat1 + System.getProperty("line.separator") + data.workCat2 + System.getProperty("line.separator") + data.workCat3);
        holder.edtUnRepairedDoors.setText(data.workCat4);
        if (data.isPending){
            holder.btnEdit.setBackground(context.getResources().getDrawable(R.drawable.button_bg_red));
            holder.btnEdit.setText("Mark Completed");
        } else {
            holder.btnEdit.setBackground(context.getResources().getDrawable(R.drawable.button_bg_green));
            holder.btnEdit.setText("Completed");
        }

        //TODO implement
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView edtHallId;
        private TextView edtUnRepairedData;
        private TextView edtUnRepairedDoors;
        private TextView edtUnrepairedTables;
        private Button btnEdit;
        private Button btnDelete;
        private View lnrBg;

        public ViewHolder(View view) {
            super(view);
            edtHallId = view.findViewById(R.id.edt_hall_id);
            edtUnRepairedData = view.findViewById(R.id.edt_seats);
            edtUnRepairedDoors = view.findViewById(R.id.edt_doors);
            edtUnrepairedTables =  view.findViewById(R.id.edt_tables);
            btnEdit = view.findViewById(R.id.btn_edit);
            lnrBg = view.findViewById(R.id.lnr_bg);
            btnDelete = view.findViewById(R.id.btn_delete);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btnEdit.getText().toString().equalsIgnoreCase("MARK COMPLETED"))
                    {
                        RepairData data = objects.get(getAdapterPosition());
                        data.isPending = false;
                        editData.editUserData(data);
                        btnEdit.setBackground(context.getResources().getDrawable(R.drawable.button_bg_green));
                        btnEdit.setText("Completed");
                    }

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnEdit.setText("Edit");
                    btnDelete.setVisibility(View.GONE);
                    edtUnRepairedData.setEnabled(false);
                    edtUnRepairedDoors.setEnabled(false);
                    edtUnrepairedTables.setEnabled(false);
                    lnrBg.setBackgroundColor(Color.WHITE);
                    lnrBg.setPadding(0,0,0,0);
                    editData.deleteUserData(getAdapterPosition());
                }
            });

        }
    }
    public interface EditData
    {
        void editUserData(RepairData data);
        void deleteUserData(int pos);
    }
}
