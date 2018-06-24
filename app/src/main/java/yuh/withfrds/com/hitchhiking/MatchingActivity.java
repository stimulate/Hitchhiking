package yuh.withfrds.com.hitchhiking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingActivity extends BaseActivity {

    private String doc_id;
    private String collection_name;
    private ListView lv;
    final ArrayList<Map<String,Object>> resultsList = new ArrayList<Map<String,Object>>();

    private String uid_posting;


    private FloatingActionButton fab;

    private int selected =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        doc_id = getIntent().getExtras().getString("doc_id");
        collection_name = getIntent().getExtras().getString("collection_name");
        uid_posting = getIntent().getExtras().getString("uid");


        lv = findViewById(R.id.matching_list);

        fab = findViewById(R.id.confirmFAB);

        fab.setVisibility(View.GONE);
        initList();

    }

    private void initList(){



        final SimpleAdapter adapter = new SimpleAdapter(this,
                resultsList,
                R.layout.item,
                new String[]{"status","from","to", "user"},
                new int[]{
                        R.id.dashboard_item_state,
                        R.id.dashboard_item_content,
                        R.id.dashboard_item_content2,
                        R.id.dashboard_item_user
        });

        lv.setAdapter(adapter);

        DocumentReference docRef = OurStore.getDB().collection(collection_name).document(doc_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Status", "DocumentSnapshot data: " + document.getData());

                        Query q = OurStore.getMatchingQuery(collection_name, document.getData());

                        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Status", "doing a query");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("Status", document.getId() + " => " + document.getData());

                                        Map<String, Object> doc = document.getData();

                                        doc.put("doc_id", document.getId());

                                        if (doc.get("matching_uid")!=null  ){

                                        }else{

                                            if (collection_name == "Offers") {
                                                doc.put("status", R.drawable.check20);
                                            }else{
                                                doc.put("status", R.drawable.check10);
                                            }

                                            resultsList.add(doc);


                                            adapter.notifyDataSetChanged();

                                            fab.setVisibility(View.VISIBLE);
                                        }

                                        if (resultsList.size() == 0){
                                            Toast.makeText(MatchingActivity.this, "No matching", Toast.LENGTH_LONG).show();
                                            gobackToDashboard();
                                        }
                                    }

                                    // limit the results to to filed by hand

                                } else {
                                    Log.d("Error", "Error getting documents: ", task.getException());
                                }
                            }
                        });


                    } else {
                        Log.d("Error", "No such document");
                    }
                } else {
                    Log.d("Error", "get failed with ", task.getException());
                }





            }
        });

        // set onlick listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Satatus", "item clicked");

                Toast.makeText(MatchingActivity.this,  ""+resultsList.get(i).get("to"), Toast.LENGTH_SHORT).show();


                selected = i;

            }
        });
    }
    private void gobackToDashboard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void confirmMatching(View view){

        if (selected < 0){
            return;
        }

        // for the doc of the peroson matched

        Map<String, Object> map = new HashMap<>();

        String uid_matching =(String)resultsList.get(selected).get("uid");

        map.put("matching_uid", uid_matching );

        OurStore.getDB().collection(collection_name).document(
                doc_id
        ).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Statues", "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating document", e);
                    }
                });



        // for the doc of the poster

        String collection_name_matching = "Offers";

        if (collection_name.equals( "Offers")){
            collection_name_matching = "Requests";
        }

        Map<String, Object> map_matching = new HashMap<>();



        map.put("matching_uid", uid_posting );

        OurStore.getDB().collection(collection_name_matching).document(
                (String)resultsList.get(selected).get("doc_id")
        ).update(map_matching )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Statues", "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating document", e);
                    }
                });

        gobackToDashboard();

    }
}
