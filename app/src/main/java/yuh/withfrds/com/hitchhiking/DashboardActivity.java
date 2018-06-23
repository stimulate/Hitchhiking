package yuh.withfrds.com.hitchhiking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;


public class DashboardActivity extends BaseActivity {


    private static String TAG = DashboardActivity.class.getSimpleName();

    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // ref: https://www.dev2qa.com/android-listview-example/
        // ref: https://alvinalexander.com/source-code/android/android-listactivity-and-listview-example

        lv = findViewById(R.id.dashboard_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Satatus", "item clicked");
            }
        });

    }

    private void initList(){



        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();

//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);


        final SimpleAdapter adapter = new SimpleAdapter(this,
                itemDataList,
                R.layout.item,
                new String[]{"status","from","to"},
                new int[]{R.id.dashboard_item_state, R.id.dashboard_item_content, R.id.dashboard_item_content2}
                );
        lv.setAdapter(adapter);

        FirebaseFirestore db = OurStore.getDB();

        CollectionReference offersRef = db.collection("Offers");

        Query q = offersRef.whereEqualTo("uid", OurStore.getUserId());


        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Satatus", document.getId() + " => " + document.getData());

                        Map<String, Object> doc = document.getData();
//                        list.add("From: " + doc.get("from") +" To: " + doc.get("to") +" Pass: " +doc.get("pass"));
                        itemDataList.add(doc);

                        if (doc.get("matching_uid")!=null  ){

                            doc.put("status", R.drawable.check1);

                        }else{
                            doc.put("status", R.drawable.check1);
                        }

                        Log.d("Status", "onStart: "+itemDataList.get(0));

                        adapter.notifyDataSetChanged();

                    }

                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            }
        });






// this snap shot listener should work
//       // https://firebase.google.com/docs/firestore/query-data/queries#compound_queries
//        q.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value,
//                                @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.", e);
//                    return;
//                }
//
//
//                for (QueryDocumentSnapshot document : value) {
//                    if (document.getId() != null) {
//                        Log.d("Satatus", document.getId() + " => " + document.getData());
//
//                        Map<String, Object> doc = document.getData();
//                        list.add("From: " + doc.get("from") +" To: " + doc.get("to") +" Pass: " +doc.get("pass"));
//
//                        Log.d("Status", "onStart: "+list.get(0));
//
//                        adapter.notifyDataSetChanged();
//                    }
//                }
////                Log.d(TAG, "Current cites in CA: " + cities);
//            }
//        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        initList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_main: return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
