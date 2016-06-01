package com.amr.udacitymovieapp.sData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amr.udacitymovieapp.Models.MovieItem;

import java.util.ArrayList;

/**
 * Created by Ismael on 15/06/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "TopMovie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE favourite_movies( id  TEXT PRIMARY KEY, " +
                "title TEXT NOT NULL ,path TEXT, overview TEXT, vote DOUBLE, prod_date TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favourite_movies");
        onCreate(db);
    }
    //Insert data into table
    public boolean insertData(String id,String title,String path,String overview,double vote,
                           String prod_date){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean available = getMovieById(id);
        if (available == true) {

            ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("path", path);
        contentValues.put("overview", overview);
        contentValues.put("vote", vote);
        contentValues.put("prod_date", prod_date);

        db.insert("favourite_movies", null, contentValues);
        db.close();
            return true;
        } else
            return false;


    }

    //Select all data from the table
    public ArrayList getMovies() {
        ArrayList<MovieItem> favourite = new ArrayList<MovieItem>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from favourite_movies";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String id=cursor.getString(0);
            String title = cursor.getString(1);
            String poseterPath = cursor.getString(2);
            String overview = cursor.getString(3);
            double vote_average = cursor.getDouble(4);
            String date = cursor.getString(5);
            MovieItem it = new MovieItem(id,poseterPath, title, date, overview, vote_average);
            favourite.add(it);
        }
        db.close();
        return favourite;
    }



    //Select data for the given id
    private boolean getMovieById(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM favourite_movies WHERE id = ?";
        String m[] = {String.valueOf(movieId)};
        Cursor cursor = db.rawQuery(query, m);
        if (cursor.moveToFirst()) {
            db.close();
            return false;
        } else
            return true;
    }

}
