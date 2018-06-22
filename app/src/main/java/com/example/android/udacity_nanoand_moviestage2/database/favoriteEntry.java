package com.example.android.udacity_nanoand_moviestage2.database;

/**
 * Created by gizzo_000 on 6/22/2018.
 */

public class favoriteEntry {
    private int id;
    private String movieId;
    private String movieTitle;

    public favoriteEntry(String id, String title){
        this.movieId = id;
        this.movieTitle = title;
    }
    public favoriteEntry(int favId, String id, String title){
        this.id =favId;
        this.movieId = id;
        this.movieTitle = title;
    }
    public int getId() {return this.id; }
    public void setId(int newId) { this.id = newId;}

    public String getMovieId() { return this.movieId;}
    public void setMovieId(String newId) {   this.movieId = newId; }

}
