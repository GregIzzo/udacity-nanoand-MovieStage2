package com.example.android.udacity_nanoand_moviestage2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacity_nanoand_moviestage2.utilities.DataUtilities;
import com.example.android.udacity_nanoand_moviestage2.utilities.NetworkUtils;
import com.example.android.udacity_nanoand_moviestage2.utilities.ReadAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import javax.security.auth.login.LoginException;

public class DetailActivity extends AppCompatActivity implements VideoRecyclerAdapter.VideoAdapterOnClickHandler, ReviewRecyclerAdapter.ReviewAdapterOnClickHandler {
/*
TO DO
[ ] onCreate = check my custom database to see if data for this movie already exists.
    if it does, else load it, and when it loads it, store it in the database

To fetch trailers request to the /movie/{id}/videos endpoint.
To fetch reviews request to the /movie/{id}/reviews endpoint
 */

    private static final int MOVIE_DETAIL_LOADER_ID= 32;
    private static final int VIDEO_DETAIL_LOADER_ID=42;
    private static final int REVIEW_DETAIL_LOADER_ID=52;

    private static final String TAG = "GGG";

    private  TextView title_tv;
    private  ImageView posterImageView;
    private  TextView year_tv;
    private  TextView summary_tv;
    private  TextView popularity_tv;
    private  TextView voteave_tv;

    private String movieId = "";
    private String detailData;
    private JSONObject videoJSON;
    private JSONObject reviewJSON;

    private RecyclerView mVideoRecyclerView;
    private VideoRecyclerAdapter videoRecyclerAdapter;

    private RecyclerView mReviewRecyclerView;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Context context = getBaseContext();

        //Put Movie Title in Detail Title
        movieId = intent.getStringExtra("id");
        /*
        CHECK CUSTOM DB FOR DATA. IF CURRENT MOVIEID EXISTS THERE, USE THAT DATA INSTEAD OF TRIGGERING LOAD
         */
        //getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, DetailActivity.this );

        //Get ImageView on detail screen
        mVideoRecyclerView =  findViewById(R.id.rv_videos);

        RecyclerView.LayoutManager mVideoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false); //new GridLayoutManager(this, 1);
        mVideoRecyclerView.setLayoutManager(mVideoLayoutManager);
        videoRecyclerAdapter = new VideoRecyclerAdapter(this );

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mVideoRecyclerView.setAdapter(videoRecyclerAdapter);

        //Reviews
        mReviewRecyclerView =  findViewById(R.id.rv_reviews);

        RecyclerView.LayoutManager mReviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //new GridLayoutManager(this, 1);
        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(this );

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mReviewRecyclerView.setAdapter(reviewRecyclerAdapter);

        //
        // VIDEO DATA LOAD
        //
        getSupportLoaderManager().initLoader(VIDEO_DETAIL_LOADER_ID, null, new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(final int id, @Nullable final Bundle args) {
                return new ReadAPI(DetailActivity.this , NetworkUtils.buildVideosURL(movieId) );
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String data) {
                if (data == null){
                    return;
                }
                Log.i(TAG, "onLoadFinished: VideoData="+data);
                try {
                    videoJSON = new JSONObject(data);
                    videoRecyclerAdapter.setVideoData(data);
                    //showMovieDataView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {
            }
        }).forceLoad();

        //
        // REVIEWS DATA LOAD
        //
        getSupportLoaderManager().initLoader(REVIEW_DETAIL_LOADER_ID, null, new LoaderManager.LoaderCallbacks<String>() {
            @NonNull
            @Override
            public Loader<String> onCreateLoader(final int id, @Nullable final Bundle args) {
                return new ReadAPI(DetailActivity.this , NetworkUtils.buildReviewsURL(movieId) );
            }

            @Override
            public void onLoadFinished(@NonNull Loader<String> loader, String data) {
                if (data == null){
                    return;
                }
                Log.i(TAG, "onLoadFinished: ReviewData="+data);
                try {
                    reviewJSON = new JSONObject(data);
                    reviewRecyclerAdapter.setReviewData(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<String> loader) {
            }
        }).forceLoad();

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

        //Load Videos and Reviews data

    }

    @Override
    public void onClick(int type, String movieData) throws JSONException {
        Log.i(TAG, "onClick: type="+ type+" data=" + movieData);
        JSONObject reader = new JSONObject(movieData);
        switch(type){
            case 1:
                String videoKey = reader.getString("key");
                Log.i(TAG, "name: " +reader.getString("name")+ " key="+ videoKey);
                //try to play video
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videoKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +videoKey));
                //now check if intents will be picked up by another app
                PackageManager packageManager = getPackageManager();
                //  List<ResolveInfo> activities = packageManager.queryIntentActivities(appIntent, PackageManager.MATCH_DEFAULT_ONLY);
                if (appIntent.resolveActivity(getPackageManager()) != null){
                    //something will handle it
                    Log.i(TAG, "onClick: ### OKAY FROM PACKAGE MANAGEER - YouTube App Available ################");
                    try{
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex){
                        //app intent didn't work, try web
                        /// getApplicationContext().startActivity(webIntent);
                        Log.i(TAG, "onClick: **** *** youtube intent errored "+ex.toString());

                    }
                } else {
                    //try web
                    Log.i(TAG, "onClick: ### OKAY FROM PACKAGE MANAGEER - Web Available ################");
                    try{
                        startActivity(webIntent);
                    } catch (ActivityNotFoundException ex){
                        //app intent didn't work,
                        Log.i(TAG, "onClick: **** *** Failed to start WebIntent ACtivity: "+ex.toString());

                    }
                }
            break;
            case 2:
                //author, content, id, url
                String reviewKey = reader.getString("id");
                String author = reader.getString("author");
                Log.i(TAG, "** REVIEW *** author: " +author+ " id="+ reviewKey);
                /*
                //try to play video
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videoKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +videoKey));
                //now check if intents will be picked up by another app
                PackageManager packageManager = getPackageManager();
                //  List<ResolveInfo> activities = packageManager.queryIntentActivities(appIntent, PackageManager.MATCH_DEFAULT_ONLY);
                if (appIntent.resolveActivity(getPackageManager()) != null){
                    //something will handle it
                    Log.i(TAG, "onClick: ### OKAY FROM PACKAGE MANAGEER ################");
                    try{
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex){
                        //app intent didn't work, try web
                        /// getApplicationContext().startActivity(webIntent);
                        Log.i(TAG, "onClick: **** *** youtube intent errored "+ex.toString());

                    }
                }
                */
                break;


        }

      
    }
    /*
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
    */
}
