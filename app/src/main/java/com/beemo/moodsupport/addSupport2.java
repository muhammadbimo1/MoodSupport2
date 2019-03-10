package com.beemo.moodsupport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addSupport2 extends AppCompatActivity {

    private RadioGroup radioGroup;
    private TextSwitcher textSwitcher;
    private Button finishbutton;
    //private RadioGroup rg;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    String documentid;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "$NAME";
    private int count =0;
    TextView t ;
    TextView total;
    private String[] textArray = {
            "I'm good in listening to others' problems."
            ,"People often told me that i'm a caring person."//emotional
            ,"I like helping others' in need."
            ,"I am a generous person." //tangible
            ,"I am a knowledgable person."//
            ,"I am in the same program as him/her."//informational
            ,"I am often available whenever people needed me.",
            "i'm close to him/her physically.",//companionship
    };

    int[] scorearray = new int[textArray.length];
    int score;
    int emotional;
    int tangible;
    int informational;
    int companionship;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //radioGroup = (radioGroup)findViewById(R.id.selectionRG);
        radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton)findViewById(R.id.radioButton4);
        radioButton5 = (RadioButton)findViewById(R.id.radioButton5);
        radioButton6 = (RadioButton)findViewById(R.id.radioButton6);
        // Get the transferred data from source activity.
        Intent intent = getIntent();
        documentid = intent.getStringExtra("documentid");
        Toast.makeText(addSupport2.this, documentid, Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_support2);
        total=findViewById(R.id.total);
        total.setText(Integer.toString(scorearray.length));
        textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        t= findViewById(R.id.counter2);
        finishbutton = (Button) findViewById(R.id.Finish);
        finishbutton.setVisibility(View.GONE);
        textSwitcher.setCurrentText(textArray[count]);

        Animation textAnimationIn =  AnimationUtils.
                loadAnimation(this,   android.R.anim.slide_in_left);
        textAnimationIn.setDuration(800);

        Animation textAnimationOut =  AnimationUtils.
                loadAnimation(this,   android.R.anim.slide_out_right);
        textAnimationOut.setDuration(800);

        textSwitcher.setInAnimation(textAnimationIn);

        textSwitcher.setOutAnimation(textAnimationOut);
        radioGroup = (RadioGroup) findViewById(R.id.selectionRG);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                int selectedType;
                if (null != rb ) {
                    int check;
                    switch(checkedId)
                    {
                        case R.id.radioButton1:
                            score = 0;
                            break;
                        case R.id.radioButton2:
                            score = 1;
                            break;
                        case R.id.radioButton3:
                            score = 2;
                            break;
                        case R.id.radioButton4:
                            score = 3;
                            break;
                        case R.id.radioButton5:
                            score = 4;
                            break;
                        case R.id.radioButton6:
                            score = 5;
                            break;
                    }

                    //Toast.makeText(addSupport2.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(addSupport2.this, Integer.toString(score), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void showNextText(View view){
        if(score==-1){
            Toast.makeText(addSupport2.this, "Please choose!", Toast.LENGTH_SHORT).show();
        }else if(count<scorearray.length) {
            scorearray[count] = score;
            textSwitcher.setText(textArray[count]);
            t.setText(Integer.toString(count + 1));
            count++;
            radioGroup.clearCheck();
            score = -1;
            //Toast.makeText(DASSActivity.this, Integer.toString(scorearray[count]), Toast.LENGTH_SHORT).show();
        }
        else{
            finishbutton.setVisibility(View.VISIBLE);
        }
    }
    public void endpersonalitytest(View v) {
        emotional = scorearray[0]+scorearray[1];
        tangible = scorearray[2]+scorearray[3];
        informational = scorearray[4]+scorearray[5];
        companionship = scorearray[6]+scorearray[7];

        //database stuff
        //Date currentTime = Calendar.getInstance().getTime();
        Map<String, Object> oceandata = new HashMap<>();
        oceandata.put("emotional", emotional);
        oceandata.put("tangible", tangible);
        oceandata.put("informational", informational);
        oceandata.put("companionship", companionship);
        //oceandata.put("timestamp", FieldValue.serverTimestamp());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("supportlist").document(documentid);
        db.collection("users").document(uid).collection("supportlist").document(documentid).update(oceandata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot written with ID: " + docRef.getId());
                Toast.makeText(addSupport2.this, "done!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(addSupport2.this, MainMenu.class);
                startActivity(intent);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(addSupport2.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });;
        //done
        //depression: 3,5,10,13,16,17,21
        //anxiety:2,4,7,9,15,18,19,20
        //stress: 1,6,8,11,12,14,18
        // add!
        //todo : finished
    }
}