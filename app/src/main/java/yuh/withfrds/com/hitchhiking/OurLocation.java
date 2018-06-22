package yuh.withfrds.com.hitchhiking;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class OurLocation {


    public static FusedLocationProviderClient client;
    public static Location location;
    public  static int MY_PERMISSION_ACCESS_COURSE_LOCATION =122;

    public static void initLocation(Context context) {

        location= null;

        client = LocationServices.getFusedLocationProviderClient(context);

        try {
            Task<Location> location = client.getLastLocation();

            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    try{
                        Log.d("location:", task.getResult().toString());
                        updateLocation(task.getResult());
                    }
                    catch (NullPointerException e){
                        Log.e("error", "onComplete: " +e );
                    }


                }
            });
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        LocationRequest req = new LocationRequest();
        req.setInterval(2000);
        req.setFastestInterval(1000);

        // make it fast for testing


        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // the permission checked in login activity
        client.requestLocationUpdates(req, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                try{
                    Log.d("location:", locationResult.getLastLocation().toString());
                    updateLocation(locationResult.getLastLocation());
                }
                catch (NullPointerException e){
                    Log.e("error", "onLocationResult: " +e );
                }
            }
        }, null);
    }

    private static void updateLocation(Location loc){
        location = loc;
    }
}
