package com.example.russian;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.russian.tables.Reward;
import com.example.russian.tables.Task;
import com.example.russian.tables.Unit;
import com.example.russian.tables.Word;
import com.example.russian.utils.DataBaseHelper;
import com.example.russian.utils.RandomGenerationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {
    private String unit_id;
    private String class_id;
    private TextView txt_condition;
    private LinearLayout taskLayout;
    private LinearLayout buttonLayout;
    private ImageView fox;
    private  LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private  AtomicInteger counter= new AtomicInteger();
    private Integer correct_tasks=0;
    private DatabaseReference ref;
    private FirebaseUser user;
    private final Integer[] coins = {0};
    private final Long[] tasks_number = {(long)0};
   private final Long[] rewards_number ={(long)0};

   private List<TextView> points;
   private  DataBaseHelper db;
   private  List<Word> words;
   private Dialog dialog;
   private List<Task> tasks;
   private Reward possible_reward;
    private Unit unit;
    private List<Word> temp_words;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        savedInstanceState=getIntent().getExtras();
        unit_id=savedInstanceState.get("unit_id").toString();
        class_id=savedInstanceState.get("class_id").toString();
        findViewById(R.id.btn_back_task).setOnClickListener(this);
        txt_condition=findViewById(R.id.txt_condition);
        taskLayout=findViewById(R.id.task_layout);
        taskLayout.setGravity(Gravity.CENTER);
        buttonLayout=findViewById(R.id.button_layout);
        buttonLayout.setGravity(Gravity.CENTER);
        lpView.rightMargin = 50;
        fox=findViewById(R.id.fox_task);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coins[0]=Integer.parseInt(snapshot.child("users").child(user.getUid()).child("coins").getValue().toString());
                tasks_number[0]=snapshot.child("users").child(user.getUid()).child("tasks").getChildrenCount();
                rewards_number[0]=snapshot.child("users").child(user.getUid()).child("rewards").child("reward"+unit_id).getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        points=new ArrayList<TextView>();
        points.add(findViewById(R.id.task1));
        points.add(findViewById(R.id.task2));
        points.add(findViewById(R.id.task3));
        points.add(findViewById(R.id.task4));
        points.add(findViewById(R.id.task5));
        points.add(findViewById(R.id.task6));
        points.add(findViewById(R.id.task7));
        points.add(findViewById(R.id.task8));
        points.add(findViewById(R.id.task9));
        points.add(findViewById(R.id.task10));

      db = DataBaseHelper.getDB(this);
        try {
            db.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
       words=db.getWordsByIdUnit(unit_id);
        tasks=db.getAllTasks();
        possible_reward=db.getRewardByIdUnit(Integer.parseInt(unit_id));
        unit=db.getUnit(Integer.parseInt(unit_id));
        temp_words=new ArrayList<Word>();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onResume(){
        super.onResume();

        if(counter.get() <5)
        {
            if(counter.get()<1)
                temp_words =getWordsByIdTask(words,tasks.get(0).getID());
            txt_condition.setText(tasks.get(0).getCondition());
            Integer number= RandomGenerationUtils.generateRandomNumber(temp_words.size());
            String[] letters =temp_words.get(number).getTaskValue().split("\\.");
            TextView textView=new TextView(this);
            textView.setText(letters[0]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            taskLayout.addView(textView);

            EditText editText=new EditText(this);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
            taskLayout.addView(editText);

            TextView textView2=new TextView(this);
            textView2.setText(letters[1]);
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            taskLayout.addView(textView2);

            Button button=new Button(this);
            button.setText("Готово");
            button.setBackgroundResource(R.color.brown);
            button.setTextColor(ContextCompat.getColor(this,R.color.light_orange));
            button.setTextSize(18);

            button.setOnClickListener((view -> {
                String answer=letters[0]+editText.getText()+letters[1];
                if(answer.equals(temp_words.get(number).getCorrectValue())){
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_green));
                    correct_tasks++;
                    fox.setImageResource(R.drawable.ic_winking_fox);

                }else{
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_red));
                    fox.setImageResource(R.drawable.ic_oops_fox);
                }
                counter.getAndIncrement();
                temp_words.remove(temp_words.get(number));
                taskLayout.removeView(textView);
                taskLayout.removeView(textView2);
                taskLayout.removeView(editText);
                buttonLayout.removeView(button);
                onResume();

            }));
            if(counter.get()<5)
                buttonLayout.addView(button);
        }

        if(counter.get()>4 && counter.get()<9){
            if(correct_tasks<3)
                fox.setImageResource(R.drawable.ic_dissatisfied_fox);
            Animation anim= AnimationUtils.loadAnimation(this, R.anim.wait);
            if(counter.get()<6) {
                temp_words.clear();
                temp_words =getWordsByIdTask(words,tasks.get(1).getID());}
            txt_condition.setText(tasks.get(1).getCondition());
            Integer number= RandomGenerationUtils.generateRandomNumber(temp_words.size());
            String[] letters =temp_words.get(number).getTaskValue().split(",");

            Button btn_left=new Button(this);
            Integer numb=RandomGenerationUtils.generateRandomNumber(2);
            btn_left.setText(letters[numb]);
            btn_left.setBackgroundResource(R.color.brown);
            btn_left.setTextColor(ContextCompat.getColor(this,R.color.light_orange));
            btn_left.setTextSize(18);
            Button btn_right=new Button(this);
            numb++;
            if(numb==2)
                numb=0;
            btn_right.setText(letters[numb]);
            btn_right.setBackgroundResource(R.color.brown);
            btn_right.setTextColor(ContextCompat.getColor(this,R.color.light_orange));
            btn_right.setTextSize(18);
            btn_left.setOnClickListener((view -> {
                if(btn_left.getText().equals(temp_words.get(number).getCorrectValue())){
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_green));
                    btn_left.setBackground(getDrawable(R.drawable.style_points_green));
                    btn_right.setBackground(getDrawable(R.drawable.style_points_red));
                    anim.start();
                    correct_tasks++;
                    fox.setImageResource(R.drawable.ic_winking_fox);
                }else{
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_red));
                    btn_left.setBackground(getDrawable(R.drawable.style_points_red));
                    btn_right.setBackground(getDrawable(R.drawable.style_points_green));
                    anim.start();
                    fox.setImageResource(R.drawable.ic_astonished_fox);
                }
                counter.getAndIncrement();
                onPause();
                temp_words.remove(temp_words.get(number));
                taskLayout.removeView(btn_left);
                taskLayout.removeView(btn_right);
                onResume();
            }));
            if(counter.get()<9)
            taskLayout.addView(btn_left,lpView);


            btn_right.setOnClickListener((view -> {
                if(btn_right.getText().equals(temp_words.get(number).getCorrectValue())){
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_green));
                    btn_right.setBackground(getDrawable(R.drawable.style_points_green));
                    btn_left.setBackground(getDrawable(R.drawable.style_points_red));
                    fox.setImageResource(R.drawable.ic_winking_fox);
                    correct_tasks++;
                }else{
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_red));
                    btn_right.setBackground(getDrawable(R.drawable.style_points_red));
                    btn_right.setBackground(getDrawable(R.drawable.style_points_green));
                    fox.setImageResource(R.drawable.ic_astonished_fox);
                }
                counter.getAndIncrement();
                onPause();
                temp_words.remove(temp_words.get(number));
                taskLayout.removeView(btn_right);
                taskLayout.removeView(btn_left);
                onResume();
            }));
            if(counter.get()<9)
            taskLayout.addView(btn_right);
        }

        if(counter.get()>8 && counter.get()<10){
            if(correct_tasks<2)
                fox.setImageResource(R.drawable.ic_sad_fox);
            if(correct_tasks>7)
                fox.setImageResource(R.drawable.ic_smiling_fox);
            temp_words.clear();
            temp_words =getWordsByIdTask(words,tasks.get(2).getID());
            txt_condition.setText(tasks.get(2).getCondition());
            Integer number= RandomGenerationUtils.generateRandomNumber(temp_words.size());
            String[] letters =temp_words.get(number).getTaskValue().split(",");
            taskLayout.setOrientation(LinearLayout.VERTICAL);
            TextView textView=new TextView(this);
            textView.setText(letters[0]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            taskLayout.addView(textView);


                LinearLayout layout=new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                taskLayout.addView(layout);
                TextView textView1=new TextView(this);
                textView1.setText(letters[1]+" - ");
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                layout.addView(textView1);
                EditText editText=new EditText(this);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                layout.addView(editText);

            LinearLayout layout2=new LinearLayout(this);
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            taskLayout.addView(layout2);
            TextView textView2=new TextView(this);
            textView2.setText(letters[2] +" - ");
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            layout2.addView(textView2);
            EditText editText2=new EditText(this);
            editText2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            editText2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
            layout2.addView(editText2);

            LinearLayout layout3=new LinearLayout(this);
            layout3.setOrientation(LinearLayout.HORIZONTAL);
            taskLayout.addView(layout3);
            TextView textView3=new TextView(this);
            textView3.setText(letters[3]+" - ");
            textView3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            layout3.addView(textView3);
            EditText editText3=new EditText(this);
            editText3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            editText3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
            layout3.addView(editText3);


            Button button=new Button(this);
            button.setText("Готово");
            button.setBackgroundResource(R.color.brown);
            button.setTextColor(ContextCompat.getColor(this,R.color.light_orange));
            button.setTextSize(18);
            List<Word> finalTemp_words = temp_words;
            button.setOnClickListener((view -> {
                String result=editText.getText()+","+editText2.getText()+","+editText3.getText();
                if(result.equals(finalTemp_words.get(number).getCorrectValue())){
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_green));
                    correct_tasks++;
                }else{
                    points.get(counter.get()).setBackground(getDrawable(R.drawable.style_points_red));
                    fox.setImageResource(R.drawable.ic_questoning_fox);
                }
                counter.getAndIncrement();
                onPause();
                taskLayout.removeView(layout);
                taskLayout.removeView(layout2);
                taskLayout.removeView(textView);
                taskLayout.removeView(layout3);
                buttonLayout.removeView(button);
                onResume();

            }));
            if(counter.get()<10)
                buttonLayout.addView(button);


        }
        if(counter.get()==10){
            if(correct_tasks==10)
                fox.setImageResource(R.drawable.ic_fox_in_love);
            if(correct_tasks==0)
                fox.setImageResource(R.drawable.ic_tired_fox);

            ValueEventListener valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    coins[0] =Integer.parseInt(snapshot.child("users").child(user.getUid()).child("coins").getValue().toString());
                    tasks_number[0] =snapshot.child("users").child(user.getUid()).child("tasks").getChildrenCount();
                    rewards_number[0] =snapshot.child("users").child(user.getUid()).child("rewards").getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
           // coins[0]=
            coins[0]+=correct_tasks;
            //tasks_number[0]=ref.get().getResult().child("users").child(user.getUid()).child("tasks").getChildrenCount();
            ref.child("users").child(user.getUid()).child("coins").setValue(coins[0]);
            tasks_number[0]++;
            String new_task="task"+ tasks_number[0];
            //ref.child("users").child(user.getUid()).child("tasks").setValue("task"+tasks_number[0]);
            ref.child("users").child(user.getUid()).child("tasks").child(new_task).child("id").setValue(unit_id);
            ref.child("users").child(user.getUid()).child("tasks").child(new_task).child("score").setValue(correct_tasks);

                dialog=new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_results);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                TextView correct_results=dialog.findViewById(R.id.txt_correct_number);
                correct_results.setText(correct_tasks.toString());
                TextView coins_number=dialog.findViewById(R.id.txt_coins_number);
                coins_number.setText(correct_tasks.toString());
                TextView message=dialog.findViewById(R.id.txt_message);
                ImageView fox_dialog=dialog.findViewById(R.id.img_fox_dialog);
                if(correct_tasks==10){
                    message.setText("Вау! Ты справился с заданием на отлично!");
                    fox_dialog.setImageResource(R.drawable.ic_fox_in_love);
                }
            dialog.show();
                Button ok_btn=dialog.findViewById(R.id.btn_ok_dialog_result);
                ok_btn.setOnClickListener(view -> {
                    if(correct_tasks==10 && rewards_number[0]==0){
                        String new_reward="reward"+possible_reward.getID();
                        ref.child("users").child(user.getUid()).child("rewards").child(new_reward).child("id").setValue(possible_reward.getID());
                        dialog.setContentView(R.layout.dialog_reward);
                        TextView reward_name=dialog.findViewById(R.id.reward_name);
                        reward_name.setText(possible_reward.getName());
                        TextView unit_name=dialog.findViewById(R.id.unit_name);
                        unit_name.setText(unit.getName());
                        Button btn_ok_reward=dialog.findViewById(R.id.btn_ok_dialog_stat);
                        btn_ok_reward.setOnClickListener(view1 -> {
                            Intent intent = new Intent(TaskActivity.this, UnitActivity.class);
                            intent.putExtra("number", class_id);
                            startActivity(intent);
                            finish();
                        });
                    }else {
                        onBackPressed();
                    }
                });


        }


    }
    private List<Word> getWordsByIdTask(List<Word> words,Integer idTask){
        List<Word> wordList=new ArrayList<Word>();
        for(int i=0;i<words.size();i++)
        {

            if(words.get(i).getIdTask()==idTask)
                wordList.add(words.get(i));
        }
        return wordList;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_task)
        {
            onBackPressed();
        }
    }
}