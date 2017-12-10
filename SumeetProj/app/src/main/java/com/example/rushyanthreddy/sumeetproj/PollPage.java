package com.example.rushyanthreddy.sumeetproj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Rushyanth Reddy on 12/10/2017.
 */

public class PollPage extends Activity {
    Button yes,no;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_activity);

        yes =(Button)findViewById(R.id.yes_button);
        no = (Button)findViewById(R.id.no_button);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();

                //mDatabase.child("POLL").child("YES").setValue(0);
                final long[] x = {0};
                mDatabase.child("POLL").child("YES").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long yesVal = (long) dataSnapshot.getValue();
                        Log.d("aaaaaaaaaaa",String.valueOf(yesVal));
                        x[0] =yesVal;
                        x[0]++;
                        Log.d("aaaaaaaaaaa",String.valueOf(x[0]));
                        mDatabase.child("POLL").child("YES").setValue(x[0]);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent i = new Intent();
                i.setClass(PollPage.this,MainHome.class);
                startActivity(i);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference();

                //mDatabase.child("POLL").child("NO").setValue(0);
                final long[] x = {0};
                mDatabase.child("POLL").child("NO").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long noVal = (long) dataSnapshot.getValue();
                        Log.d("sakljklksl",String.valueOf(noVal));
                        x[0] =noVal;
                        x[0]++;
                        Log.d("sakljklksl",String.valueOf(x[0]));
                        mDatabase.child("POLL").child("NO").setValue(x[0]);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent i = new Intent();
                i.setClass(PollPage.this,MainHome.class);
                startActivity(i);
            }
        });

    }
}
