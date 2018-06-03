package com.example.android.udacity_nanoand_moviestage2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacity_nanoand_moviestage2.utilities.DataUtilities;
import com.example.android.udacity_nanoand_moviestage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
/*
TO DO
[ ] onCreate = check my custom database to see if data for this movie already exists.
    if it does, else load it, and when it loads it, store it in the database

To fetch trailers request to the /movie/{id}/videos endpoint.
To fetch reviews request to the /movie/{id}/reviews endpoint
 */

    private static final int MOVIE_DETAIL_LOADER_ID= 32;
    private static final String TAG = "GGG";

    private  TextView title_tv;
    private  ImageView posterImageView;
    private   TextView year_tv;
    private  TextView summary_tv;
    private  TextView popularity_tv;
    private  TextView voteave_tv;

    private String movieId = "";
    private String detailData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Context context = getBaseContext();

        /*
        CHECK CUSTOM DB FOR DATA. IF CURRENT MOVIEID EXISTS THERE, USE THAT DATA INSTEAD OF TRIGGERING LOAD
         */
        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, DetailActivity.this );


        //setTitle(intent.getStringExtra("title"));
        if (title_tv == null)   title_tv = findViewById(R.id.title_tv);
        title_tv.setText(intent.getStringExtra("title"));

        String posterPath = intent.getStringExtra("backdrop_path");
        String imurl = "https://image.tmdb.org/t/p/w780" + posterPath;
        if (posterImageView == null) posterImageView =  findViewById(R.id.poster_iv);
        Picasso.with(context).load(imurl).into(posterImageView);

        if (year_tv == null) year_tv =  findViewById(R.id.year_tv);
        String releaseText = getString(R.string.yearReleased_label) + DataUtilities.getFormattedDate(intent.getStringExtra("release_date"));
        year_tv.setText(releaseText);

        if (summary_tv == null) summary_tv =  findViewById(R.id.summary_tv);
        summary_tv.setText(intent.getStringExtra("overview"));

        if (popularity_tv == null) popularity_tv =  findViewById(R.id.popularity_tv);
        popularity_tv.setText(String.valueOf(intent.getDoubleExtra("popularity",0))) ;

        if (voteave_tv == null) voteave_tv =  findViewById(R.id.voteave_tv);
        voteave_tv.setText(String.valueOf(intent.getDoubleExtra("vote_average",0))) ;
        //Put Movie Title in Detail Title
        movieId = intent.getStringExtra("id");
        //Load Videos and Reviews data

    }
    // ADDED FOR LOADERMANAGER
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        //Start the loader in the background
        return  new  AsyncTaskLoader<String>(this ) {

            @Override
            public String loadInBackground() {
                URL searchUrl = NetworkUtils.buildVideosURL(movieId);
                try {
                    return NetworkUtils
                            .getResponseFromHttpUrl(searchUrl);
                } catch (Exception e) {
                    Log.i(TAG, "loadInBackground: ERROR calling getReponseFromHttpUrl ");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {

                if (detailData != null) {
                    //already have data
                    deliverResult(detailData);
                } else {
                   // loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            public void deliverResult(String data) {

                if (data == null) {
                    Log.i(TAG, "*** Detail-Dataloaded: len=0 (null) ");
                } else {
                    Log.i(TAG, "*** Detail-Dataloaded: len="+data.length());
                    detailData = data;
                    //Turn it to a json object:
                }

                super.deliverResult(data);

            }
        };
    }
    // ADDED FOR LOADERMANAGER
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        //When loader is done, deal with results
        if (data == null){
            Log.i(TAG, "onLoadFinished: data len=0 null");
        } else {
            Log.i(TAG, "onLoadFinished: data len=" + data.length()+ " "+data);
        }
    }
    // ADDED FOR LOADERMANAGER
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
