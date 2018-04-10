package com.example.abhisheksingh.fireapp.Fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhisheksingh.fireapp.Adapters.RepairDataAdapter;
import com.example.abhisheksingh.fireapp.Adapters.StocksDataAdapter;
import com.example.abhisheksingh.fireapp.Helpers.Constants;
import com.example.abhisheksingh.fireapp.Helpers.CustomDialog;
import com.example.abhisheksingh.fireapp.Helpers.CustomDialogForStocks;
import com.example.abhisheksingh.fireapp.Models.RepairData;
import com.example.abhisheksingh.fireapp.Models.Stock;
import com.example.abhisheksingh.fireapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.example.abhisheksingh.fireapp.Helpers.Constants.baseUrl;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockDataFragment extends Fragment implements View.OnClickListener,CustomDialogForStocks.DialogSaveClickedListener{
    private Button btnAddData;
    private StocksDataAdapter stocksDataAdapter;
    private RecyclerView recyclerView;
    private List<Stock> stockDataList;
    private DatabaseReference globaRef;
    private ProgressBar mProgressBar;

    public StockDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stocks, container, false);
        btnAddData =  view.findViewById(R.id.btn_add);
        recyclerView = view.findViewById(R.id.recyclerView);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        btnAddData.setOnClickListener(this);
        stocksDataAdapter = new StocksDataAdapter(getActivity(),new ArrayList<Stock>());
        stocksDataAdapter.setData(new ArrayList<Stock>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL,false));
        recyclerView.setAdapter(stocksDataAdapter);
        globaRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + Constants.stocks);
        Query myTopPostsQuery = globaRef;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stockDataList = new ArrayList<>();
                for (DataSnapshot stock: dataSnapshot.getChildren()) {
                    stockDataList.add(stock.getValue(Stock.class));
                }
                stocksDataAdapter.setData(stockDataList);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            // TODO: implement the ChildEventListener methods as documented above
            // ...
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_add:
                CustomDialogForStocks customDialog = new CustomDialogForStocks(getActivity(),this);
                customDialog.setCancelable(false);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
                break;
        }
    }

    @Override
    public void onDialogSaveButtonClicked(Stock stock) {
         writeData(stock);
    }

    private void writeData(Stock stock) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl+ Constants.stocks + stock.getId());
        myRef.setValue(stock);
        Toast.makeText(getActivity(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();

    }

}
