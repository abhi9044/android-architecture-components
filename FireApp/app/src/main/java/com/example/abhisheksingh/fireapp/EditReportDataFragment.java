package com.example.abhisheksingh.fireapp;


import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import static com.example.abhisheksingh.fireapp.Constants.baseUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditReportDataFragment extends Fragment implements EditDataAdapter.EditData{

    private RecyclerView recyclerView;
    private DatabaseReference globaRef;
    private EditDataAdapter editDataAdapter;
    private List<RepairData> repairDataList;
    private ProgressBar mProgressBar;

    public EditReportDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_report_data,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        globaRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.baseUrl);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        editDataAdapter = new EditDataAdapter(getActivity(),new ArrayList<RepairData>(),this);
        editDataAdapter.setData(new ArrayList<RepairData>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL,false));
        recyclerView.setAdapter(editDataAdapter);
        Query myTopPostsQuery = globaRef;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                repairDataList = new ArrayList<>();
                for (DataSnapshot repairData: dataSnapshot.getChildren()) {
                    repairDataList.add(repairData.getValue(RepairData.class));
                }
                editDataAdapter.setData(repairDataList);
                mProgressBar.setVisibility(View.GONE);

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
    public void editUserData(String hallId, int unChairs, int unTables, int unDoors) {
        RepairData data = new RepairData(hallId,unChairs,unTables,unDoors);
        writeData(data);
    }

    @Override
    public void deleteUserData(int pos) {
       deleteData(repairDataList.get(pos));
    }

    private void deleteData(RepairData data) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + data.hallId);
        myRef.removeValue();
        Toast.makeText(getActivity(),"Data deleted successfully",Toast.LENGTH_LONG);

    }
    private void writeData(RepairData data) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + data.hallId);
        RepairData newData = new RepairData(data.hallId,data.unRepairedChairs,data.unRepairedTables,data.unRepairedDoors);
        myRef.setValue(newData);
        Toast.makeText(getActivity(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();
    }
}
