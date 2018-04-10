package com.example.abhisheksingh.fireapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhisheksingh.fireapp.Fragments.EditReportDataFragment;
import com.example.abhisheksingh.fireapp.Fragments.StockDataFragment;
import com.example.abhisheksingh.fireapp.Helpers.Constants;
import com.example.abhisheksingh.fireapp.Helpers.CustomDialog;
import com.example.abhisheksingh.fireapp.Helpers.Utils;
import com.example.abhisheksingh.fireapp.Models.UserProfile;
import com.example.abhisheksingh.fireapp.R;
import com.example.abhisheksingh.fireapp.Fragments.RepairDataFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.example.abhisheksingh.fireapp.Helpers.Constants.baseUrl;

public class MasterDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,CustomDialog.DialogSaveClickedListener, View.OnClickListener {
    private static final String TAG = MasterDetailActivity.class.getSimpleName();
    private static final int PROFILE_PIC_CODE = 1110;
    private TextView tvName;
    private TextView tvPhoneNumber;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_detail_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send this data via email", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setCheckedItem(R.id.nav_work);
        navigationView.setItemIconTintList(null);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            loadFragment(new RepairDataFragment());
            navigationView.setNavigationItemSelectedListener(this);
            View header=navigationView.getHeaderView(0);
            /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            tvPhoneNumber = header.findViewById(R.id.tv_phone_num);
            tvName = header.findViewById(R.id.tv_name);
            imgProfile = header.findViewById(R.id.img_profile_picture);
            imgProfile.setOnClickListener(this);
            tvPhoneNumber.setText(user.getPhoneNumber());

            if (Utils.isBlank(user.getDisplayName()))
            {
                CustomDialog customDialog = new CustomDialog(this);
                customDialog.setCancelable(false);
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customDialog.show();
            }
            else
            {
                tvName.setText(user.getDisplayName());
                if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals(Uri.EMPTY)) {
                    //doTheThing()
                    imgProfile.setImageURI(user.getPhotoUrl());
                }
            }
        }
        else
        {
            Intent logoutIntent = new Intent(this,LoginActivity.class);
            startActivity(logoutIntent);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_work) {
            loadFragment(new RepairDataFragment());
        } else if (id == R.id.nav_stock) {
            loadFragment(new StockDataFragment());

        }
        else if (id == R.id.nav_reports) {
            loadFragment(new EditReportDataFragment());

        }
        else if (id == R.id.nav_imp_issue) {
            loadFragment(new EditReportDataFragment());

        } else if (id == R.id.nav_logout) {
         logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent logoutIntent = new Intent(this,LoginActivity.class);
        startActivity(logoutIntent);
    }

    public void loadFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.lnr,fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDialogSaveButtonClicked(UserProfile profile) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl(baseUrl + Constants.userProfile);
            myRef.setValue(profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(profile.getName()).build();
        user.updateProfile(profileUpdates);
        user.updateEmail(profile.getEmail());
        tvName.setText(profile.getName());
            Toast.makeText(this,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, MasterDetailActivity.this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(MasterDetailActivity.this,"Some error occurred,please try again!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Uri uri = Uri.fromFile(imageFile);
                imgProfile.setImageURI(uri);
                imgProfile.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build());
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_profile_picture:
                EasyImage.openChooserWithGallery(this,"Select",PROFILE_PIC_CODE);
        }
    }
}
