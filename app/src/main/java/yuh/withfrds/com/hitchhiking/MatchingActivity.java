package yuh.withfrds.com.hitchhiking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MatchingActivity extends BaseActivity {

    private String doc_id;
    private String collection_name;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        doc_id = getIntent().getExtras().getString("doc_id");
        collection_name = getIntent().getExtras().getString("collection_name");


        lv = findViewById(R.id.matching_list);
        initList();

    }

    private void initList(){

        final ArrayList<Map<String,Object>> resultsList = new ArrayList<Map<String,Object>>();
        final SimpleAdapter adapter = new SimpleAdapter(this,
                resultsList,
                R.layout.item,
                new String[]{"status","from","to"},
                new int[]{R.id.dashboard_item_state, R.id.dashboard_item_content, R.id.dashboard_item_content2}
        );
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

                                        if (doc.get("matching_uid")!=null  ){

                                        }else{

                                            if (collection_name == "Offers") {
                                                doc.put("status", R.drawable.check20);
                                            }else{
                                                doc.put("status", R.drawable.check10);
                                            }
                                            resultsList.add(doc);
                                            adapter.notifyDataSetChanged();
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








    }
}
