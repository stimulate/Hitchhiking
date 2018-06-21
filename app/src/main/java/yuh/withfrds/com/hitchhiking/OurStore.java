package yuh.withfrds.com.hitchhiking;


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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

    /*
This is an encapsulation class for fire store

created by Tim
 */


public class OurStore {



    /*

    get fields and id of user

     */
    private static String getUserId(){

        String uid ="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();

        }else{
            Log.d("Error", "Not logined");
        }
        return uid;
    }

    private static String[] splitPasses(String pass){

        // remove all white spaces
        pass = pass.replaceAll("\\s","");

        return pass.split(",");
    }


    private static void submitADocument(
            FirebaseFirestore db,
            String collectionName ,
            String start, String dest,
            String pass,
            Date timeStart, Date timeEnd,
            int seats
    ){


        String uid = getUserId(); //we should get this id from auth




        // get the mas which storage key and values
        final Map<String, Object> ourMap = new HashMap<>();

        ourMap.put("uid", uid);
        ourMap.put("from", start);
        ourMap.put("to", dest);
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

    public static void postAnOffer(FirebaseFirestore db,
                                   String start, String dest,
                                   String pass,
                                   Date timeStart, Date timeEnd,
                                   int seats)
    {
        submitADocument(db, "Offers", start,dest,pass, timeStart, timeEnd, seats);
    }

    /*
    Post an request to db
     */

    public static void postAnRequest(FirebaseFirestore db,
                                     String start, String dest,

                                     Date timeStart, Date timeEnd,
                                     int seats){

        String pass ="";
        submitADocument(db, "Requests", start,dest,pass, timeStart, timeEnd, seats);
    }


}
