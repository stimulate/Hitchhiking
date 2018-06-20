package yuh.withfrds.com.hitchhiking;

import android.os.Bundle;
import android.widget.Spinner;

/**
 * Created by a_yu_ on 2018/6/19.
 */
public class InfoActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Spinner ageSelector = (Spinner)findViewById(R.id.age);
    }
}
