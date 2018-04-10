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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.abhisheksingh.fireapp.Adapters.RepairDataAdapter;
import com.example.abhisheksingh.fireapp.Helpers.Constants;
import com.example.abhisheksingh.fireapp.Models.RepairData;
import com.example.abhisheksingh.fireapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.example.abhisheksingh.fireapp.Helpers.Constants.baseUrl;

public class RepairDataFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = RepairDataFragment.class.getSimpleName() ;
    private static final int IMG_REPAIR_PIC_CODE = 1111;
    private Button btnSendData;
    private RecyclerView recyclerView;
    private DatabaseReference globaRef;
    private ProgressBar mProgressBar;
    private RepairDataAdapter repairDataAdapter;
    private List<RepairData> repairDataList;
    private MaterialSpinner spnrWork;
    private MaterialSpinner spnr2;
    private MaterialSpinner spnr3;
    private String workCat1;
    private String workCat2;
    private String workCat3;
    private String workCat4;
    private int selecteedId;
    private String[] array;
    private File file;
    private EditText edtIssueDescription;
    private RadioGroup radioGroup;
    private ImageButton imgButton;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private ImageView imgIssue;
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
        btnSendData = view.findViewById(R.id.btnSendData);
        edtIssueDescription = view.findViewById(R.id.edt_issue_description);
        imgButton = view.findViewById(R.id.img_take_picture);
        imgIssue = view.findViewById(R.id.img_issue);
        spnrWork =  view.findViewById(R.id.spnr_work);
        spnr2 =  view.findViewById(R.id.spnr2);
        spnr3 = view.findViewById(R.id.spnr3);
        radioGroup = view.findViewById(R.id.radiogroup);
        spnrWork.setItems(getResources().getStringArray(R.array.work));
        recyclerView = view.findViewById(R.id.recyclerView);
        globaRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + Constants.reports);
        firebaseStorage = FirebaseStorage.getInstance();
        mProgressBar = view.findViewById(R.id.progress_bar);
        repairDataAdapter = new RepairDataAdapter(getActivity(),new ArrayList<RepairData>());
        repairDataAdapter.setData(new ArrayList<RepairData>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL,false));
        recyclerView.setAdapter(repairDataAdapter);
        mProgressBar.setVisibility(View.GONE);
        btnSendData.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = radioGroup.findViewById(i);
                workCat4 = checkedRadioButton.getText().toString();

            }
        });
        spnrWork.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                workCat1 = item.toString();
                if (item.toString().equals("SELECT")) {
                    spnr2.setVisibility(View.GONE);
                    spnr3.setVisibility(View.GONE);
                    edtIssueDescription.setVisibility(View.GONE);
                    imgButton.setVisibility(View.GONE);
                } else {
                    spnr2.setVisibility(View.VISIBLE);
                    spnr3.setVisibility(View.VISIBLE);
                    edtIssueDescription.setVisibility(View.VISIBLE);
                    imgButton.setVisibility(View.VISIBLE);
                    spnr3.setItems(getResources().getStringArray(R.array.common_issues));
                    switch (item.toString()) {
                        case "NORTH PAVILION":
                            spnr2.setItems(getResources().getStringArray(R.array.north_pavilion));
                            break;
                        case "SOUTH PAVILION":
                            spnr2.setItems(getResources().getStringArray(R.array.south_pavilion));
                            break;
                        case "EAST GALLERY":
                            spnr2.setItems(getResources().getStringArray(R.array.east_pavilion));
                            break;
                        case "WEST GALLERY":
                            spnr2.setItems(getResources().getStringArray(R.array.west_pavilion));
                            break;
                        case "ADMIN BLOCK":
                            spnr2.setItems(getResources().getStringArray(R.array.admin_block));
                            break;
                    }
                }
            }
        });

        spnr2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                workCat2 = item.toString();
                spnr3.setItems(getResources().getStringArray(R.array.common_issues));

            }
        });
       spnr3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
               workCat3 = item.toString();
               switch (item.toString())
               {
                   case ("INFRASTRUCTURE"):
                       addRadioButtons("INFRASTRUCTURE");
                       break;
                   case("CHAIR MAINTAINACE"):
                       addRadioButtons("CHAIR MAINTAINACE");
                       break;
                   case("ELECTRICITY"):
                       addRadioButtons("ELECTRICITY");
                       break;
                   case("HOUSEKEEPING"):
                       addRadioButtons("HOUSEKEEPING");
                       break;
                       default:
                           addRadioButtons("ANY OTHER SPECIFIC ISSUES");
               }
           }
       });

        return view;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void addRadioButtons(String option) {
         radioGroup.removeAllViews();
         for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(getActivity());
            ll.setOrientation(LinearLayout.VERTICAL);
             array = new String[0];
            switch (option)
            {
                case ("INFRASTRUCTURE"):
                    array = getResources().getStringArray(R.array.infrastructure);
                    break;
                case("CHAIR MAINTAINACE"):
                    array = getResources().getStringArray(R.array.chair_maintenance);
                    break;
                case("ELECTRICITY"):
                    array = getResources().getStringArray(R.array.electricity);
                    break;
                case("HOUSEKEEPING"):
                    array = getResources().getStringArray(R.array.house_keeping);
                    break;
                    default:
                        radioGroup.removeAllViews();
            }

            for (int i = 0; i < array.length; i++) {
                RadioButton rdbtn = new RadioButton(getActivity());
                rdbtn.setId(i);
                rdbtn.setText(array[i]);
                ll.addView(rdbtn);
            }
          radioGroup.addView(ll);
        }

    }

    private void writeData(RepairData data) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl+ Constants.reports + data.hallId);
        myRef.setValue(data);
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();
        resetView();
    }

    private void resetView() {
        spnrWork.setSelectedIndex(0);
        spnr2.setVisibility(View.GONE);
        spnr3.setVisibility(View.GONE);
        edtIssueDescription.setVisibility(View.GONE);
        imgButton.setVisibility(View.GONE);
        imgIssue.setImageResource(0);
        imgIssue.setVisibility(View.GONE);
        radioGroup.removeAllViews();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(getContext(),"Some error occurred,please try again!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                if (type == IMG_REPAIR_PIC_CODE) {
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
        switch (view.getId())
        {
            case R.id.btnSendData:
                mProgressBar.setVisibility(View.VISIBLE);
                final RepairData data = new RepairData();
                data.hallId = String.valueOf(System.currentTimeMillis());
                data.workCat1 = workCat1;
                data.workCat2 = workCat2;
                data.workCat3 = workCat3;
                data.workCat4 = workCat4;
                data.issueDescription = edtIssueDescription.getText().toString();
                storageReference = firebaseStorage.getReference( "images");
                final StorageReference photoRef = storageReference.child(data.hallId);
                photoRef.putFile(Uri.fromFile(file)).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        data.imageUrl = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(getContext(),"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        writeData(data);
                    }
                });
                break;

            case R.id.img_take_picture:
                EasyImage.openChooserWithGallery(this, "Select", IMG_REPAIR_PIC_CODE);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
