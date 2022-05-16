package com.example.russian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ClassActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        findViewById(R.id.btn_back_class).setOnClickListener(this);
        findViewById(R.id.btn_class).setOnClickListener(this);
        findViewById(R.id.btn_class2).setOnClickListener(this);
        findViewById(R.id.btn_class3).setOnClickListener(this);
        findViewById(R.id.btn_class4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_class)
        {
            onBackPressed();
        }
        if(view.getId() == R.id.btn_class)
        {
            Intent intent = new Intent(ClassActivity.this,UnitActivity.class);
            intent.putExtra("number",1);
            startActivity(intent);
        }
        if(view.getId() == R.id.btn_class2)
        {
            Intent intent = new Intent(ClassActivity.this,UnitActivity.class);
            intent.putExtra("number",2);
            startActivity(intent);

        }
        if(view.getId() == R.id.btn_class3)
        {
            Intent intent = new Intent(ClassActivity.this,UnitActivity.class);
            intent.putExtra("number",3);
            startActivity(intent);

        }
        if(view.getId() == R.id.btn_class4)
        {
            Intent intent = new Intent(ClassActivity.this,UnitActivity.class);
            intent.putExtra("number",4);
            startActivity(intent);

        }

    }
}