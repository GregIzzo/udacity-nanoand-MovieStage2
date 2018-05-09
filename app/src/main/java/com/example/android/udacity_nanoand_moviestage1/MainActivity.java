package com.example.android.udacity_nanoand_moviestage1;

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

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<String> {
    ImageView poster_iv;

    public static final int MOVIE_LOADER_ID= 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get ImageView on main screen (activity_main)
        poster_iv = (ImageView) findViewById(R.id.poster_iv);
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
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        //Start the loader in the backgroun
        Log.i("GREGOUT", "onCreateLoader:************* ");
        return new AsyncTaskLoader<String>(this ) {
            String mMovieData = null;

            @Override
            public String loadInBackground() {
                URL moviePopularURL = NetworkUtils.buildPopularURL();
                Log.i("GREGOUT", "#####  loadInBackground: ##### url = "+ moviePopularURL.toString());
                try {
                    String jsonMovieData = NetworkUtils
                            .getResponseFromHttpUrl(moviePopularURL);

                    return jsonMovieData;
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
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        //When loader is done, deal with results
        Log.i("GREGOUT", "onLoadFinished: ***" +s+ "***");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
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