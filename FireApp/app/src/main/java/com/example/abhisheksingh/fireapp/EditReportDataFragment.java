package com.example.abhisheksingh.fireapp;


import android.os.Bundle;
import android.os.Environment;
import android.os.RecoverySystem;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Button btnShareData;
    public EditReportDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_report_data,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        btnShareData = view.findViewById(R.id.btn_share);
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
                Gson gson = new Gson();
                String reqJson= gson.toJson(repairDataList);

                JsonFlattener parser = new JsonFlattener();
                CSVWriters writer = new CSVWriters();

                List<Map<String, String>> flatJson = null;
                File pathToExternalStorage = Environment.getExternalStorageDirectory();
                //to this path add a new directory path and create new App dir (InstroList) in /documents Dir
                File appDirectory = new File(pathToExternalStorage.getAbsolutePath()  + "/documents/sample.csv");
                String path = pathToExternalStorage.getAbsolutePath() + "/documents/sample.csv";

                try {
                    flatJson = parser.parseJson(reqJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String filename = System.currentTimeMillis()+"sample.csv";
                  String output = writer.writeAsCSV(flatJson, path);
                  Utils.writeToFile(output);
                  Log.d("YOYO",output);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
    public void editUserData(RepairData data) {
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
        myRef.setValue(data);
        Toast.makeText(getActivity(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();
    }
}
