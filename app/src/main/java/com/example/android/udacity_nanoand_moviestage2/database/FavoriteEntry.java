package com.example.android.udacity_nanoand_moviestage2.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by gizzo_000 on 6/22/2018.
 * Note: The database maintains it's own internal record id which is independent of the MovieId
 */

@Entity(tableName = "favorite")
public class FavoriteEntry {

    @PrimaryKey @NonNull
    private String movieId;
    private String movieTitle;
    private String movieJSONString;


    public FavoriteEntry(String movieId, String movieTitle, String movieJSONString){
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieJSONString = movieJSONString;
    }
     //Getters and Setters
    public String getMovieId() { return this.movieId;}
    public void setMovieId(String newId) {   this.movieId = newId; }

    public String getMovieTitle() { return this.movieTitle;}
    public void setMovieTitle(String title) { this.movieTitle = title;}

    public String getMovieJSONString() { return this.movieJSONString;}
    public void setMovieJSONString(String jsonString) {this.movieJSONString = jsonString;}

}
