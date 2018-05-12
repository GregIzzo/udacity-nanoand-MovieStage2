package com.example.android.udacity_nanoand_moviestage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.android.udacity_nanoand_moviestage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<String> {
    ImageView poster_iv;
    ImageView poster2_iv;
    Context mainContext;
    public static final int MOVIE_LOADER_ID= 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup NetworkUtils with context so it can read the api key from res/values/keys.xml
        NetworkUtils.setup(this);
        mainContext = this;
        setContentView(R.layout.activity_main);
        //Get ImageView on main screen (activity_main)
        poster_iv =  findViewById(R.id.poster_iv);
        poster2_iv = findViewById(R.id.poster_iv2);
        //Load an image into it
        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(poster_iv);
        //////
        // LoaderCallbacks<String[]> callback = MainActivity.this;
        LoaderCallbacks<String> callbacks = MainActivity.this;
        Log.i("GREGOUT", "onCreate: ---STARTING");
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callbacks );

        startMovieDataLoad();
    }

    @Override
    public @NonNull Loader<String> onCreateLoader(int i, Bundle bundle) {
        //Start the loader in the backgroun
        Log.i("GREGOUT", "onCreateLoader:************* ");
        return new AsyncTaskLoader<String>(this ) {
            String mMovieData = null;

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
}
