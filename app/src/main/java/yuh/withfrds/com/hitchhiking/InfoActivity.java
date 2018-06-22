package yuh.withfrds.com.hitchhiking;

import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a_yu_ on 2018/6/19.
 */
public class InfoActivity extends BaseActivity {

    private Button mSub;
    private EditText mName0Txt;
    private EditText mName1Txt;
    private EditText mOccupation;
    private EditText mAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mPhone;
    private EditText mPlate;
    private RadioGroup mradiogrp;
    private FirebaseFirestore mDb;
    private RadioButton mBtn1;
    private RadioButton mBtn2;
    private String sex;
    private Integer role;
    private CheckBox mchx1;
    private CheckBox mchx2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mDb = FirebaseFirestore.getInstance();

        final String userId = FirebaseAuth. getInstance().getCurrentUser().getUid();
        Spinner ageSelector = (Spinner) findViewById(R.id.age);
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.age_group));
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSelector.setAdapter(ageAdapter);

        mName0Txt = findViewById(R.id.info_name0);
        mName1Txt = findViewById(R.id.info_name1);
        mOccupation = findViewById(R.id.info_job);
        mAddress = findViewById(R.id.info_address);
        mCity = findViewById(R.id.info_city);
        mState = findViewById(R.id.info_city);
        mPhone = findViewById(R.id.info_no);
        mPlate = findViewById(R.id.info_plate);
        mradiogrp = findViewById(R.id.radioGroup);
        mBtn1 = findViewById(R.id.info_female);
        mBtn2 = findViewById(R.id.info_male);
        mchx1 = findViewById(R.id.info_driver);
        mchx2 = findViewById(R.id.info_hiker);
        mSub = findViewById(R.id.info_submit);

        mchx1.setOnCheckedChangeListener(listener);
        mchx2.setOnCheckedChangeListener(listener);

        mradiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mBtn1.getId() == checkedId) {
                    sex = mBtn1.getText().toString();
                } else if (mBtn2.getId() == checkedId) {
                    sex = mBtn2.getText().toString();
                }
            }
        });

        mSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firName = mName0Txt.getText().toString();
                String lastName = mName1Txt.getText().toString();
                String occupation = mOccupation.getText().toString();
                String address = mAddress.getText().toString();
                String city = mCity.getText().toString();
                String state = mState.getText().toString();
                String phone = mPhone.getText().toString();
                String plate = mPlate.getText().toString();

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("FirstName", firName);
                userInfo.put("LastName", lastName);
                userInfo.put("Occupation", occupation);
                userInfo.put("Address", address);
                userInfo.put("City", city);
                userInfo.put("Country", state);
                userInfo.put("MobilePhone", phone);
                userInfo.put("LicensePlate", plate);
                userInfo.put("role", role);
                userInfo.put("gender", sex);

                mDb.collection("Users").document(userId)
                        .set(userInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(InfoActivity.this,"Save successfully",Toast.LENGTH_LONG);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Fail", "Error writing document", e);
                            }
                        });
            }
        });

    }
    private CheckBox.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId()== R.id.info_driver){
                if (isChecked){
                    role = 1;
                }
            }

            else if (buttonView.getId()==R.id.info_hiker){
                if (isChecked){
                    role =  2;
                }
            }

            if (mchx1.isChecked() && mchx2.isChecked()){
                role = 3;
            }
        }
    };
}