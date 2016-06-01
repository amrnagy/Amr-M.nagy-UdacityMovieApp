package com.amr.udacitymovieapp.Models;

/**
 * Created by Amr on 5/28/2016.
 */
public class MovieItem {
private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String title;
    private String date;
    public String overview;
    private double voting_average;
    private String path;

    public MovieItem(String id,String path, String title, String date, String overview, double voting_average) {
       this.id=id;
        this.path = path;
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.voting_average = voting_average;
    }

    public MovieItem(String path, String title) {
        super();
        this.title = title;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_average() {
        return voting_average;
    }

    public void setVote_average(double vote_average) {
        this.voting_average = vote_average;
    }

    /*
    // to handle image
    private String image;
    private String title;

    public MovieItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    */



/* for static image
    private Bitmap image;
    private String title;


    public MovieItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
*/
}
