package yuh.withfrds.com.hitchhiking;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;


public class OfferActivity extends BaseActivity {


    /*
    Author:
    tim
    github.com/tim-hub
     */
    public static String TIMEFORMAT = "HH:mm dd/MM/yyyy";

    private FirebaseFirestore db;


    private EditText textStart;
    private EditText textDest;
    private EditText textPass;

    private EditText textTimeStart;
    private EditText textTimeEnd;
    private EditText textSeats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        initTexts();
        initFirestore();


        Button offerButton = findViewById(R.id.buttonPost);
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postOffer();
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

        textStart = (EditText) findViewById(R.id.textStartAddress);
        textDest = (EditText) findViewById(R.id.textDestAddress);
        textPass = (EditText) findViewById(R.id.textPassAddress);
        textTimeStart = (EditText) findViewById(R.id.requestTimeDeparture);
        //textTimeEnd = findViewById(R.id.requestTimeRangeTo);
        textSeats = findViewById(R.id.textSeats);


    }

    private void getBackToDashboard(){

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }






    private void postOffer(){

        String startPlace = textStart.getText().toString();
        String destination = textDest.getText().toString();
        String passPlaces = textPass.getText().toString();

        GeoPoint userLocation = new GeoPoint(OurLocation.location.getLatitude(), OurLocation.location.getLongitude());

        int seats = Integer.parseInt(textSeats.getText().toString());

        Date timeStart = new Date();
        Date timeEnd = new Date();

        try {

            timeStart = new SimpleDateFormat(TIMEFORMAT).parse(textTimeStart.getText().toString());
            timeEnd = new SimpleDateFormat(TIMEFORMAT).parse(textTimeEnd.getText().toString());

        }catch (Exception e){
            Log.e("Error", "postOffer: " +e +"");
        }
        OurStore.postAnOffer(userLocation ,startPlace, destination, passPlaces, timeStart, timeEnd, seats);

        getBackToDashboard();
    }

    /*
    get fields and id of user
     */
    public void postOffer(View view){
        postOffer();
    }

}
