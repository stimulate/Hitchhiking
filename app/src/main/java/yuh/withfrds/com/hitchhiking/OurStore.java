package yuh.withfrds.com.hitchhiking;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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

public class OurStore {
    /*
This is an encapsulation class for fire store

如果后续时间充足，应该封装firestore 存储到这个类里，

好处是

- 代码质量上会有加分
- 维护也更容易



如果时间不够，删除这个类


 */



    /*

    get fields and id of user

     */
    private static String getUserId(){

        String uid ="";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();

        }else{
            Log.d("Error", "Firebase Auth error");
        }
        return uid;
    }






    public static void submitADocument(
            FirebaseFirestore db,
            String collectionName ,
            String start, String dest,
            String pass,
            Date timeStart, Date timeEnd,
            int seats
    ){


        String uid = getUserId(); //we should get this id from auth

        String startPlace = start;
        String destination = dest;
        String passPlaces = pass;


        // get the mas which storage key and values
        final Map<String, Object> offer = new HashMap<>();

        offer.put("from", startPlace);
        offer.put("to", destination);


        final DocumentReference theUser= db.collection("Users").document(uid);

        final CollectionReference offersCollection= db.collection(collectionName);


        theUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        offersCollection.add(offer);

                    } else {
                        Log.d("State", "No such document, the document will be created");

                        Map<String, Object> userData = new HashMap<>();

                        Map<String, Object> nestedData = new HashMap<>();
                        // get more user profile from auth
                        nestedData.put("userName", "username");

                        userData.put("Info", nestedData);

                        theUser.set(
                                userData
                        );

                        // store the offer data

                        offersCollection.add(offer);

                    }
                } else {
                    Log.d("Error", "get failed with ", task.getException());
                }
            }
        });

    }


    public static void postAnOffer(FirebaseFirestore db,
                                   String start, String dest,
                                   String pass,
                                   Date timeStart, Date timeEnd,
                                   int seats)
    {
        submitADocument(db, "Offers", start,dest,pass, timeStart, timeEnd, seats);
    }

    public static void postAnRequest(FirebaseFirestore db,
                                     String start, String dest,
                                     String pass,
                                     Date timeStart, Date timeEnd,
                                     int seats){
        submitADocument(db, "Requests", start,dest,pass, timeStart, timeEnd, seats);
    }


}
