package com.example.android.udacity_nanoand_moviestage1;

//import android.content.Context;
import android.content.Intent;
//import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SwitchCompat;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
//import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.udacity_nanoand_moviestage1.utilities.NetworkUtils;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<String>, MovieRecyclerAdapter.MovieAdapterOnClickHandler, CompoundButton.OnCheckedChangeListener {
   // ImageView poster_iv;
   // ImageView poster2_iv;

    private String mMovieData = null;
    private static final int MOVIE_LOADER_ID= 22;
    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    //private Toolbar mTopToolbar;
    //private ActionBar mActionBar;
    private boolean sortByPopular = false;
    private ProgressBar loadingIndicator;
    private TextView errorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Setup NetworkUtils with context so it can read the api key from res/values/keys.xml
        NetworkUtils.setup(this);
        //Setup Toolbar/Action bar which has a button for changing sort: Popularity vs top_rated

        /* TextView used to display errors. Invisible until error */
        errorMessageDisplay =  findViewById(R.id.tv_error_message_display);
        loadingIndicator =  findViewById(R.id.pb_loading_anim);
        // Menu is inflated in the method 'onCreateOptionsMenu'

//        mTopToolbar =  findViewById(R.id.action_sort);
//        setSupportActionBar(mTopToolbar);//Creates the toolbar
//       mActionBar =  getSupportActionBar();//need this to process interactions with the bar


//       SwitchCompat sortToggle = (SwitchCompat) findViewById(R.id.sortSwitchForActionBar);
// /       if (sortToggle != null)
//            sortToggle.setOnCheckedChangeListener(this);

        ToggleButton sortSelectButton = findViewById(R.id.toggle_button);
        if (sortSelectButton != null) {
           // sortSelectButton.setOnCheckedChangeListener(this);
            sortSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   Log.i("GGG", "onClick: "+((ToggleButton)view).isChecked());
                   sortByPopular =  ((ToggleButton)view).isChecked();
                    mMovieData = null;
                    invalidateData();
                   // startMovieDataLoad();

                    getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this );
                }
            });
        }


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
        //LoaderCallbacks<String> callbacks = MainActivity.this;
      //  Log.i("GREGOUT", "onCreate: ---STARTING");
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, MainActivity.this );

        //startMovieDataLoad();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("GGG", "------ ------ ----- onCreateOptionsMenu: ");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.myswitch);
        item.setActionView(R.layout.switch_layout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("GGGG", "@@@@@@ onOptionsItemSelected: ");
        switch (item.getItemId()) {
            case R.id.sortSwitchForActionBar://  R.id.action_sort:
                Log.i("GGGG", "@@@@@@ action_sort ");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    public @NonNull Loader<String> onCreateLoader(int i, Bundle bundle) {
        //Start the loader in the background
       // Log.i("GREGOUT", "onCreateLoader:************* sort= "+sortByPopular);
        return  new  AsyncTaskLoader<String>(this ) {
           // String mMovieData = null;

            @Override
            public String loadInBackground() {
                URL searchUrl;
                if (sortByPopular){
                    searchUrl = NetworkUtils.buildPopularURL();

                } else {
                    searchUrl = NetworkUtils.buildTopRatedURL();
                }
                ///URL moviePopularURL = NetworkUtils.buildPopularURL();
                ////URL movieTopRatedURL = NetworkUtils.buildTopRatedURL();
            //    Log.i("GREGOUT", "#####  loadInBackground: ##### url = "+ searchUrl.toString());
                try {
                    //return NetworkUtils
                    //        .getResponseFromHttpUrl(moviePopularURL);
                    return NetworkUtils
                            .getResponseFromHttpUrl(searchUrl);

                } catch (Exception e) {
            //        Log.i("GREGOUT", "#####  loadInBackgroundERROR: ##### "+ e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
              //  Log.i("GREGOUT", "#####  onStartLoading ##### ");
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            public void deliverResult(String data) {
                if (data == null) {
                 //   Log.i("GREGOUT", "#####  deliverResult #####: data.length=0 (null) " );
                } else {
                 //   Log.i("GREGOUT", "#####  deliverResult #####: data.length " + data.length());
                    mMovieData = data;
                    //Turn it to a json object:

                    /*
                    try {
                        JSONObject reader = new JSONObject(mMovieData);
                        JSONArray resArray = reader.getJSONArray("results");
                       // Log.i("GREGOUT", "=== COUNT = " + resArray.length());
                        //Load image from first object:
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                }
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        //When loader is done, deal with results

      //  Log.i("GREGOUT", "onLoadFinished-- TURN OFF LOADING INDICATOR: ");
        if (s == null) {
       //     Log.i("GREGOUT", "!!!! WOOOT !!!! onLoadFinished: ***string len=0 (null)***");
            showErrorMessage();
            loadingIndicator.setVisibility(View.GONE);
        } else {
        //    Log.i("GREGOUT", "!!!! WOOOT !!!! onLoadFinished: ***string len=" + s.length() + "***");
            movieRecyclerAdapter.setMovieData(s);
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
      //  Log.i("GREGOUT", "onLoaderReset: ******************* ");
    }

    private void startMovieDataLoad() {
        Log.i("GREGOUT", " ########## startMovieDataLoad: ");
        // Create a bundle called queryBundle
        //Bundle queryBundle = null;//new Bundle();
        // Use putString with OPERATION_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        //url value here is https://jsonplaceholder.typicode.com/posts
       ///// queryBundle.putString(OPERATION_QUERY_URL_EXTRA,url);
        // Call getSupportLoaderManager and store it in a LoaderManager variable
        LoaderManager loaderManager = getSupportLoaderManager();
        // Get our Loader by calling getLoader and passing the ID we specified
        Loader<String> loader = loaderManager.getLoader(MOVIE_LOADER_ID);
        // If the Loader was null, initialize it. Else, restart it.
        if(loader==null){
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }else{
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public void onClick(String movieData) throws JSONException {
      //  Log.i("TAG", "######onClick: "+movieData);
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
    private void invalidateData() {
        //mRecyclerView.invalidate();
        movieRecyclerAdapter.setMovieData(null);
    }

    private void showMovieDataView() {
        /* First, hide error (turn invisible) */
       errorMessageDisplay.setVisibility(View.GONE);
        /* show Recycler view */
        mRecyclerView.setVisibility(View.VISIBLE);
        /* turn off loading anim */
        loadingIndicator.setVisibility(View.GONE);
    }
    private void showErrorMessage(){
        /* Hide RecyclerView */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Show error */
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
      //  Log.i("GGG", " ACTION SWITCH CLICKED:" + b+ " button = "+ compoundButton.isChecked());
        sortByPopular = b;
        mMovieData = null;
        invalidateData();
        //startMovieDataLoad();

        // getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this );
    }

}
