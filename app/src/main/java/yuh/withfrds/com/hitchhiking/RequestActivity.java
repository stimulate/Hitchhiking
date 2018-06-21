package yuh.withfrds.com.hitchhiking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestActivity extends BaseActivity {


    public static String TIMEFORMAT;


    private FirebaseFirestore db;

    private EditText textTimeStart;
    private EditText textRangeTo;

    private EditText textStart;
    private EditText textDest;

    private EditText textSeats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        initTexts();
        initFirestore();


    }


    @Override
    protected void onStart() {
        super.onStart();
        TIMEFORMAT = OfferActivity.TIMEFORMAT;
    }

    private void initTexts(){
        textStart = (EditText) findViewById(R.id.requestStartAddress);
        textDest = (EditText) findViewById(R.id.requestDestAddress);

        textRangeTo = (EditText) findViewById(R.id.requestTimeRangeTo);
        textTimeStart = (EditText) findViewById(R.id.requestTimeDeparture);

        textSeats = findViewById(R.id.requestSeats);
    }

    private void initFirestore(){

        db = FirebaseFirestore.getInstance();


    }

    private void getBackToDashboard(){

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }


    private void postRequest(){
        String startPlace = textStart.getText().toString();
        String destination = textDest.getText().toString();

        int seats = Integer.parseInt(textSeats.getText().toString());

        Date timeStart = new Date();
        Date timeEnd = new Date();

        try {

            timeStart = new SimpleDateFormat(TIMEFORMAT).parse(textTimeStart.getText().toString());
            timeEnd = new SimpleDateFormat(TIMEFORMAT).parse(textRangeTo.getText().toString());

        }catch (Exception e){
            Log.e("Error", "postOffer: " +e +"");
        }
        OurStore.postAnRequest(db, startPlace, destination, timeStart, timeEnd, seats);

        getBackToDashboard();

    }

    public void postRequest(View view){
        postRequest();
    }

}
