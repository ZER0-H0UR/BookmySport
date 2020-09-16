package com.ahmed.bookmysport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Model.Adaptor;
import Objects.Venue;

public class BookingListActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private RecyclerView recyclerView;
    private Adaptor mAdaptor;
    private List<Venue> venueL;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private TextView navUserName,navUserEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        //Firebase authentication & Database
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        mDatabaseReference = mDatabase.getReference().child("MVenue");
        mDatabaseReference.keepSynced(true);

        //drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //gives the icon and back animation
        //getSupportActionBar().setHomeButtonEnabled(true);



        //Navigation on click listener to go to new activity
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        updateNavHeader();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_viewBooking:
                        startActivity(new Intent(BookingListActivity.this,ViewBookings.class));
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //setting the status bar background to transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //adding items to array-list of type Venue to be displayed in recyclerview
        venueL = new ArrayList<>();
        venueL.add(new Venue(R.drawable.soccer, "7A Football Club", "Soccer", "Farwaniya", R.drawable.soccer_icon, true, true, false, true, false, true));
        venueL.add(new Venue(R.drawable.basketball, "NBA Academy", "BasketBall", "Hawally", R.drawable.basketball_icon, false, true, false, false, false, true));
        venueL.add(new Venue(R.drawable.swimming, "Sea Dolpins Academy", "Swimming", "Salmiya", R.drawable.swimming_icon, false, false, false, false, false, true));
        venueL.add(new Venue(R.drawable.cricket, "Cricket Academy", "Cricket", "Jahra", R.drawable.cricket_icon, false, true, false, true, false, false));
        venueL.add(new Venue(R.drawable.tennis, "Tennis Club Society", "Tennis", "Jabriya", R.drawable.tennis_icon, true, true, true, true, true, true));
        venueL.add(new Venue(R.drawable.rugby, "X Men Rugby", "Rugby", "Farwaniya", R.drawable.rugby_icon, true, false, true, true, false, true));
        venueL.add(new Venue(R.drawable.hockey, "Ice Hockry Legends", "Hockey", "Farwaniya", R.drawable.hockey_icon, true, true, false, true, false, true));
        venueL.add(new Venue(R.drawable.golf, "National Golf Club", "Golf", "Farwaniya", R.drawable.golf_icon, false, true, false, true, false, true));
        venueL.add(new Venue(R.drawable.horse, "Salmiya Horse stables", "Horse Riding", "Salmiya"));

        //setting up the recyclerview to display items in the adaptor
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookingListActivity.this));

        //Linking items in adaptor to the activity containing the recyclerview
        mAdaptor = new Adaptor(BookingListActivity.this, venueL);
        recyclerView.setAdapter(mAdaptor);

    }


    //override method so when we press back it closes the navigation drawer and not the activity
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Attaching toolbar to activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){ //attaching the navigation view so it can be clicked
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_signout:
                if (mUser != null && mAuth != null) {
                    mAuth.signOut();
                    startActivity(new Intent(BookingListActivity.this, MainActivity.class));
                    finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Creating options on toolbar aka inflate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        SearchView searchView = (SearchView) item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Here we call getFilter method in adaptor
                mAdaptor.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //method that updates the header part of the navigation view with the current logged in user information
    public void updateNavHeader(){
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        View headerView= navigationView.getHeaderView(0);
        //navUserName=headerView.findViewById(R.id.displayUserNameID);
        navUserEmail=headerView.findViewById(R.id.displayUserEmailID);
        //navUserName.setText(mUser.getDisplayName());
        navUserEmail.setText(mUser.getEmail());
    }





}



