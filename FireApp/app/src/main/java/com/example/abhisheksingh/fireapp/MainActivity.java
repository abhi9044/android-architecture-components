package com.example.abhisheksingh.fireapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.*;

/**
 * Created by abhisheksingh on 02/04/18.
 */

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSendData = (Button)findViewById(R.id.btnSendData);
        edtHallNumber = (EditText)findViewById(R.id.edtHallId);
        edtChairs = (EditText)findViewById(R.id.edtUnRepairedChairs);
        edtTables = (EditText)findViewById(R.id.edtUnrepairedTables);
        edtDoors = (EditText)findViewById(R.id.edtUnRepairedDoors);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        globaRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl);
        repairDataAdapter = new RepairDataAdapter(this,new ArrayList<RepairData>());
        repairDataAdapter.setData(new ArrayList<RepairData>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, VERTICAL,false));
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
    }
    private void writeData(String hallId, int chairs, int tables, int doors) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + hallId);
        RepairData data = new RepairData(hallId,chairs,tables,doors);
        myRef.setValue(data);
        Toast.makeText(this,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
        edtHallNumber.setText("");
        edtTables.setText("");
        edtChairs.setText("");
        edtDoors.setText("");
    }
}
