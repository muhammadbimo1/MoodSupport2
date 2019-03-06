package com.beemo.moodsupport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    EditText name;
    EditText hobby;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "$NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        /*todo:
                - add personality test
                - add DASS test

         */

    }
    public void addProfile(View v){
        name   = (EditText)findViewById(R.id.name);
        hobby = (EditText)findViewById(R.id.hobby);
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef
                .update("name", name.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot name successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        docRef.update("hobby",hobby.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot hobby successfully updated!");
            }
        });
        docRef.update("done",true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot done mark successfully updated! to true");
            }
        });
        startActivity(new Intent(ProfileActivity.this, MainMenu.class));
    }
}
