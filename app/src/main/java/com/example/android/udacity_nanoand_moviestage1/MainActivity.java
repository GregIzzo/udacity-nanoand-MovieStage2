package com.example.android.udacity_nanoand_moviestage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
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

import com.example.android.udacity_nanoand_moviestage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<String>, MovieRecyclerAdapter.MovieAdapterOnClickHandler {
   // ImageView poster_iv;
   // ImageView poster2_iv;
    Context mainContext;
    String mMovieData = null;
    public static final int MOVIE_LOADER_ID= 22;
    RecyclerView mRecyclerView;
    MovieRecyclerAdapter movieRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup NetworkUtils with context so it can read the api key from res/values/keys.xml
        NetworkUtils.setup(this);
        mainContext = this;
        setContentView(R.layout.activity_main);
        //Get ImageView on main screen (activity_main)
        mRecyclerView =  findViewById(R.id.rv_movies);

      //  LinearLayoutManager layoutManager
      //          = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(mLayoutManager);

        movieRecyclerAdapter = new MovieRecyclerAdapter(this );

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(movieRecyclerAdapter);


       // poster_iv =  findViewById(R.id.poster_iv);
        // LoaderCallbacks<String[]> callback = MainActivity.this;
        LoaderCallbacks<String> callbacks = MainActivity.this;
        Log.i("GREGOUT", "onCreate: ---STARTING");
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callbacks );

        startMovieDataLoad();
    }

    @Override
    public @NonNull Loader<String> onCreateLoader(int i, Bundle bundle) {
        //Start the loader in the background
        Log.i("GREGOUT", "onCreateLoader:************* ");
        return new AsyncTaskLoader<String>(this ) {
           // String mMovieData = null;

            @Override
            public String loadInBackground() {
                URL moviePopularURL = NetworkUtils.buildPopularURL();
                URL movieTopRatedURL = NetworkUtils.buildTopRatedURL();
                Log.i("GREGOUT", "#####  loadInBackground: ##### url = "+ movieTopRatedURL.toString());
                try {
                    //return NetworkUtils
                    //        .getResponseFromHttpUrl(moviePopularURL);
                    return NetworkUtils
                            .getResponseFromHttpUrl(movieTopRatedURL);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }
            public void deliverResult(String data) {
                mMovieData = data;
                //Turn it to a json object:
                try {
                    JSONObject reader = new JSONObject(mMovieData);
                    JSONArray resArray = reader.getJSONArray("results");
                    Log.i("GREGOUT","=== COUNT = "+resArray.length());
                    //Load image from first object:
 /*
                    for (int i = 0; i < resArray.length(); i++) {
                        JSONObject d = resArray.getJSONObject(i);
                        Log.i("GREGOUT", "title:"+d.getString("title")+" popularity: "+d.getString("popularity")+" vote ave:"+d.getString("vote_average"));
                        if (i == 0){
                            JSONObject firstObj = resArray.getJSONObject(i);
                            String posterPath = firstObj.getString("poster_path");
                            String imurl = "https://image.tmdb.org/t/p/w500" + posterPath;
                            Picasso.with(mainContext).load(imurl).into(poster_iv);
                        }

                        if (i == 1){
                            JSONObject firstObj = resArray.getJSONObject(i);
                            String posterPath = firstObj.getString("poster_path");
                            String imurl = "https://image.tmdb.org/t/p/w500" + posterPath;
                            Picasso.with(mainContext).load(imurl).into(poster2_iv);
                        }


                    }
                    */
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        //When loader is done, deal with results
        Log.i("GREGOUT", "!!!! WOOT !!!! onLoadFinished: ***" +s+ "***");
        movieRecyclerAdapter.setMovieData(s);
        showMovieDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.i("GREGOUT", "onLoaderReset: ******************* ");
    }

    private void startMovieDataLoad() {
        Log.i("GREGOUT", " ########## startMovieDataLoad: ");
        // Create a bundle called queryBundle
        Bundle queryBundle = new Bundle();
        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //url value here is https://jsonplaceholder.typicode.com/posts
       ///// queryBundle.putString(OPERATION_QUERY_URL_EXTRA,url);
        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(MOVIE_LOADER_ID);
        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null){
            loaderManager.initLoader(MOVIE_LOADER_ID, queryBundle, this);
        }else{
            loaderManager.restartLoader(MOVIE_LOADER_ID, queryBundle, this);
        }
    }

    @Override
    public void onClick(String movieData) throws JSONException {
        Log.i("TAG", "######onClick: "+movieData);
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        //Movie details layout contains
        // //title,
        // //release date,
        // //movie poster,
        // //vote average, and
        // //plot synopsis.
        /*
        "title": "Dilwale Dulhania Le Jayenge",
        "release_date": "1995-10-20"
        "poster_path": "\/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
        "vote_average": 9.2,
		"popularity": 15.778658,
		"overview": "Raj is a rich, caref..."

         */
        JSONObject reader = new JSONObject(movieData);
        movieDetailIntent.putExtra("title",reader.getString("title") );
        movieDetailIntent.putExtra("release_date",reader.getString("release_date") );
        movieDetailIntent.putExtra("backdrop_path",reader.getString("backdrop_path") );
        movieDetailIntent.putExtra("vote_average",reader.getDouble("vote_average") );
        movieDetailIntent.putExtra("popularity",reader.getDouble("popularity") );
        movieDetailIntent.putExtra("overview",reader.getString("overview") );
        startActivity(movieDetailIntent);

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
       // mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
