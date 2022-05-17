package com.example.russian;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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


    @SuppressLint({"ResourceAsColor", "UseCompatTextViewDrawableApis"})
    @RequiresApi(api = Build.VERSION_CODES.M)
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
        final Integer[] user_coins = new Integer[1];
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
               // coins.setText(dataSnapshot.child("users").child(user.getUid()).child("coins").getValue().toString());
                user_coins[0] = Integer.parseInt(dataSnapshot.child("users").child(user.getUid()).child("coins").getValue().toString());
                coins.setText(user_coins[0].toString());
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
                    int finalNumber = number;
                    final Integer[] item_id = new Integer[1];
                    final Boolean[] selected = new Boolean[1];
                    DatabaseReference ref2=FirebaseDatabase.getInstance().getReference();

                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot snapshot) {
                            Object temp;
                            temp=snapshot.child("users").child(user.getUid()).child("items").child("item"+items.get(finalNumber).getID()).child("id").getValue();
                            Object temp_b=snapshot.child("users").child(user.getUid()).child("items").child("item" + items.get(finalNumber).getID()).child("selected").getValue();
                            if(temp!=null && temp_b!=null) {
                                item_id[0] = Integer.parseInt(temp.toString());
                                 selected[0] = (Boolean) temp_b;
                            if (item_id[0] == items.get(finalNumber).getID()) {
                                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_done);
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setText("");
                                    textView.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(ShopActivity.this,
                                            R.color.green)));
                                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                                }
                                if (selected[0] == true) {
                                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_selected);
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setText("");
                                    textView.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(ShopActivity.this,
                                            R.color.green)));
                                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                                }
                            }else
                                item_id[0]=0;


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });

                    textView.setOnClickListener(View->{
                         if(item_id[0] !=items.get(finalNumber).getID()){
                        Dialog dialog=new Dialog(this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_buy);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(false);
                        dialog.show();
                        TextView coins=dialog.findViewById(R.id.txt_coins_buy);
                        coins.setText(items.get(finalNumber).getCost().toString());
                        Button yes=dialog.findViewById(R.id.btn_yes);

                        yes.setOnClickListener(view -> {
                            if(user_coins[0]>=(items.get(finalNumber).getCost())){
                                user_coins[0]-=items.get(finalNumber).getCost();
                                ref.child("users").child(user.getUid()).child("coins").setValue(user_coins[0]);
                                textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.ic_selected);
                                textView.setGravity(Gravity.CENTER);
                                textView.setText("");
                                textView.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.green)));
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

                                ref.child("users").child(user.getUid()).child("items").child("item"+items.get(finalNumber).getID()).child("id").setValue(items.get(finalNumber).getID());
                                ref.child("users").child(user.getUid()).child("items").child("item"+items.get(finalNumber).getID()).child("selected").setValue(true);
                                dialog.cancel();

                            }else
                            {
                                TextView message=dialog.findViewById(R.id.txt_message_buy);
                                message.setText("Недостаточно монет");
                            }

                        });

                        Button no=dialog.findViewById(R.id.btn_no);
                        no.setOnClickListener(view -> {
                            dialog.cancel();
                        });
                        }else if(item_id[0] ==items.get(finalNumber).getID()) {
                            if(selected[0]==true){
                                textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.ic_done);
                                textView.setGravity(Gravity.CENTER);
                                textView.setText("");
                                textView.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.green)));
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                                ref.child("users").child(user.getUid()).child("items").child("item"+items.get(finalNumber).getID()).child("selected").setValue(false);

                            }
                            else{
                                textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.ic_selected);
                                textView.setGravity(Gravity.CENTER);
                                textView.setText("");
                                textView.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.green)));
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                                ref.child("users").child(user.getUid()).child("items").child("item"+items.get(finalNumber).getID()).child("selected").setValue(true);

                            }
                        }
                    });

/*   }
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