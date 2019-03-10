package com.beemo.moodsupport;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recievesupport3 extends AppCompatActivity {
    //List<Integer> requestlist = new ArrayList<>();
    List<Support> eligible = new ArrayList<>();
    int emotional;
    int tangible;
    int informational;
    int companionship;
    boolean prioritize_strongtie;
    //TextView emotext;
    //TextView tangitext;
    //TextView infotext;
    //TextView comptext;
    //db stuff
    private FirebaseFirestore db;
    CollectionReference collectionReference ;
    DocumentReference documentReference;
    List<Support> supports = new ArrayList<>();
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    //String[] names = new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recievesupport3);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        db = FirebaseFirestore.getInstance();
        emotional = extras.getInt("emotional");
        tangible = extras.getInt("tangible");
        informational = extras.getInt("informational");
        companionship = extras.getInt("companionship");
        //see which one the most needed
        //int max = 0;
        //if(emotional>4){
        //    max=emotional;
        //}
        //if

        //db
        collectionReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("supportlist");
        documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("supportlist").document();
        Query supportquery = collectionReference.orderBy("burden", Query.Direction.DESCENDING);
        supportquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        //Support support = document.toObject(Support.class);
                        String emailval = document.getString("email");
                        String idval = document.getId();
                        String nameval = document.getString("name");
                        int burdenval = document.getLong("burden").intValue();
                        int emotionalval = document.getLong("emotional").intValue();;
                        int tangibleval = document.getLong("tangible").intValue();;
                        int informationalval = document.getLong("informational").intValue();;
                        boolean closetieval = document.getBoolean("closetie");
                        int companionshipval = document.getLong("companionship").intValue();
                        Support support = new Support(emailval,idval,nameval,burdenval,emotionalval,tangibleval,informationalval,companionshipval,closetieval);
                        //int moodval = document.getLong("mood").intValue();
                        //Date date = document.getDate("timestamp");
                        supports.add(support);
                    }
                    double judges[] = new double[supports.size()];
                    //int [] listofrequests = {emotional,tangible,informational,companionship};


                    //for(int i = 0; i < listofrequests.length; i++) {
                    //    if (max < listofrequests[i]) {
                    //        max = listofrequests[i];
                    //    }
                    //}
                    //emotext.setText(Integer.toString(supports.size()));

                    if(emotional>4){
                        //double max2 = 0.0;
                        //emotional code goes here
                        for(int i=0;i<supports.size();i++){
                            judges [i] = (double)supports.get(i).getEmotional()/emotional;
                            if(judges [i]>0.8&& supports.get(i).isClosetie()&&supports.get(i).getBurden()<50){
                                eligible.add(supports.get(i));
                            }
                        }
                    }
                    if(tangible>4){
                        for(int i=0;i<supports.size();i++){
                            judges [i] = (double)supports.get(i).getTangible()/tangible;
                            if(judges [i]>0.8&&supports.get(i).getBurden()<50){
                                eligible.add(supports.get(i));
                            }
                        }
                    }
                    if(informational>4){
                        for(int i=0;i<supports.size();i++){
                            judges [i] = (double)supports.get(i).getInformational()/informational;
                            if(judges [i]>0.8&&supports.get(i).getBurden()<50){
                                eligible.add(supports.get(i));
                            }
                        }
                    }
                    if(companionship>4){
                        for(int i=0;i<supports.size();i++){
                            judges [i] = (double)supports.get(i).getCompanionship()/companionship;
                            if(judges [i]>0.8&& supports.get(i).isClosetie()==true&&supports.get(i).getBurden()<50){
                                eligible.add(supports.get(i));
                            }
                        }
                    }

                    //add the support to list
                    ListView list = (ListView) findViewById(R.id.supportlist);
                    ArrayAdapter adapter = new SupportListAdapter(Recievesupport3.this, R.layout.list_support,eligible);
                    list.setAdapter(adapter);
                    // Set The Adapter


                }
                else {
                    Log.d("PUCK", "Error getting documents: ", task.getException());
                }
            }
        });



        //emotext   = (TextView) findViewById(R.id.textView7);
        //tangitext = (TextView) findViewById(R.id.textView8);
        //infotext = (TextView) findViewById(R.id.textView9);
        //comptext = (TextView) findViewById(R.id.textView10);
        //check whether it's enough to warrant a support
        if(emotional<5&&tangible<5&&informational<5&&companionship<5){
            //cancel support
        }


        if(companionship>7&&emotional>7){
           prioritize_strongtie = true;
        }
        else {
            prioritize_strongtie = false;
        }

        //emotext.setText(Integer.toString());
        //tangitext.setText(Integer.toString(tangible));
        //infotext.setText(Integer.toString(informational));
        //comptext.setText(Integer.toString(companionship));
    }
    public void sendsupport(View v){
        //String[] TO = {"someone@gmail.com"};
        String[] TO = new String[eligible.size()];
        for(int i = 0;i<eligible.size();i++){
            TO[i] = eligible.get(i).getEmail();
        }

        /*for(int i = 0;i<eligible.size();i++){
            eligible.get(i).setBurden(eligible.get(i).getBurden()+10);
        }


        for(int i = 0;i<eligible.size();i++){
            Map<String, Object> burdendata = new HashMap<>();
            burdendata.put("burden",eligible.get(i).getBurden());
            //final DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("supportlist").document(documentid);
            db.collection("users").document(uid).collection("supportlist").document(eligible.get(i).getId()).update(burdendata).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Log.d(TAG, "DocumentSnapshot written with ID: " + docRef.getId());
                    Toast.makeText(Recievesupport3.this, "added burden!", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(addSupport2.this, MainMenu.class);
                    //startActivity(intent);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(Recievesupport3.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });;
        }*/


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MoodSupport Tracking");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "hi, this is an automatic message from MoodSupport. Your friend is having a hard time lately, mind checking out?");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            //startActivity(new Intent(Recievesupport3.this, MainMenu.class));
            Log.i("Finished.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Recievesupport3.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }
    public void backToMenu(View v){
        startActivity(new Intent(Recievesupport3.this, MainMenu.class));
    }
}
