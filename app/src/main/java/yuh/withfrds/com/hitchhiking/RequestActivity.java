package yuh.withfrds.com.hitchhiking;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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


    private static Location depLoc;
    private static Location destLoc;
    private static String depAddress;
    private static String destAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        EventBus.getDefault().register(this);

        initTexts();
        initFirestore();

    }


    @Override
    protected void onStart() {
        super.onStart();
        TIMEFORMAT = OfferActivity.TIMEFORMAT;



        textStart.setText(depAddress);
        textDest.setText(destAddress);
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

    private void openMapActivity(){

        Intent intent = new Intent(this, Maps_Activity.class);
        startActivity(intent);
//        finish();
    }

    private void postRequest(){
        String startPlace = textStart.getText().toString();
        String destination = textDest.getText().toString();

        int seats = Integer.parseInt(textSeats.getText().toString());

        GeoPoint userLocation = new GeoPoint(OurLocation.location.getLatitude(), OurLocation.location.getLongitude());

        Date timeStart = new Date();
        Date timeEnd = new Date();

        try {

            timeStart = new SimpleDateFormat(TIMEFORMAT).parse(textTimeStart.getText().toString());
            timeEnd = new SimpleDateFormat(TIMEFORMAT).parse(textRangeTo.getText().toString());

        }catch (Exception e){
            Log.e("Error", "postOffer: " +e +"");
        }
        OurStore.postAnRequest(userLocation, startPlace, destination, timeStart, timeEnd, seats, depLoc, destLoc);

        getBackToDashboard();

    }

    public void postRequest(View view){
        postRequest();
    }


    public void openMap(View view){
        openMapActivity();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void getAddresses(Msg mMsg) {

        depAddress =  mMsg.getDep();
        destAddress= mMsg.getDest();
        depLoc = mMsg.getDepLocation();
        destLoc = mMsg.getDestLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
