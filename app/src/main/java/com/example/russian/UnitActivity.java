package com.example.russian;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.russian.tables.Unit;
import com.example.russian.utils.DataBaseHelper;

import java.util.List;

public class UnitActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private TextView class_name;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        savedInstanceState=getIntent().getExtras();
        String class_number=savedInstanceState.get("number").toString();
        linearLayout=findViewById(R.id.linear_layout);
        findViewById(R.id.btn_back_reward).setOnClickListener(this);
        class_name=findViewById(R.id.text_class_title);
        class_name.setText(class_number+" КЛАСС");

        DataBaseHelper db = DataBaseHelper.getDB(this);
        try {
            db.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        List<Unit> units=db.getUnitsByClassID(class_number);
        int number=units.size();
        //linearLayout.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(1200, 200);
        lpView.topMargin = 50;
        lpView.gravity= Gravity.CENTER;
        for (int i = 0; i < number; i++) {
            Button button=new Button(this);
            button.setText(units.get(i).getName());
            button.setBackgroundResource(R.color.brown);
            button.setTextColor(ContextCompat.getColor(this,R.color.light_orange));
            button.setTextSize(17);
            int id=units.get(i).getID();
            button.setOnClickListener((view -> {
                Intent intent=new Intent(UnitActivity.this,TaskActivity.class);
                intent.putExtra("unit_id",id);
                intent.putExtra(("class_id"),class_number);
                startActivity(intent);
            }));
            //button.setGravity(Gravity.CENTER);
            linearLayout.addView(button,lpView);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_reward)
        {
            onBackPressed();
        }
    }
}