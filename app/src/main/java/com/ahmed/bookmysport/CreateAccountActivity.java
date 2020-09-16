package com.ahmed.bookmysport;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Objects.User;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText email,password,firstName,lastName;
    private TextView errorMessageRegister;
    private Button createAccount;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MUsers");
        mDatabaseReference.keepSynced(true);

        mAuth=FirebaseAuth.getInstance();

        email=(EditText) findViewById(R.id.registerEmailID);
        password=(EditText) findViewById(R.id.registerPasswordID);
        firstName=(EditText) findViewById(R.id.enterFirstNameID);
        lastName=(EditText) findViewById(R.id.enterLastNameID);
        errorMessageRegister=(TextView) findViewById(R.id.errorText2ID);

        createAccount=(Button) findViewById(R.id.createAccountBtnID);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }

        });

    }
    //method for creation of a new account and "trim" is used to remove empty spaces
    public void createNewAccount(){
        final String fName=firstName.getText().toString().trim();
        final String lName=lastName.getText().toString().trim();
        final String em=email.getText().toString().trim();
        String pass=password.getText().toString().trim();

        if (!(TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(em)
        || TextUtils.isEmpty(pass)) ){
            mAuth.createUserWithEmailAndPassword(em,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult !=null){
                        String userId=mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb=mDatabaseReference.child(userId);
                        currentUserDb.child("firstName").setValue(fName);
                        currentUserDb.child("lastName").setValue(lName);
                        currentUserDb.child("Email").setValue(em);
                        currentUserDb.child("Date Booked").setValue("No Booking made yet");
                        currentUserDb.child("PayedAmount").setValue("No Booking made yet");
                        currentUserDb.child("VenueName").setValue("No Booking made yet");
                        currentUserDb.child("SportType").setValue("No Booking made yet");

                        Toast.makeText(CreateAccountActivity.this,"Creating Account.....",Toast.LENGTH_LONG).show();
                        //send user now after account creation to venue list to view different sport venues
                        Intent intent=new Intent(CreateAccountActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
        checkEmpty();
    }

    //Method to check that all fields are entered to register account
    private void checkEmpty(){
        if (firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("")){
            errorMessageRegister.setVisibility(View.VISIBLE);
        }else{
            errorMessageRegister.setVisibility(View.INVISIBLE);
        }
    }

}