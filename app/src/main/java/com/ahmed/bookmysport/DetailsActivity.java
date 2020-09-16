package com.ahmed.bookmysport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import Objects.Venue;
import Model.Adaptor;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView background, profilePicture, sportTypeIcon, bathroomIcon, carParkIcon, firstAidIcon, resturantIcon, wifiIcon, lockerIcon;
    private TextView venueName,sportType,bathroomText, carParkText, firstAidText, resturantText, wifiText, lockerText;
    private Button booking;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private static final String TAG="Details Activity";
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Firebase authentication & Database
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();
        mDatabaseReference=mDatabase.getReference().child("MUsers");
        mDatabaseReference.keepSynced(true);

        background = (ImageView) findViewById(R.id.backgroundCopyID);
        profilePicture = (ImageView) findViewById(R.id.profilePictureCopyID);
        sportTypeIcon = (ImageView) findViewById(R.id.sportTypeImageID);
        bathroomIcon = (ImageView) findViewById(R.id.image1ID);
        carParkIcon = (ImageView) findViewById(R.id.image2ID);
        firstAidIcon = (ImageView) findViewById(R.id.image3ID);
        lockerIcon = (ImageView) findViewById(R.id.image4ID);
        wifiIcon = (ImageView) findViewById(R.id.image6ID);
        resturantIcon = (ImageView) findViewById(R.id.image5ID);

        venueName=(TextView) findViewById(R.id.venueNameCopyID);
        sportType=(TextView) findViewById(R.id.sportDescriptionID);
        bathroomText = (TextView) findViewById(R.id.text1ID);
        carParkText = (TextView) findViewById(R.id.text2ID);
        firstAidText = (TextView) findViewById(R.id.text3ID);
        lockerText = (TextView) findViewById(R.id.text4ID);
        wifiText = (TextView) findViewById(R.id.text6ID);
        resturantText = (TextView) findViewById(R.id.text5ID);

        //Declaring the map fragment and linking the fragment view to the current activity
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapID);
        mapFragment.getMapAsync(DetailsActivity.this);
        booking=(Button) findViewById(R.id.goButtonID);


        //setting the status bar background to transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVenueDetails();
                Intent intent=new Intent(DetailsActivity.this,PlaceBooking.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //Getting the intent from the adaptor and adding it to DetailsActivity
        String venueTitle=getIntent().getStringExtra("venueName");
        String sportName=getIntent().getStringExtra("sportType");
        int copybackground=getIntent().getIntExtra("background",0);
        int sporticon=getIntent().getIntExtra("sporticon",0);
        boolean hasWashroom=getIntent().getBooleanExtra("bathroom",false);
        boolean hasCarParking=getIntent().getBooleanExtra("carpark",false);
        boolean hasAid=getIntent().getBooleanExtra("firstaid",false);
        boolean hasSafes=getIntent().getBooleanExtra("lockers",false);
        boolean hasFood=getIntent().getBooleanExtra("resturant",false);
        boolean hasInternet=getIntent().getBooleanExtra("wifi",false);
        background.setImageResource(copybackground);
        sportTypeIcon.setImageResource(sporticon);
        venueName.setText(venueTitle);
        sportType.setText(sportName);

        //Here is condition to check the database of the Venue and check what facilities/services the venue have and display's it in DetailsActivity "Dynamically"
        if (hasWashroom==true){
            bathroomIcon.setImageResource(R.drawable.bathroom_icon);
            bathroomIcon.setVisibility(View.VISIBLE);
            bathroomText.setVisibility(View.VISIBLE);
        } if (hasCarParking==true){
            carParkIcon.setImageResource(R.drawable.parking_icon);
            carParkIcon.setVisibility(View.VISIBLE);
            carParkText.setVisibility(View.VISIBLE);
        } if (hasAid==true){
            firstAidIcon.setImageResource(R.drawable.first_aid_icon);
            firstAidIcon.setVisibility(View.VISIBLE);
            firstAidText.setVisibility(View.VISIBLE);
        } if (hasSafes==true){
            lockerIcon.setImageResource(R.drawable.locker_icon);
            lockerIcon.setVisibility(View.VISIBLE);
            lockerText.setVisibility(View.VISIBLE);
        } if (hasFood==true){
            resturantIcon.setImageResource(R.drawable.resturant_icon);
            resturantIcon.setVisibility(View.VISIBLE);
            resturantText.setVisibility(View.VISIBLE);
        } if (hasInternet==true){
            wifiIcon.setImageResource(R.drawable.wifi_icon);
            wifiIcon.setVisibility(View.VISIBLE);
            wifiText.setVisibility(View.VISIBLE);
        }

    }


    //Method to create a map that is linked to the fragment and a location is add with a marker
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        LatLng mainLocation= new LatLng(29.332708, 48.073740);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mainLocation));
        mMap.addMarker(new MarkerOptions().position(mainLocation).title("Your Sport Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainLocation,15));
    }


    //add details of the venue into the current user database
    public void addVenueDetails(){
        String userId=mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb=mDatabaseReference.child(userId);
        currentUserDb.child("VenueName").setValue(getIntent().getStringExtra("venueName"));
        currentUserDb.child("SportType").setValue(getIntent().getStringExtra("sportType"));
    }

}


