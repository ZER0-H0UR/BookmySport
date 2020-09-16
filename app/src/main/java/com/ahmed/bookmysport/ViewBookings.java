package com.ahmed.bookmysport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Objects.User;

public class ViewBookings extends AppCompatActivity {
    private TextView text1,text2,text3,text4,textVenueName,textSportType,textDateBooked,textAmountPayed;
    private Button cancelButton;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private String userID;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);
        textVenueName=(TextView) findViewById(R.id.venueNameTextID);
        textSportType=(TextView) findViewById(R.id.getSportTypeID);
        textDateBooked=(TextView) findViewById(R.id.getDateSelectedID);
        textAmountPayed=(TextView) findViewById(R.id.getAmountTextID);
        text1=(TextView) findViewById(R.id.textView5);
        text2=(TextView) findViewById(R.id.textView15);
        text3=(TextView) findViewById(R.id.textView16);
        text4=(TextView) findViewById(R.id.textView17);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference("MUsers");
        mDatabaseReference.keepSynced(true);

        cancelButton=(Button) findViewById(R.id.cancelButtonID);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    textVenueName.setText(ds.child("VenueName").getValue(String.class));
                    textSportType.setText(ds.child("SportType").getValue(String.class));
                    textDateBooked.setText(ds.child("Date Booked").getValue(String.class));
                    textAmountPayed.setText(ds.child("PayedAmount").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellationDialog();
            }
        });



    }

    //Method to remove the booking details of the user
    public void removeBookingInfo(){
        String userId=mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb=mDatabaseReference.child(userId);
        currentUserDb.child("Date Booked").setValue("No Booking made yet");
        currentUserDb.child("PayedAmount").setValue("No Booking made yet");
        currentUserDb.child("VenueName").setValue("No Booking made yet");
        currentUserDb.child("SportType").setValue("No Booking made yet");
    }

    //Method that creates an alert dialog for the booking cancellation
    public void cancellationDialog(){
        alertDialog=new AlertDialog.Builder(ViewBookings.this);
        alertDialog.setTitle(getResources().getString(R.string.alert_title));
        alertDialog.setMessage(getResources().getString(R.string.alert_message));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getResources().getString(R.string.postive_alert), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeBookingInfo();
                Toast.makeText(ViewBookings.this, "Your Booking has been cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.negative_alert), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.create();
        alertDialog.show();

    }




}