package com.example.abhisheksingh.fireapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.abhisheksingh.fireapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StocksFragment extends Fragment implements View.OnClickListener {
    private Button btnAddData;

    public StocksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_stocks, container, false);
        btnAddData =  view.findViewById(R.id.btn_add);
        btnAddData.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_add:
                break;
        }
    }
}
