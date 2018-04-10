package com.example.abhisheksingh.fireapp.Fragments;


import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abhisheksingh.fireapp.Adapters.ImportantIssueAdapter;
import com.example.abhisheksingh.fireapp.Helpers.Constants;
import com.example.abhisheksingh.fireapp.Helpers.CustomDialogForImportantIssue;
import com.example.abhisheksingh.fireapp.Models.ImportantIssue;
import com.example.abhisheksingh.fireapp.Models.Stock;
import com.example.abhisheksingh.fireapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.example.abhisheksingh.fireapp.Helpers.Constants.baseUrl;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportantIssueDataFragment extends Fragment implements View.OnClickListener, CustomDialogForImportantIssue.DialogSaveClickedListener {
    private static final int IMP_ISSUE_PIC_CODE = 1112;
    private Button btnAddData;
    private Button btnCancel;
    private ImportantIssueAdapter importantIssueAdapter;
    private RecyclerView recyclerView;
    private List<ImportantIssue> importantIssuesList;
    private DatabaseReference globaRef;
    private ProgressBar mProgressBar;
    private LinearLayout lnrAddIssue;
    public ImageButton imgTakePicture;
    public ImageView imgIssue;
    private EditText edtIssue;
    private Button btnSave;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private File file;

    public ImportantIssueDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_important_issue_data, container, false);
        btnAddData = view.findViewById(R.id.btn_add_issue);
        recyclerView = view.findViewById(R.id.recyclerView);
        mProgressBar = view.findViewById(R.id.progress_bar);
        lnrAddIssue = view.findViewById(R.id.lnr_add_issue);
        imgTakePicture = view.findViewById(R.id.img_take_picture);
        imgIssue = view.findViewById(R.id.img_issue);
        btnSave = view.findViewById(R.id.btn_save);
        edtIssue = view.findViewById(R.id.edt_issue_description);
        btnCancel = view.findViewById(R.id.btn_cancel);
        imgTakePicture.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        btnAddData.setOnClickListener(this);
        firebaseStorage = FirebaseStorage.getInstance();
        importantIssueAdapter = new ImportantIssueAdapter(getActivity(), new ArrayList<ImportantIssue>());
        importantIssueAdapter.setData(new ArrayList<ImportantIssue>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));
        recyclerView.setAdapter(importantIssueAdapter);
        globaRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + Constants.importantIssue);
        Query myTopPostsQuery = globaRef;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                importantIssuesList = new ArrayList<>();
                for (DataSnapshot importantIssue : dataSnapshot.getChildren()) {
                    importantIssuesList.add(importantIssue.getValue(ImportantIssue.class));
                }
                importantIssueAdapter.setData(importantIssuesList);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(getContext(), "Some error occurred,please try again!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if (type == IMP_ISSUE_PIC_CODE) {
                    file = imageFile;
                    Uri uri = Uri.fromFile(imageFile);
                    imgIssue.setImageURI(uri);
                    imgIssue.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_issue:
                lnrAddIssue.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_save:
                mProgressBar.setVisibility(View.VISIBLE);
                storageReference = firebaseStorage.getReference("images");
                final ImportantIssue importantIssue = new ImportantIssue();
                importantIssue.setId(String.valueOf(System.currentTimeMillis()));
                importantIssue.setIssueDescription(edtIssue.getText().toString());
                Uri uri = Uri.fromFile(file);
                final StorageReference photoRef = storageReference.child(importantIssue.getId());
                photoRef.putFile(uri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        importantIssue.setUrl(taskSnapshot.getDownloadUrl().toString());
                        Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        writeData(importantIssue);
                    }
                });
                break;

            case R.id.btn_cancel:
                lnrAddIssue.setVisibility(View.GONE);
                break;

            case R.id.img_take_picture:
                EasyImage.openChooserWithGallery(this, "Select", IMP_ISSUE_PIC_CODE);
                break;
        }
    }

    private void writeData(ImportantIssue importantIssue) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + Constants.importantIssue + importantIssue.getId());
        myRef.setValue(importantIssue);
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        resetView();
    }

    private void resetView() {
        edtIssue.setText("");
        imgIssue.setImageResource(0);
        lnrAddIssue.setVisibility(View.GONE);

    }

    @Override
    public void onDialogSaveButtonClicked(Stock stock) {
        writeData(stock);
    }

    private void writeData(Stock stock) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + Constants.stocks + stock.getId());
        myRef.setValue(stock);
        Toast.makeText(getActivity(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();

    }

}
