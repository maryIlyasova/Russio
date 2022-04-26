package com.example.russian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView username;
    private TextView coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_reward).setOnClickListener(this);
        findViewById(R.id.btn_shop).setOnClickListener(this);
        findViewById(R.id.btn_user).setOnClickListener(this);
        username=findViewById(R.id.text_username);
        coins=(TextView)findViewById(R.id.text_coin);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=mAuth.getCurrentUser();
        String id=user.getUid();

        // Key
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
               username.setText(dataSnapshot.child("users").child(user.getUid()).child("name").getValue(String.class));
               coins.setText(dataSnapshot.child("users").child(user.getUid()).child("coins").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_start)
        {
            Intent intent = new Intent(HomeActivity.this,ClassActivity.class);
            startActivity(intent);
        }

    }
}