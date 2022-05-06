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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView username;
    private TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.btn_back_profile).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        username=findViewById(R.id.text_username_profile);
        email=findViewById(R.id.text_email);
        FirebaseUser user=mAuth.getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        email.setText(user.getEmail());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                username.setText(dataSnapshot.child("users").child(user.getUid()).child("name").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_profile)
        {

            Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        if(view.getId() == R.id.btn_exit)
        {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this,EmailPasswordActivity.class);
            startActivity(intent);
            finish();
        }

    }
}