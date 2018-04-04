package com.example.abhisheksingh.fireapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class RepairDataFragment extends Fragment {

    final String baseUrl = "https://fireapp-daab6.firebaseio.com/";
    private Button btnSendData;
    private EditText edtHallNumber;
    private EditText edtDoors;
    private EditText edtChairs;
    private EditText edtTables;
    private RecyclerView recyclerView;
    private DatabaseReference globaRef;
    private RepairDataAdapter repairDataAdapter;
    private List<RepairData> repairDataList;
    private OnFragmentInteractionListener mListener;

    public RepairDataFragment() {
        // Required empty public constructor
    }

    public static RepairDataFragment newInstance(String param1, String param2) {
        RepairDataFragment fragment = new RepairDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_repair_data, container, false);
        btnSendData = (Button)view.findViewById(R.id.btnSendData);
        edtHallNumber = (EditText)view.findViewById(R.id.edtHallId);
        edtChairs = (EditText)view.findViewById(R.id.edtUnRepairedChairs);
        edtTables = (EditText)view.findViewById(R.id.edtUnrepairedTables);
        edtDoors = (EditText)view.findViewById(R.id.edtUnRepairedDoors);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        globaRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl);
        repairDataAdapter = new RepairDataAdapter(getActivity(),new ArrayList<RepairData>());
        repairDataAdapter.setData(new ArrayList<RepairData>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL,false));
        recyclerView.setAdapter(repairDataAdapter);

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeData(edtHallNumber.getText().toString(),Integer.parseInt(edtChairs.getText().toString()),Integer.parseInt(edtTables.getText().toString()),Integer.parseInt(edtDoors.getText().toString()));
            }
        });
        Query myTopPostsQuery = globaRef;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                repairDataList = new ArrayList<>();
                for (DataSnapshot repairData: dataSnapshot.getChildren()) {
                    repairDataList.add(repairData.getValue(RepairData.class));
                }

                repairDataAdapter.setData(repairDataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            // TODO: implement the ChildEventListener methods as documented above
            // ...
        });

        return view;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void writeData(String hallId, int chairs, int tables, int doors) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + hallId);
        RepairData data = new RepairData(hallId,chairs,tables,doors);
        myRef.setValue(data);
        Toast.makeText(getActivity(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();
        edtHallNumber.setText("");
        edtTables.setText("");
        edtChairs.setText("");
        edtDoors.setText("");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
