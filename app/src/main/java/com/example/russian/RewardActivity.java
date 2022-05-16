package com.example.russian;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.russian.tables.Reward;
import com.example.russian.utils.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RewardActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private FirebaseAuth mAuth;
    private final Long[] rewards_number ={(long)0};
    private final List<Long>[] rewards_id = new List[]{new ArrayList<>()};
    private  DataBaseHelper db;
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        findViewById(R.id.btn_back_reward).setOnClickListener(this);
        linearLayout=findViewById(R.id.linear_layout_reward);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=mAuth.getCurrentUser();

        ref=ref.child("users").child(user.getUid()).child("rewards");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             rewards_id[0] =collectPhoneNumbers((Map<String, Object>) dataSnapshot.getValue());
             onResume();// Retrieve latest value
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });
        db = DataBaseHelper.getDB(this);
        try {
            db.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onResume(){
        super.onResume();
        int number=rewards_id[0].size();
        //linearLayout.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpView.topMargin = 50;
        lpView.gravity= Gravity.CENTER;
        Reward reward = new Reward();
        for (int i = 0; i < number; i++) {
            reward=db.getRewardById(Math.toIntExact(rewards_id[0].get(i)));
            Button button=new Button(this);
            String name_unit=db.getUnit(reward.getIdUnit()).getName();
            button.setBackgroundResource(R.color.light_orange);
            button.setTextColor(ContextCompat.getColor(this,R.color.brown));
            button.setText(name_unit);
            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_medal,0,0,0);
            button.setCompoundDrawablePadding(20);
            button.setTextSize(17);
            Reward finalReward = reward;
            button.setOnClickListener((view -> {
                Dialog dialog=new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_reward);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
                TextView reward_name=dialog.findViewById(R.id.reward_name);
                reward_name.setText(finalReward.getName());
                TextView unit_name=dialog.findViewById(R.id.unit_name);
                unit_name.setText(name_unit);
                Button btn_ok_reward=dialog.findViewById(R.id.btn_ok_dialog_stat);
                btn_ok_reward.setOnClickListener(view1 -> {
                    dialog.cancel();
                });
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
    private ArrayList<Long> collectPhoneNumbers(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get("id"));
        }

        System.out.println(phoneNumbers.toString());
        return phoneNumbers;
    }
}