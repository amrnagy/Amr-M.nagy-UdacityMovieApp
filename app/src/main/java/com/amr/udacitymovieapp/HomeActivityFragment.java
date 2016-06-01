package com.amr.udacitymovieapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amr.udacitymovieapp.Adapters.GridAdapter;
import com.amr.udacitymovieapp.Models.MovieItem;
import com.amr.udacitymovieapp.sData.MovieData;
import com.amr.udacitymovieapp.sData.MovieDbHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {

    private GridView gridView;
    private boolean inTwoPane;


    private GridAdapter moviesAdapter;
    ArrayList<String> imgUrls = new ArrayList<>();
    ArrayList<MovieItem> suggestedMovies = new ArrayList<>();

    public HomeActivityFragment() {

       setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     getMovies();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.movie_detail_container) != null) {

            inTwoPane = true;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getActivity(), SettingActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        gridView = (GridView) v.findViewById(R.id.gridView);

        moviesAdapter = new GridAdapter(getActivity(), R.layout.item_grid_layout, imgUrls);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovieItem item = suggestedMovies.get(position);
                //Create intent
                if (inTwoPane == false) {
                    Intent inDet = new Intent(getActivity(), DetailsActivity.class);
                    inDet.putExtra("title", item.getTitle());
                    inDet.putExtra("overview", item.getOverview());
                    inDet.putExtra("path", item.getPath());
                    inDet.putExtra("vote", item.getVote_average());
                    inDet.putExtra("date", item.getDate());
                    inDet.putExtra("id", item.getId());
                    startActivity(inDet);


                } else
                {
                    // Bundle are generally used for passing data between various Android activities
                    // you can image it like a MAP that stores primitive datatypes and objects as couple key-value

                    Bundle dMap = new Bundle();

                    dMap.putString("id", item.getId());
                    dMap.putString("title", item.getTitle());
                    dMap.putString("path", item.getPath());
                    dMap.putString("date", item.getDate());
                    dMap.putString("overview", item.getOverview());
                    dMap.putDouble("vote", item.getVote_average());

                    DetailsActivityFragment fragment = new DetailsActivityFragment();
                    fragment.setArguments(dMap);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();


                }
            }
        });
        return v;
    }

    private void getMovies() {

        SharedPreferences sPrefernce = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String type = sPrefernce.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_popular));
        Toast.makeText(getActivity(), type, Toast.LENGTH_LONG).show();
        if(type.equals("favourite"))
        {
            MovieDbHelper db = new MovieDbHelper(getActivity());
            suggestedMovies = db.getMovies();
            if (suggestedMovies.size()>0)
            {
                moviesAdapter.clear();
                for (int i = 0; i < suggestedMovies.size(); i++) {
                    moviesAdapter.add(suggestedMovies.get(i).getPath());
                }
            }else
            {
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setTitle("Show Movies From Favourite");
                build.setMessage("Favourite Doesn't Contain any Movies");
                build.setPositiveButton("OK", null);
                AlertDialog alert = build.create();
                alert.show();
            }

        }else
        {
            FetchMovies f = new FetchMovies();
            f.execute(type);
        }
    }

    public class FetchMovies extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovies.class.getSimpleName();
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setMessage("Loading Amazing Movies  ...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... type) {
           MovieData wsMovieData = new MovieData();
            JSONObject result = wsMovieData.getMovies(type[0]);
            suggestedMovies = wsMovieData.getAllMoviesFromJson(result);
            Log.d(LOG_TAG, result.toString());
            String[] imgUrls = new String[suggestedMovies.size()];
            for (int i = 0; i < imgUrls.length; i++) {
                imgUrls[i] = suggestedMovies.get(i).getPath();
            }
            return imgUrls;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                moviesAdapter.clear();
                for (String movieStr : result) {
                    Log.d(LOG_TAG, movieStr);
                    moviesAdapter.add(movieStr);
                }
                dialog.hide();
                dialog.dismiss();
            }
        }

    }
}


    /*
    private static final String TAG = HomeActivityFragment.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridAdapter mGridAdapter;
    private ArrayList<MovieItem> mGridData;
    private String FEED_URL = "https://api.themoviedb.org/3/movie/550?api_key=1d077c24e2ef3f5e88b42da8af52ee3b";

    public HomeActivityFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        // i put getactivity here instead this
        mGridAdapter = new GridAdapter(getActivity(), R.layout.item_grid_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Start download
        new AsyncHttpTask().execute(FEED_URL);
        mProgressBar.setVisibility(View.VISIBLE);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                MovieItem item = (MovieItem) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });
        return v;
    }


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }
        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     * @param result
     */
    /*
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            MovieItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                item = new MovieItem();
                item.setTitle(title);
                JSONArray attachments = post.getJSONArray("attachments");
                if (null != attachments && attachments.length() > 0) {
                    JSONObject attachment = attachments.getJSONObject(0);
                    if (attachment != null)
                        item.setImage(attachment.getString("url"));
                }
               /* mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
*/


/////////////////////
    /*  for static images
    private GridAdapter gridAdapter;
    private GridView gridView;

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_home, container, false);
        gridView = (GridView) v.findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(getActivity(), R.layout.item_grid_layout, getData());
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                MovieItem item = (MovieItem) parent.getItemAtPosition(position);
                //Create intent
                Toast.makeText(getActivity(), "Show Picture " + item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                Log.e("Image", "" + item.getImage().getWidth());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }});
        return v;
    }
    //  some static data for gridview
    private ArrayList<MovieItem> getData() {
        final ArrayList<MovieItem> MovieItems = new ArrayList<>();
        TypedArray images = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < images.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.getResourceId(i, -1));
            MovieItems.add(new MovieItem(bitmap, "Image Number" + i));
        }
        return MovieItems;
    }*/

