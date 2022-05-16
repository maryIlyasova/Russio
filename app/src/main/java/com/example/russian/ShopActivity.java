package com.example.russian;

import android.database.SQLException;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.russian.tables.Item;
import com.example.russian.utils.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {
    private TableLayout tableLayout;
    private int rows;
    private static int COLUMNS=3;
    private TextView coins;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        findViewById(R.id.btn_back_shop).setOnClickListener(this);
        tableLayout=findViewById(R.id.table);
        coins=(TextView)findViewById(R.id.text_coin_shop);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=mAuth.getCurrentUser();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                coins.setText(dataSnapshot.child("users").child(user.getUid()).child("coins").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });

        DataBaseHelper db = DataBaseHelper.getDB(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            db.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        List<Item> items=db.getAllContacts();
        rows=1;
        int number=0;
        int thirds_number=0;
        int k=0;
        tableLayout.setStretchAllColumns(true);
        for (int i = 0; i < rows; i++) {
            TableLayout.LayoutParams lpView = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            lpView.bottomMargin = 100;
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(lpView);

            for (int j = 0; j < COLUMNS; j++) {
                if(j<items.size()) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(getResources().getIdentifier(items.get(number).getName(), "drawable", getPackageName()));
                    tableRow.addView(imageView,new TableRow.LayoutParams(400,400,3f));
                    number++;
                }
            }
            tableLayout.addView(tableRow, k);
            k++;
            TableRow tableRow2 = new TableRow(this);

            tableRow2.setLayoutParams(lpView);
            number=thirds_number;
            for (int j = 0; j < COLUMNS; j++) {
                if(j<items.size()) {

                    TextView textView=new TextView(this);
                    textView.setText(items.get(number).getCost().toString());
                    textView.setGravity(Gravity.RIGHT);
                    textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_coin,0);
                    textView.setCompoundDrawablePadding(45);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    tableRow2.addView(textView,new TableRow.LayoutParams(40,130));
/*
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.ic_coin);
                    tableRow2.addView(imageView, new TableRow.LayoutParams(45,60));
                    tableRow2.setGravity(Gravity.CENTER);*/

                    number++;

                }
            }
            tableLayout.addView(tableRow2, k);
            k++;
            if(number<items.size()) {
                rows++;
                thirds_number+=3;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_shop)
        {
            onBackPressed();
        }
    }
}