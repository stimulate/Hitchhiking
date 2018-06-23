package yuh.withfrds.com.hitchhiking;


import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

    /*
This is an encapsulation class for fire store

created by Tim
 */


public class OurStore {


    // ref about gps
    // nz latitude around 40.9006° S,
    // one minute of latitue is around 1.85km
    // one minute of longtitude is around 1.42km

    // http://www.longitudestore.com/how-big-is-one-gps-degree.html

    // we assume in 2km is nearyby

    private static final double LAT_DIFF = 2.0/1.85/60;
    private static final double LONG_DIFF = 2.0/1.42/60;


    private static String[] splitPasses(String pass){

        // remove all white spaces
        pass = pass.replaceAll("\\s","");

        return pass.split(",");
    }


    private static GeoPoint getGeoPoint(Location location){

        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    private static GeoPoint getSWPoint(GeoPoint point){

        double lat = point.getLatitude();
        double lon = point.getLongitude();

        return new GeoPoint(lat-LAT_DIFF, lon-LONG_DIFF);

    }
    private static GeoPoint getNEPoint(GeoPoint point){
        double lat = point.getLatitude();
        double lon = point.getLongitude();

        return new GeoPoint(lat+LAT_DIFF, lon+LONG_DIFF);
    }

    private static void submitADocument(
            FirebaseFirestore db,
            GeoPoint current_location_point,
            String collectionName ,
            String dep, String dest,
            String pass,
            Date timeStart, Date timeEnd,
            int seats,
            GeoPoint depPoint,
            GeoPoint destPoint
    ){


        String uid = getUserId(); //we should get this id from auth




        // get the mas which storage key and values
        final Map<String, Object> ourMap = new HashMap<>();


        ourMap.put("uid", uid);
        ourMap.put("location_current", current_location_point);
        ourMap.put("from", dep);
        ourMap.put("to", dest);

        ourMap.put("location_from", depPoint);
        ourMap.put("location_to", destPoint);
        ourMap.put("timeFrom", timeStart);
        ourMap.put("timeTo", timeEnd);
        ourMap.put("seats", seats);


        if (pass!="" && collectionName!="Requests"){
            // it is an offer
            // we should put a sub object
            String[] passes = splitPasses(pass);

            // use object here, because array cannot be queried in firestore
            Map<String, Object> passMap = new HashMap<>();

            for (String p:passes
                 ) {
                passMap.put(p, true);
            }

            ourMap.put("pass", passMap);
        }


        final DocumentReference theUser= db.collection("Users").document(uid);

        final CollectionReference offersCollection= db.collection(collectionName);


        theUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        offersCollection.add(ourMap);

                    } else {
                        Log.d("State", "No such document, the document will be created");

                        Map<String, Object> userData = new HashMap<>();

                        Map<String, Object> nestedData = new HashMap<>();

                        userData.put("Profile", nestedData);

                        theUser.set(
                                userData
                        );

                        // store the postOffer data
                        offersCollection.add(ourMap);

                    }
                } else {
                    Log.d("Error", "get failed with ", task.getException());
                }
            }
        });

    }


    /*

    get fields and id of user

     */
    public static String getUserId(){

        String uid ="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();

        }else{
            Log.d("Error", "Not logined");
        }
        return uid;
    }

    public static FirebaseFirestore getDB(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db;
    }

    /*
    Set user profile
     */


    public static void setUserProfile(Map<String, Object> userMap){
        // set user profile

    }


    /*
    get user profile, return a map

     */
    public static Map<String, Object> getUserProfileMap(){
        // get user profile

        Map<String, Object> nestedData = new HashMap<>();


        return nestedData;

    }

    /*

    Post an offer to db
     */

    public static void postAnOffer(
                                    GeoPoint location,
                                   String start, String dest,
                                   String pass,
                                   Date timeStart, Date timeEnd,
                                   int seats,
                                    Location depLocation,
                                    Location destLocation
                                    )
    {



        GeoPoint depPoint= getGeoPoint(depLocation);
        GeoPoint destPoint = getGeoPoint(destLocation);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        submitADocument(db, location,"Offers", start, dest, pass, timeStart, timeEnd, seats, depPoint, destPoint);
    }

    /*
    Post an request to db
     */

    public static void postAnRequest(
                                    GeoPoint location,
                                     String start, String dest,
                                     Date timeStart, Date timeEnd,
                                     int seats,
                                    Location depLocation,
                                    Location destLocation

    ){
        GeoPoint depPoint= getGeoPoint(depLocation);
        GeoPoint destPoint = getGeoPoint(destLocation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String pass ="";
        submitADocument(db, location,"Requests", start,dest,pass, timeStart, timeEnd, seats, depPoint, destPoint);
    }


    /*
    get user's all offers and requests
     */

    public  static QuerySnapshot getAll(){
        return null;
    }

    /*
    This is the core function for matching requests and offers
    parameters, doc (offer or request), user profile
     */

    public static Query getMatchingQuery(String collection_belong_to, Map<String, Object> doc, Map<String, Object> user){


        String collection_name = "Offers";



        if (collection_belong_to == "Offers"){
            collection_name = "Requests";
        }
        CollectionReference ref = getDB().collection(collection_name);
        Query q;

        if (user!=null){

        }


        GeoPoint dep = (GeoPoint) doc.get("location_from");
        GeoPoint dest = (GeoPoint) doc.get("location_to");

        GeoPoint current = (GeoPoint) doc.get("location_current");


        GeoPoint swPoint = getSWPoint(dep);
        GeoPoint nePoint = getNEPoint(dest);


        q=ref.whereLessThanOrEqualTo("location_from", nePoint).
                whereGreaterThanOrEqualTo("location_from", swPoint);

        return q;

    }

    public static Query getMatchingQuery(String collection_belong_to, Map<String, Object> doc){

        return getMatchingQuery(collection_belong_to,doc, null);
    }


    public static void  doQuery(Query q){
        final ArrayList<Map<String,Object>> results  = new ArrayList<>();
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Satatus", document.getId() + " => " + document.getData());

                        Map<String, Object> doc = document.getData();
//                        list.add("From: " + doc.get("from") +" To: " + doc.get("to") +" Pass: " +doc.get("pass"));
                        results.add(doc);
                    }

                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        });
    }



}
