package com.beemo.moodsupport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "$NAME";
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //check whether the document even exist
                        if (document.getBoolean("done") == true) {
                            //if profile is created, send it to mainmenu
                            startActivity(new Intent(MainActivity.this, MainMenu.class));
                        } else {
                            //if not, create profile
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        }
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Toast.makeText(this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        //       @Override
        //       public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //        FirebaseUser user = firebaseAuth.getCurrentUser();
        //        if (user == null) {
        // user auth state is changed - user is null
        // launch login activity
        //            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        //            Toast.makeText(getApplicationContext(), "Log Out!", Toast.LENGTH_SHORT).show();
        //            finish();
        //        }
        //    }
        //};
        //}
        //public void logOut (View v){
        //    FirebaseAuth.getInstance().signOut();
        //    startActivity(new Intent(MainActivity.this, LoginActivity.class));
            // this listener will be called when there is change in firebase user session

// this listener will be called when there is change in firebase user session
        }
    }
