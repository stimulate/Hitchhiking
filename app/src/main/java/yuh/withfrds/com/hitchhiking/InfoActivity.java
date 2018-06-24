package yuh.withfrds.com.hitchhiking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by a_yu_ on 2018/6/19.
 */
public class InfoActivity extends BaseActivity {

    private Button mSub,mChoose,mUpload;
    private ImageView mImg;
    private Uri filepath;
    private EditText mName0Txt,mName1Txt;
    private EditText mOccupation,mPhone,mPlate;
    private EditText mAddress,mCity,mState;
    private RadioGroup mradiogrp;
    private FirebaseFirestore mDb;
    private RadioButton mBtn1,mBtn2;
    private String sex;
    private Integer role;
    private CheckBox mchx1,mchx2;
    private Integer ageGroup;
    private final int PICK_IMAGE_REQUEST = 23;
    FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mDb = FirebaseFirestore.getInstance();
        mStorage =  FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        final String userId = FirebaseAuth. getInstance().getCurrentUser().getUid();

        final DocumentReference user= mDb.collection("Users").document(userId);
        final Spinner ageSelector = (Spinner) findViewById(R.id.age);
        final ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(InfoActivity.this, android.R.layout.simple_list_item_1,
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
        mChoose = findViewById(R.id.info_choose);
        mUpload = findViewById(R.id.info_upload);
        mImg = findViewById(R.id.image_upload);

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
        ageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ageGroup = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        try {
                            mName0Txt.setText(document.get("FirstName").toString());
                            mName1Txt.setText(document.get("LastName").toString());
                            mOccupation.setText(document.get("Occupation").toString());
                            mAddress.setText(document.get("Address").toString());
                            mCity.setText(document.get("City").toString());
                            mState.setText(document.get("Country").toString());
                            mPhone.setText(document.get("MobilePhone").toString());
                            mPlate.setText(document.get("LicensePlate").toString());
                            Integer r = Integer.parseInt(document.get("role").toString());
                            if (r == 1) {
                                mchx1.setChecked(true);
                            } else if (r == 2) {
                                mchx2.setChecked(true);
                            } else if (r == 3) {
                                mchx1.setChecked(true);
                                mchx2.setChecked(true);
                            }
                            String s = document.get("gender").toString();
                            if (s == "Female") {
                                mBtn1.setChecked(true);
                            } else if (s == "Male") {
                                mBtn2.setChecked(true);
                            }

                            if (document.get("ageGroup") != null) {
                                Integer a;
                                a = Integer.parseInt(document.get("ageGroup").toString());
                                ageSelector.setSelection(a);
                            }
                            mUpload.setVisibility(View.INVISIBLE);
                            mChoose.setVisibility(View.INVISIBLE);
                            mSub.setVisibility(View.INVISIBLE);
                            if (document.get("avatar") != null) {
                                mImg.setImageURI(Uri.parse(document.get("avatar").toString()));
                            }
                        }catch(Exception e) {
                            Log.d("Error", "get failed with ", e);
                            Toast.makeText(InfoActivity.this, "Fields are null", Toast.LENGTH_LONG).show();
                        }

                    } else {
                    }
                } else {
                    Log.d("Error", "get failed with ", task.getException());
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
                userInfo.put("avatar",filepath.toString());
                userInfo.put("ageGroup",ageGroup);
                    user.set(userInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(InfoActivity.this,"Save successfully",Toast.LENGTH_LONG).show();
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
//                mDb.collection("Users").document(userId)
//                    .set(userInfo)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(InfoActivity.this,"Save successfully",Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("Fail", "Error writing document", e);
//                        }
//                    });

        mUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                UploadImg();
            }
        });
        mChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ChooseImg();
            }
        });
    }
    private void ChooseImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),PICK_IMAGE_REQUEST);
    }

    private void UploadImg(){
        if(filepath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();
            StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filepath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            Toast.makeText(InfoActivity.this,"Uploaded successfuly",Toast.LENGTH_LONG).show();
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(InfoActivity.this,"Upload failed" + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            })
           .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                   double progress = (100.0 * taskSnapshot.getBytesTransferred()/
                           taskSnapshot.getTotalByteCount());
                   progressDialog.setMessage("Uploaded " + (int)progress+ "%");
               }
           });
        }
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            filepath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                mImg.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
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