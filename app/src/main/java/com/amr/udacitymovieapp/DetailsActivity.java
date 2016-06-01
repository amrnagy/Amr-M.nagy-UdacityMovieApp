package com.amr.udacitymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailsActivity extends ActionBarActivity {


       @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_details);

           if (savedInstanceState == null) {
               // Create the detail fragment and add it to the activity
               // using a fragment transaction.
               Intent i = getIntent();

               Bundle dMap = new Bundle();

               dMap.putString("id",i.getStringExtra("id"));
               dMap.putString("title",i.getStringExtra("title"));
               dMap.putString("path", i.getStringExtra("path"));
               dMap.putString("date", i.getStringExtra("date"));
               dMap.putString("overview", i.getStringExtra("overview"));
               dMap.putDouble("vote", i.getDoubleExtra("vote", 0));

               DetailsActivityFragment fragment = new DetailsActivityFragment();
               fragment.setArguments(dMap);
               getSupportFragmentManager().beginTransaction()
                       .replace(R.id.movie_detail_container, fragment)
                       .commit();
           }

       }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_details, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


    }
