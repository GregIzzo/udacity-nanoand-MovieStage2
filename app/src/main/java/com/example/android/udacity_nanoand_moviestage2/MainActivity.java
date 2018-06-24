package com.example.android.udacity_nanoand_moviestage2;

//import android.content.Context;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
//import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.android.udacity_nanoand_moviestage2.database.AppDatabase;
import com.example.android.udacity_nanoand_moviestage2.database.FavoriteEntry;
import com.example.android.udacity_nanoand_moviestage2.utilities.NetworkUtils;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<String>, MovieRecyclerAdapter.MovieAdapterOnClickHandler, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MAINACTIVITY";
    private String mMovieData = null;
    private static final int MOVIE_LOADER_ID= 22;
    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private boolean sortByPopular = false;
    private ProgressBar loadingIndicator;
    private TextView errorMessageDisplay;
    private AppDatabase mDb;

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

        ToggleButton sortSelectButton = findViewById(R.id.toggle_button);
        if (sortSelectButton != null) {
           // sortSelectButton.setOnCheckedChangeListener(this);
            sortSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   sortByPopular =  ((ToggleButton)view).isChecked();
                    mMovieData = null;
                    invalidateData();
                    getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this );
                }
            });
        }


        //Get ImageView on main screen (activity_main)
        mRecyclerView =  findViewById(R.id.rv_movies);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movieRecyclerAdapter = new MovieRecyclerAdapter(this );

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(movieRecyclerAdapter);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, MainActivity.this );

        mDb = AppDatabase.getsInstance(getApplicationContext());
        retrieveFavorites();
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
        return  new  AsyncTaskLoader<String>(this ) {

            @Override
            public String loadInBackground() {
                URL searchUrl;
                if (sortByPopular){
                    searchUrl = NetworkUtils.buildPopularURL();

                } else {
                    searchUrl = NetworkUtils.buildTopRatedURL();
                }
                try {
                     return NetworkUtils
                            .getResponseFromHttpUrl(searchUrl);

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

                }
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        //When loader is done, deal with results

        if (s == null) {
            showErrorMessage();
            loadingIndicator.setVisibility(View.GONE);
        } else {
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
        JSONObject reader = new JSONObject(movieData);
        movieDetailIntent.putExtra("id",reader.getString("id") );
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
        sortByPopular = b;
        mMovieData = null;
        invalidateData();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void retrieveFavorites(){
        //Using LiveData instead of Executor because LiveData runs in a separate thread, and will keep track of changes

        final LiveData<List<FavoriteEntry>> favorites = mDb.favoriteDao().loadAllFavorites();
        favorites.observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Log.d(TAG, "Actively retrieving the favorites from the DataBase");
                //mAdapter.setFavorites(favoriteEntries);
            }
        });
    }

}
