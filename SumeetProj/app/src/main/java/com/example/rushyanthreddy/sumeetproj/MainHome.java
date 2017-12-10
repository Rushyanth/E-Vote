package com.example.rushyanthreddy.sumeetproj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rushyanth Reddy on 12/10/2017.
 */

public class MainHome extends Activity {
    Button poll,logout;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView yesC,noC,totalC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        poll = findViewById(R.id.poll_button);
        logout = findViewById(R.id.button_logout);
        yesC =findViewById(R.id.yes_poll_number_textview);
        noC = findViewById(R.id.no_poll_number_textview);
        totalC = findViewById(R.id.total_poll_number_textview);

        final long[] x1 = new long[1];
        final long[] x2 = new long[1];

        mDatabase.child("POLL").child("YES").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long x = (long)dataSnapshot.getValue();
                x1[0] =x;
                yesC.setText(String.valueOf(x));
                totalC.setText(String.valueOf(x1[0]+x2[0]));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("POLL").child("NO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long x = (long)dataSnapshot.getValue();
                x2[0]=x;
                noC.setText(String.valueOf(x));
                totalC.setText(String.valueOf(x1[0]+x2[0]));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainHome.this,PollPage.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(MainHome.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
