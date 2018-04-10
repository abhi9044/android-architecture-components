package com.example.abhisheksingh.fireapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhisheksingh.fireapp.Models.ImportantIssue;
import com.example.abhisheksingh.fireapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImportantIssueAdapter extends RecyclerView.Adapter<ImportantIssueAdapter.ViewHolder> {

    private List<ImportantIssue> objects = new ArrayList<ImportantIssue>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ImportantIssueAdapter(Context context, List<ImportantIssue> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imp_issue_recycler_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImportantIssue importantIssue = objects.get(position);
        initializeViews(importantIssue, holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setData(List<ImportantIssue> importantIssues) {
        this.objects = importantIssues;
        notifyDataSetChanged();
    }

    private void initializeViews(ImportantIssue importantIssue, ViewHolder holder) {
        Picasso.get().load(importantIssue.getUrl()).placeholder(R.drawable.ic_launcher_background).into(holder.imgIssue);
        holder.tvIssueDescription.setText(importantIssue.getIssueDescription());

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIssueDescription;
        private ImageView imgIssue;

        public ViewHolder(View view) {
            super(view);
            tvIssueDescription = view.findViewById(R.id.tv_issue_description);
            imgIssue = view.findViewById(R.id.img_issue);


        }
    }
}
