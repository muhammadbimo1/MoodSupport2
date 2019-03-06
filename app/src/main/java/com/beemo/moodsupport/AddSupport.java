package com.beemo.moodsupport;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddSupport extends AppCompatActivity {
    int emotional;
    int tangible;
    int informational;
    int companionship;
    DiscreteSeekBar discreteSeekBar1;
    DiscreteSeekBar discreteSeekBar2;
    DiscreteSeekBar discreteSeekBar3;
    DiscreteSeekBar discreteSeekBar4;
    private RadioGroup radioGroup;
    String namestring = "";
    RadioButton radioButton1;
    RadioButton radioButton2;
    TextView phoneno;
    private EditText name;
    //private EditText contactno;
    private final static int REQUEST_CONTACTPICKER = 1;
    boolean closetie = false;
    String numberphone = "";

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "$NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_support);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        name = (EditText) findViewById(R.id.name);
        //radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                int selectedType;
                if (null != rb) {
                    int check;
                    switch (checkedId) {
                        case R.id.radioButton:
                            closetie = true;
                            Toast.makeText(AddSupport.this, Boolean.toString(closetie), Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.radioButton2:
                            closetie = false;
                            Toast.makeText(AddSupport.this, Boolean.toString(closetie), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }
        });

    }


    public void submit(View v) {
        if(name.getText().toString().length()>0&&numberphone.length()>0) {

            discreteSeekBar1 = findViewById(R.id.discrete1);
            discreteSeekBar2 = findViewById(R.id.discrete2);
            discreteSeekBar3 = findViewById(R.id.discrete3);
            discreteSeekBar4 = findViewById(R.id.discrete4);
            emotional = discreteSeekBar1.getProgress();
            tangible = discreteSeekBar2.getProgress();
            informational = discreteSeekBar3.getProgress();
            companionship = discreteSeekBar4.getProgress();
            namestring =name.getText().toString();
            //database stuff
            Date currentTime = Calendar.getInstance().getTime();
            Map<String, Object> dassdata = new HashMap<>();
            dassdata.put("name", namestring);
            dassdata.put("contactno", numberphone);
            dassdata.put("closetie", closetie);
            dassdata.put("emotional", emotional);
            dassdata.put("tangible", tangible);
            dassdata.put("informational", informational);
            dassdata.put("companionship", companionship);
            dassdata.put("burden", 100);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //Toast.makeText(DASSActivity.this, "depression "+Integer.toString(depression)+" anxiety "+Integer.toString(anxiety)+"Stress: "+Integer.toString(stress), Toast.LENGTH_SHORT).show();
            db.collection("users").document(uid).collection("supportlist").add(dassdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    startActivity(new Intent(AddSupport.this, MainMenu.class));
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
        else{
            Toast.makeText(AddSupport.this, "Please Add All data required!", Toast.LENGTH_SHORT).show();
        }
        //done

    }

    public void contactPicker(View v) {
        // This intent will fire up the contact picker dialog
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, REQUEST_CONTACTPICKER);
    }

    //@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACTPICKER && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneno = (TextView) findViewById(R.id.textViewPhone);
            //(new normalizePhoneNumberTask()).execute(cursor.getString(column));
            numberphone = cursor.getString(column);
            phoneno.setText(numberphone);
            Log.d("phone number", cursor.getString(column));
        }
    }
}