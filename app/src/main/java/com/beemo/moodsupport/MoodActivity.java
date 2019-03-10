package com.beemo.moodsupport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.HashMap;
import java.util.Map;

public class MoodActivity extends AppCompatActivity {
    int mood;
    int moodval;
    DiscreteSeekBar discreteSeekBar1;
    ImageView simpleImageView;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "$NAME";
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        discreteSeekBar1 = findViewById(R.id.discrete1);
        simpleImageView = findViewById(R.id.moodimage);

        t1 = findViewById(R.id.moodtext);
        discreteSeekBar1.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                moodval = discreteSeekBar1.getProgress();
                if (0 < value && value < 2){
                    t1.setText("Really terrible");
                    simpleImageView.setImageResource(R.drawable.terrible);
                }
                if(3 < value && value < 5){
                    t1.setText("Somewhat bad");
                    simpleImageView.setImageResource(R.drawable.bad);
                }
                if(4 < value && value < 6){
                    t1.setText("Perfectly okay");
                    simpleImageView.setImageResource(R.drawable.okay);
                }
                if(7 < value && value < 9){
                    t1.setText("Pretty good");
                    simpleImageView.setImageResource(R.drawable.good);
                }
                if(9 < value && value < 11){
                    t1.setText("Awesome!");
                    simpleImageView.setImageResource(R.drawable.awesome);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

    }
    public void moodTestFinish(View v) {
        mood = discreteSeekBar1.getProgress();
        Map<String, Object> oceandata = new HashMap<>();
        oceandata.put("mood", mood);
        oceandata.put("timestamp", FieldValue.serverTimestamp());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid).collection("mood").add(oceandata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                Toast.makeText(MoodActivity.this, "done!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });;
        Toast.makeText(getApplicationContext(), Integer.toString(mood), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MoodActivity.this, MainMenu.class));
    }
}
