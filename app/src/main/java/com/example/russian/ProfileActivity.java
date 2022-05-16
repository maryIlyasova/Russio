package com.example.russian;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView username;
    private TextView email;
    private Button statistics;
    private GraphView graphView;
    private final List<Long>[] tasks_score = new List[]{new ArrayList<>()};

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
        DatabaseReference ref2= FirebaseDatabase.getInstance().getReference();
        ref2=ref2.child("users").child(user.getUid()).child("tasks");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                tasks_score[0] =collectPhoneNumbers((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error handling
            }
        });
        statistics=findViewById(R.id.btn_statistics);

    }
@Override
    protected void onResume(){
        super.onResume();
    statistics.setOnClickListener(View->{
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_statistics);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        graphView = dialog.findViewById(R.id.idGraphView);

        // on below line we are adding data to our graph view.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(generateData());

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        series.setColor(ContextCompat.getColor(this,R.color.blue));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series.setThickness(20);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setMaxY(10);
        graphView.getViewport().setMaxX(tasks_score[0].size()+1);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Номер упражнения");
        gridLabel.setVerticalAxisTitle("Оценка");
        gridLabel.setVerticalAxisTitleColor(ContextCompat.getColor(this,R.color.red));
        gridLabel.setHorizontalAxisTitleColor(ContextCompat.getColor(this,R.color.red));
        gridLabel.setHorizontalAxisTitleTextSize(60);
        gridLabel.setVerticalAxisTitleTextSize(60);
        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(70);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
        dialog.findViewById(R.id.btn_ok_dialog_stat).setOnClickListener(view -> {
            dialog.cancel();
        });
    });

}

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_profile)
        {
            onBackPressed();
        }
        if(view.getId() == R.id.btn_exit)
        {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this,EmailPasswordActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private ArrayList<Long> collectPhoneNumbers(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get("score"));
        }

        System.out.println(phoneNumbers.toString());
        return phoneNumbers;
    }
    private DataPoint[] generateData() {
        int count = tasks_score[0].size();
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i+1;
            double y = tasks_score[0].get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
}