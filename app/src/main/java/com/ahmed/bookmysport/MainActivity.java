package com.ahmed.bookmysport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private static final String TAG = "MainActivity";
    private EditText email,password;
    private Button login,register;
    private TextView forgotPassword, errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //adding widgets by id
        email=(EditText) findViewById(R.id.registerEmailID);
        password=(EditText) findViewById(R.id.registerPasswordID);
        login=(Button) findViewById(R.id.loginButtonID);
        register= (Button) findViewById(R.id.registerButtonID);
        forgotPassword=(TextView) findViewById(R.id.forgotTextID);
        errorMessage=(TextView) findViewById(R.id.errorTextID);

        //onClick listeners for widgets
        login.setOnClickListener(MainActivity.this);
        register.setOnClickListener(MainActivity.this);
        forgotPassword.setOnClickListener(MainActivity.this);

        //Firebase authentication & Database
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference("Welcome Message");
        mDatabaseReference.setValue("Welcome to Book mySport");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value= snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this,value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mAuthListener= new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser=firebaseAuth.getCurrentUser();
                if (mUser!=null){
                    //user is signed in
                    Log.d(TAG, "user signed in");
                }else{
                    //user is signed out
                    Log.d(TAG, "user signed out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuth!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButtonID:
                String emailString=email.getText().toString();
                String passwordString=password.getText().toString();
                //condition in-case fields are empty
                if (!emailString.equals("") && !passwordString.equals("")){
                    //passing method of mAuth to get user/pass
                    mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Failed Sign-in",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(MainActivity.this," Sign-in succsesful",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, BookingListActivity.class));
                                finish();
                            }
                        }
                    });
                }
                checkEmpty();
                break;
            case R.id.registerButtonID:
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                break;

            case R.id.forgotTextID:
                startActivity(new Intent(MainActivity.this,ResetPassword.class));
                break;
        }
    }
    //Method to check that data has been entered in email and password login fields
    private void checkEmpty(){
        if (email.getText().toString().equals("") || password.getText().toString().equals("")){
            errorMessage.setVisibility(View.VISIBLE);
        }else{
            errorMessage.setVisibility(View.INVISIBLE);
        }
    }
}