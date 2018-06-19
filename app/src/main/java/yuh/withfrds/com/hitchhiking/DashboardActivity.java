package yuh.withfrds.com.hitchhiking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // ref: https://www.dev2qa.com/android-listview-example/
        // ref: https://alvinalexander.com/source-code/android/android-listactivity-and-listview-example




        Button offerButton = findViewById(R.id.buttonOffer);
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOfferActivity();
            }
        });
    }


    private void startOfferActivity() {
        Intent intent = new Intent(this, OfferActivity.class);
        startActivity(intent);
//        finish();
    }


}
