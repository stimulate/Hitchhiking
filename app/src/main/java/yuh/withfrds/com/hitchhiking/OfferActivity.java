package yuh.withfrds.com.hitchhiking;


import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class OfferActivity extends BaseActivity {


    /*
    Author:
    tim
    github.com/tim-hub
     */



    private FirebaseFirestore db;

    private TextView textStart;
    private TextView textDest;
    private TextView textPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        initTexts();

        Button offerButton = findViewById(R.id.buttonPost);
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offer();
            }
        });

    }
    private void initFirestore(){

        // the fire storage codes starts here

//        FirebaseApp.initializeApp(this); // this is not required

        // please remember connect firebase firstly
        // get instance
        db = FirebaseFirestore.getInstance();


    }

    private void initTexts(){
        initFirestore();

        textStart = (EditText) findViewById(R.id.textStartAddress);
        textDest = (EditText) findViewById(R.id.textDestAddress);
        textPass = (EditText) findViewById(R.id.textPassAddress);
    }


    /*

    get fields and id of user

     */
    private String getUserId(){

        String uid ="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();



        } else {
            // No user is signed in
        }

        return uid;

    }

    public void offer(View view){
        offer();
    }


    private void offer(){

        String uid = getUserId(); //we should get this id from auth

        String startPlace = textStart.getText().toString();
        String destination = textDest.getText().toString();

        String passPlaces = textPass.getText().toString();


        // get the mas which storage key and values
        final Map<String, Object> offer = new HashMap<>();

        offer.put("from", startPlace);
        offer.put("to", destination);


        final DocumentReference theUser= db.collection("Users").document(uid);

        final CollectionReference offersCollection= theUser.collection("Offers");


        theUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        offersCollection.add(offer);


                    } else {
                        Log.d("State", "No such document");

                        Map<String, Object> userData = new HashMap<>();

                        Map<String, Object> nestedData = new HashMap<>();
                        // get more user profile from auth
                        nestedData.put("userName", "username");

                        userData.put("Info", nestedData);

                        theUser.set(
                            userData
                        );

                        CollectionReference offersCollection= theUser.collection("Offers");
                        offersCollection.add(offer);

                    }
                } else {
                    Log.d("Error", "get failed with ", task.getException());
                }
            }
        });



//        // add ducument
//        db.collection("Users")
//                .add(offer)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                                          @Override
//                                          public void onFailure(@NonNull Exception e) {
//                                              Log.w("Error", "Error adding document", e);
//                                          }
//                }
//                );

    }

}
