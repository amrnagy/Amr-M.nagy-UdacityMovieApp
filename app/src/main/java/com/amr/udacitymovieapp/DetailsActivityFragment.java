package com.amr.udacitymovieapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amr.udacitymovieapp.Models.ReviewItem;
import com.amr.udacitymovieapp.Models.TrailerItem;
import com.amr.udacitymovieapp.sData.MovieData;
import com.amr.udacitymovieapp.sData.MovieDbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by amr on 5/26/2016.
 */
public class DetailsActivityFragment extends Fragment {

    ArrayAdapter<String> vidAdapter;
    ArrayAdapter<String> revAdapter;
    ArrayList<TrailerItem> suggestedVideos = new ArrayList<>();
    ArrayList<ReviewItem> suggestedReviews = new ArrayList<>();
    String id;
    ListView traList;
    ListView revList;
    ImageButton favourite;

    public DetailsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getVideos();

    }

    private void getVideos() {
        FetchVideo f = new FetchVideo();
        f.execute(id);
    }
    private void getReviews() {
        FetchReview f = new FetchReview();
        f.execute(id);
    }
    Uri mUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle dMap = getArguments();
        if (dMap != null) {

                        id = dMap.getString("id");
            final String title = dMap.getString("title");
            final String path = dMap.getString("path");
            final String date = dMap.getString("date");
            final String overview = dMap.getString("overview");
            final double vote = dMap.getDouble("vote", 0);

            TextView titleTextView = (TextView) v.findViewById(R.id.title);
            titleTextView.setText(title);
            TextView dateTextView = (TextView) v.findViewById(R.id.date);
            dateTextView.setText(date);
            TextView overviewTextView = (TextView) v.findViewById(R.id.overview);
            overviewTextView.setText(overview);
            TextView rateTextView = (TextView) v.findViewById(R.id.rate);
            rateTextView.setText("" + vote);
            ImageView imageView = (ImageView) v.findViewById(R.id.image);
            Picasso.with(getActivity())
                    .load(path)
                    .fit()
                    .into(imageView);

            favourite = (ImageButton) v.findViewById(R.id.favourite);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieDbHelper db = new MovieDbHelper(getActivity());
                    boolean state = db.insertData(id, title, path, overview, vote, date);
                    if (state == true) {
                        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                        build.setTitle("Add Favourite Movies");
                        build.setMessage("Movie Added Successfully");

                        build.setPositiveButton("OK", null);
                        AlertDialog alert = build.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                        build.setTitle("Add Favourite Movies");
                        build.setMessage("Movie Is Added Previously !");
                        build.setPositiveButton("OK", null);
                        AlertDialog alert = build.create();
                        alert.show();
                    }
                }
            });
            Button rev = (Button) v.findViewById(R.id.rating);
            rev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getReviews();
                }
            });
            traList = (ListView) v.findViewById(R.id.listView);
            traList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = suggestedVideos.get(position).getSite();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(path));

                    startActivity(intent);
                }
            });
            revList = (ListView) v.findViewById(R.id.reviewsList);
            revList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ReviewItem revItem = suggestedReviews.get(position);
                    AlertDialog.Builder aD = new AlertDialog.Builder(getActivity());
                    aD.setTitle("Details About Reviews");
                    String message = "ID : " + revItem.getId() + "\n";
                    message += "Author : " + revItem.getAuthor() + "\n";
                    message += "Content : " + revItem.getContent();

                    aD.setMessage(message);
                    aD.setPositiveButton("OK", null);
                    AlertDialog alert = aD.create();
                    alert.show();

                }
            });
        }
        return v;
    }
    public class FetchReview extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchReview.class.getSimpleName();
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setMessage("Loading Suggested Reviews  ...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... id) {
            MovieData wsMovieData = new MovieData();
            JSONObject result = wsMovieData.getReviews(id[0]);
            suggestedReviews = wsMovieData.getAllReviewsFromJson(result);
            Log.d(LOG_TAG, result.toString());
            String[] reviews = new String[suggestedReviews.size()];
            for (int i = 0; i < reviews.length; i++) {
                reviews[i] = suggestedReviews.get(i).getAuthor();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                revAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, result);
                revList.setAdapter(revAdapter);
                dialog.hide();
                dialog.dismiss();
            }
        }

    }

    public class FetchVideo extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchVideo.class.getSimpleName();
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setMessage("Loading Suggested Videos  ...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... id) {
            MovieData wsMovieData = new MovieData();
            JSONObject result = wsMovieData.getVideos(id[0]);
            suggestedVideos = wsMovieData.getAllVediosFromJson(result);
            Log.d(LOG_TAG, result.toString());
            String[] VideoUrls = new String[suggestedVideos.size()];
            for (int i = 0; i < VideoUrls.length; i++) {
                VideoUrls[i] = "Trailer " + (i + 1);// allVideos.get(i).getSite();
            }
            return VideoUrls;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                vidAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, result);
                traList.setAdapter(vidAdapter);
                dialog.hide();
                dialog.dismiss();
            }
        }

    }


}