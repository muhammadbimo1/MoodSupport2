package com.beemo.moodsupport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    DiscreteSeekBar discreteSeekBar1;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "$NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
    }
    public void moodTestFinish(View v) {
        discreteSeekBar1 = findViewById(R.id.discrete1);
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
