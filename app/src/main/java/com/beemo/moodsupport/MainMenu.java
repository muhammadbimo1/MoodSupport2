package com.beemo.moodsupport;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {
    private static final String TAG = "$NAME";
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView name ;
    //TextView hobby ;
    //private FirebaseFirestore db;
    CollectionReference collectionReference ;
    DocumentReference documentReference;
    BarChart barChart;
    List<Mood> moods = new ArrayList<>();
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name   = (TextView) findViewById(R.id.name);
                        //hobby = (TextView) findViewById(R.id.hobby);
                       name.setText(document.getString("name"));
                       //hobby.setText(document.getString("hobby"));
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
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainMenu.this, LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "Log Out!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };

        moodgraph();
    }


    public void logOut(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainMenu.this, LoginActivity.class));
        // this listener will be called when there is change in firebase user session

// this listener will be called when there is change in firebase user session
    }
    public void Dass(View v) {
        startActivity(new Intent(MainMenu.this, DASSActivity.class));
    }

    public void ocean(View v) {
        startActivity(new Intent(MainMenu.this, OCEANActivity.class));
    }
    public void mood(View v){
        startActivity(new Intent(MainMenu.this, MoodActivity.class));
    }
    public void addsupport(View v){
        startActivity(new Intent(MainMenu.this, AddSupport.class));
    }
    public void graph(View v){
        startActivity(new Intent(MainMenu.this, MoodGraph.class));
    }
    public void supportrecieve(View v){
        startActivity(new Intent(MainMenu.this, Recievesupport.class));
    }

    public void profilemenu(View v){
        startActivity(new Intent(MainMenu.this, MainProfileActivity.class));
    }

    public void selfhealing(View v){
        startActivity(new Intent(MainMenu.this, SelfHealingActivity.class));
    }


    private void moodgraph(){
        //db = FirebaseFirestore.getInstance();
        barChart = findViewById(R.id.chart);
        barChart.setDrawBarShadow(true);
        //barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(15);
        barChart.setTouchEnabled(false);
        barChart.setDrawGridBackground(true);
        barChart.setNoDataText("Loading...");
        final ArrayList<BarEntry> barEntries = new ArrayList<>();
        collectionReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("mood");
        documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("mood").document();
        Query moodquery = collectionReference.orderBy("timestamp", Query.Direction.DESCENDING).limit(15);
        moodquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Mood mood = document.toObject(Mood.class);
                        //int moodval = document.getLong("mood").intValue();
                        //Date date = document.getDate("timestamp");
                        moods.add(mood);
                    }
                    //graph
                    for(int i=0;i<moods.size();i++){
                        barEntries.add(new BarEntry(i,moods.get(i).getMood()));
                        Toast.makeText(MainMenu.this, Integer.toString(moods.get(i).getMood()), Toast.LENGTH_SHORT).show();
                    }
                    BarDataSet barDataSet = new BarDataSet(barEntries,"date");
                    BarData data = new BarData(barDataSet);
                    data.setDrawValues(false);
                    data.setBarWidth(0.9f);

                    barChart.setData(data);

                    barChart.getLegend().setEnabled(false);
                    barChart.getXAxis().setDrawLabels(false);
                    barChart.getAxisLeft().setDrawLabels(false);
                    barChart.getAxisRight().setDrawLabels(false);
                    barChart.animateY(500, Easing.EaseOutSine);
                }
                else {
                    Log.d("PUCK", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}