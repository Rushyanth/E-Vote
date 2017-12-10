package com.example.rushyanthreddy.sumeetproj;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Rushyanth Reddy on 3/18/2017.
 */
public class SignUp extends AppCompatActivity{

    EditText userFirstName,userLastName,userEmail,userPassword,userRetypePassword,userPhoneNumber;
    private  FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    Button createaccount;

    private DatabaseReference mDatabase;
    public String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Bundle b = getIntent().getExtras();
        userFirstName=(EditText)findViewById(R.id.signup_edittext_FirstName);
        userLastName=(EditText)findViewById(R.id.signup_edittext_LastName);
        userEmail=(EditText)findViewById(R.id.signup_edittext_Email);
        userPassword=(EditText)findViewById(R.id.signup_edittext_Password);
        userRetypePassword=(EditText)findViewById(R.id.signup_edittext_RetypePassword);
        userPhoneNumber=(EditText)findViewById(R.id.signup_edittext_PhoneNumber);
        createaccount = (Button)findViewById(R.id.signup_button_createaccount);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean network;
                network=haveNetworkConnection();
                if(network) {
                    createAccount(userEmail.getText().toString(), userPassword.getText().toString());
                    Log.d(TAG, "Clicked on button");
                }else
                    Toast.makeText(SignUp.this,"No internet Connection",Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String phoneNumber = userPhoneNumber.getText().toString();
        if(TextUtils.isEmpty(firstName)){
            userFirstName.setError("Required.");
        }
        if(TextUtils.isEmpty(lastName)){
            userLastName.setError("Required.");
        }
        if(TextUtils.isEmpty(phoneNumber)){
            userPhoneNumber.setError("Required.");
        }
        String email = userEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userEmail.setError("Required.");
            valid = false;
        } else {
            userEmail.setError(null);
        }
        String retypePassword = userRetypePassword.getText().toString();
        String password = userPassword.getText().toString();
        if(retypePassword.equals(password)){
            if (TextUtils.isEmpty(password)) {
                userPassword.setError("Required.");
                valid = false;
            } else {
                userPassword.setError(null);
            }
        }else {
            userRetypePassword.setError("Passwords don't match");
        }
        return valid;
    }

    private void createAccount(String email,String password){

        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    uid =  mAuth.getCurrentUser().getUid();
                    Log.d(TAG,"uid  "+ uid);
                    createUserInDatabase(uid);
                    Intent i1 = new Intent(SignUp.this,MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i1);
                }

                if (!task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Crearting user Failed", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private void createUserInDatabase(String uid){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        User user = new User(userFirstName.getText().toString(),userLastName.getText().toString(),userPhoneNumber.getText().toString(),userEmail.getText().toString());
        mDatabase.child("USERS").child(uid).setValue(user);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("FirstName",userFirstName.getText().toString());
        outState.putString("LastName",userLastName.getText().toString());
        outState.putString("Email",userEmail.getText().toString());
        outState.putString("Password",userPassword.getText().toString());
        outState.putString("RetypePassword",userRetypePassword.getText().toString());
        outState.putString("PhoneNo",userPhoneNumber.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        updateUI(savedInstanceState);

    }

    private void updateUI(Bundle savedInstanceState) {
        userFirstName.setText(savedInstanceState.getString("FirstName"));
        userLastName.setText(savedInstanceState.getString("LastName"));
        userEmail.setText(savedInstanceState.getString("Email"));
        userPassword.setText(savedInstanceState.getString("Password"));
        userRetypePassword.setText(savedInstanceState.getString("RetypePassword"));
        userPhoneNumber.setText(savedInstanceState.getString("PhoneNo"));
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
