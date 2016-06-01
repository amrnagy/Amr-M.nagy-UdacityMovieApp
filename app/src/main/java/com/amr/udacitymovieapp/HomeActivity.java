package com.amr.udacitymovieapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HomeActivity extends ActionBarActivity {

    boolean inTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (findViewById(R.id.movie_detail_container) != null) {

            inTwoPane = true;

        } else {
            inTwoPane = false;
        }
    }

}
