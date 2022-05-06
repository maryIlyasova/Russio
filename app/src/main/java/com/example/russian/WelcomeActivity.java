package com.example.russian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;
    private FirebaseUser user;
    private EditText ETname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ETname = (EditText) findViewById(R.id.et_name);
        findViewById(R.id.btn_ok).setOnClickListener((View.OnClickListener) this);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference();
        // Key
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_ok)
        {
            sendName(ETname.getText().toString());
        }

    }

    public void sendName(String name)
    {
                if(name!=null) {
                    Toast.makeText(WelcomeActivity.this, "Вход выполнен", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WelcomeActivity.this,HomeActivity.class);
                    startActivity(intent);
                    ref.child("users").child(user.getUid()).child("name").setValue(name);
                    ref.child("users").child(user.getUid()).child("coins").setValue(0);
                    ref.child("users").child(user.getUid()).child("name").setValue(name);
                    ref.child("users").child(user.getUid()).child("email").setValue(user.getEmail());
                }else
                    Toast.makeText(WelcomeActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();
        }
    }
