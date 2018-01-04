package org.androidtown.randomsite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
    }

    public void moveToCafeStatusActivity(View view) {
        Intent intent = new Intent(this, CafeStatusActivity.class);
        startActivity(intent);
    }
}
