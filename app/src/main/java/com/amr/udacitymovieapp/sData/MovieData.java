package com.amr.udacitymovieapp.sData;

import android.net.Uri;
import android.util.Log;

import com.amr.udacitymovieapp.Models.MovieItem;
import com.amr.udacitymovieapp.Models.ReviewItem;
import com.amr.udacitymovieapp.Models.TrailerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Amr on 5/29/2016.
 */
public class MovieData {


    private final String LOG_TAG = MovieData.class.getSimpleName();
    String key = "";

    public JSONObject getMovies(String type) {

        // HttpURLConnection and BufferedReader are declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String MoviesJsonStr = null;


        try {
            // Construct the URL for the themoviedb query
            String Movies_URL = "https://api.themoviedb.org/3";
            String path = "";
            if (type.equals("popular")) {
                path = "/movie/popular";

            } else if (type.equals("top_rated")) {
                path = "/movie/top_rated";
            }

            Movies_URL += path;
            //String key = "";
            Uri builtUri = Uri.parse(Movies_URL).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .build();

            URL url = new URL(builtUri.toString());


            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to  open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }
            MoviesJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast string: " + MoviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Stream Closing Error ", e);
                }
            }
        }
        JSONObject moviesJson = null;
        try {
            moviesJson = new JSONObject(MoviesJsonStr);
            return moviesJson;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return moviesJson;
    }

    /*  private ArrayList<FilmItem> getData() {
          final ArrayList<FilmItem> filmItems = new ArrayList<>();
          TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
          for (int i = 0; i < imgs.length(); i++) {
              Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
              filmItems.add(new FilmItem(bitmap, "Image#" + i));
          }
          return filmItems;
      }*/
    public JSONObject getVideos(String id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String MoviesJsonStr = null;

        try {
            // Construct the URL for the themoviedb query
            String Movies_URL = "http://api.themoviedb.org/3/movie/" + id + "/videos";


            Uri builtUri = Uri.parse(Movies_URL).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to  open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }
            MoviesJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast string: " + MoviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        JSONObject moviesJson = null;
        try {
            moviesJson = new JSONObject(MoviesJsonStr);
            return moviesJson;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return moviesJson;
    }

    public ArrayList<TrailerItem> getAllVediosFromJson(JSONObject videos) {
        String basePath = "http://www.youtube.com/watch?v=";
        JSONArray videoArray = null;
        ArrayList<TrailerItem> allvideos = null;
        try {
            videoArray = videos.getJSONArray("results");


            allvideos = new ArrayList<>();

            for (int i = 0; i < videoArray.length(); i++) {


                JSONObject movie = videoArray.getJSONObject(i);

                String key = movie.getString("key");
                String name = movie.getString("name");
                String site = basePath + key;
                String type = movie.getString("type");
                String id = movie.getString("id");
                TrailerItem Ti = new TrailerItem(id, key, name, site, type);
                allvideos.add(Ti);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return allvideos;
    }


    public ArrayList<MovieItem> getAllMoviesFromJson(JSONObject movies) {
        String basePath = "http://image.tmdb.org/t/p/w185";
        JSONArray movieArray = null;
        ArrayList<MovieItem> allMovies = null;
        try {
            movieArray = movies.getJSONArray("results");


            allMovies = new ArrayList<>();

            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject movie = movieArray.getJSONObject(i);

                String title = movie.getString("title");
                String date = movie.getString("release_date");
                String overview = movie.getString("overview");
                double vote_average = movie.getDouble("vote_average");
                String poseterPath = basePath + movie.getString("poster_path");
                String id = movie.getString("id");
                MovieItem it = new MovieItem(id, poseterPath, title, date, overview, vote_average);
                allMovies.add(it);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return allMovies;

    }


    public JSONObject getReviews(String id) {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String ReviewsJsonStr = null;

        try {

            String Review_URL = "http://api.themoviedb.org/3/movie/" + id + "/reviews";
            String key = "1d077c24e2ef3f5e88b42da8af52ee3b";
            Uri builtUri = Uri.parse(Review_URL).buildUpon()
                    .appendQueryParameter("api_key", key)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            ReviewsJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Reviews string: " + ReviewsJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the Movies data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        JSONObject moviesJson = null;
        try {
            moviesJson = new JSONObject(ReviewsJsonStr);
            return moviesJson;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return moviesJson;
    }

    public ArrayList<ReviewItem> getAllReviewsFromJson(JSONObject reviews) {
        JSONArray reviewsJsonArray = null;
        ArrayList<ReviewItem> allReviews = null;
        try {
            reviewsJsonArray = reviews.getJSONArray("results");
            allReviews = new ArrayList<>();

            for (int i = 0; i < reviewsJsonArray.length(); i++) {

                JSONObject movie = reviewsJsonArray.getJSONObject(i);
                String id = movie.getString("id");
                String author = movie.getString("author");
                String content = movie.getString("content");
                ReviewItem RI = new ReviewItem(id, author, content);
                allReviews.add(RI);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return allReviews;
    }

}

