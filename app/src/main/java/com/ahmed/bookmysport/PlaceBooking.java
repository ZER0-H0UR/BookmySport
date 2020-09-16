package com.ahmed.bookmysport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import com.ahmed.bookmysport.DetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlaceBooking extends AppCompatActivity implements View.OnClickListener {
    private EditText selectDate,selectStartTime,selectEndTime,cardName,cardNumber,cardCvv;
    private TextView errorMessage,amountToPay,holderText;
    private Button confirm;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_booking);

        //Firebase authentication & Database
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();
        mDatabaseReference=mDatabase.getReference().child("MUsers");
        mDatabaseReference.keepSynced(true);

        selectDate=(EditText) findViewById(R.id.dateEditTextID);
        selectStartTime=(EditText) findViewById(R.id.startTimeEditTextID);
        selectEndTime=(EditText) findViewById(R.id.endTimeEditTextID);
        cardName=(EditText) findViewById(R.id.cardOwnerID);
        cardNumber=(EditText) findViewById(R.id.cardNumberID);
        cardCvv=(EditText) findViewById(R.id.cardCcvID);

        confirm=(Button) findViewById(R.id.confirmButtonID);

        errorMessage=(TextView) findViewById(R.id.errorMessageTextID);
        amountToPay=(TextView) findViewById(R.id.amountToPayID);
        holderText=(TextView) findViewById(R.id.holderTextID);

        //addonClickListener to edit text to create a popup date/time dialog and for Button
        selectDate.setOnClickListener(this);
        selectStartTime.setOnClickListener(this);
        selectEndTime.setOnClickListener(this);
        confirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirmButtonID:
                ifEmpty();
                break;

            case R.id.dateEditTextID:
                handleDate();
                break;

            case R.id.startTimeEditTextID:
                handleStartTime();
                break;

            case R.id.endTimeEditTextID:
                handleEndTime();
                calculateRate();
                break;

        }
    }

    //Method to check that the fields where info is required to be entered is not empty
    private void ifEmpty(){
        if (cardName.getText().toString().equals("") || cardNumber.getText().toString().equals("") || cardCvv.getText().toString().equals("")){
            errorMessage.setText(R.string.enter_messing_info);
            errorMessage.setVisibility(View.VISIBLE);
        }if (!(cardName.getText().toString().equals("") || cardNumber.getText().toString().equals("") || cardCvv.getText().toString().equals(""))){
            addBookingDetails();
            Toast.makeText(this, "Your Booking is confirmed", Toast.LENGTH_SHORT).show();
            errorMessage.setVisibility(View.INVISIBLE);
            Intent intent1=new Intent(PlaceBooking.this,BookingListActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            finish();
        }
    }

    //Method to create a calender pop-up dialog
    private void handleDate(){
        Calendar calendar=Calendar.getInstance();
        int YEAR=calendar.get(Calendar.YEAR);
        int MONTH=calendar.get(Calendar.MONTH);
        int DATE=calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c1=Calendar.getInstance();
                c1.set(Calendar.YEAR, year);
                c1.set(Calendar.MONTH, month);
                c1.set(Calendar.DATE,dayOfMonth);
                //Setting up the format of the date
                CharSequence dateCharSeq= DateFormat.format("MMM d, yyyy",c1);
                selectDate.setText(dateCharSeq);
            }
        },YEAR,MONTH,DATE);
        datePickerDialog.show();
    }

    //Method to create a time selection pop-up dialog for intiall time
    private void handleStartTime(){
        Calendar calendar=Calendar.getInstance();
        int HOUR=calendar.get(Calendar.HOUR);
        int MINUTE=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c1=Calendar.getInstance();
                c1.set(Calendar.HOUR,hourOfDay);
                //Setting up format of the time "it will show hour only+ if Am or PM
                CharSequence charSequence1=DateFormat.format("h",c1);
                selectStartTime.setText(charSequence1);
            }
        }, HOUR, MINUTE,false);
        timePickerDialog.show();
    }

    //Method to create a time selection pop-up dialog for end time
    private void handleEndTime(){
        Calendar calendar=Calendar.getInstance();
        int HOUR=calendar.get(Calendar.HOUR);
        int MINUTE=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c1=Calendar.getInstance();
                c1.set(Calendar.HOUR,hourOfDay);
                //Setting up format of the time "it will show hour only+ if Am or PM
                CharSequence charSequence2=DateFormat.format("h",c1);
                selectEndTime.setText(charSequence2);
            }
        }, HOUR, MINUTE,false);
        timePickerDialog.show();

    }

    //adding the date of booking and rate into the current user database
    private void addBookingDetails(){
        final String dateSelected=selectDate.getText().toString();
        final String rentingAmount=amountToPay.getText().toString();
        String userId=mAuth.getCurrentUser().getUid();
        DatabaseReference currentUserDb=mDatabaseReference.child(userId);
        currentUserDb.child("Date Booked").setValue(dateSelected);
        currentUserDb.child("PayedAmount").setValue(rentingAmount);

    }

    //Calculating the rent rate of the selected venue
    public void calculateRate(){
        int result=0;
        final int RATE=3;
        if (!(selectStartTime.getText().toString().equals("") || selectEndTime.getText().toString().equals(""))){
            int startTime=Integer.parseInt(selectStartTime.getText().toString().trim());
            int finalTime=Integer.parseInt(selectEndTime.getText().toString().trim());
            result= (finalTime-startTime)*RATE;
            amountToPay.setText(String.valueOf(result)+" Kuwaiti Dinars");
            amountToPay.setVisibility(View.VISIBLE);
            holderText.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
        }else {
            errorMessage.setText(R.string.enter_time_of_booking);
            errorMessage.setVisibility(View.VISIBLE);
            amountToPay.setVisibility(View.INVISIBLE);
            holderText.setVisibility(View.INVISIBLE);
        }

    }
    public void getRateText(){
        if (!(selectStartTime.getText().toString().equals("") && selectEndTime.getText().toString().equals(""))){
            amountToPay.setVisibility(View.VISIBLE);
        }else {
            amountToPay.setVisibility(View.INVISIBLE);
        }
    }

}