package com.example.android.udacity_nanoand_moviestage2;

//import android.content.Context;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
//import android.net.Uri;
import android.os.PersistableBundle;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.udacity_nanoand_moviestage2.database.AppDatabase;
import com.example.android.udacity_nanoand_moviestage2.database.FavoriteEntry;
import com.example.android.udacity_nanoand_moviestage2.utilities.NetworkUtils;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<String>, MovieRecyclerAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = "MAINACTIVITY";
    private String mMovieData = null;
    private static final int MOVIE_LOADER_ID= 22;
    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private int sortBy=1; //1=top rated, 2=popularity, 3=favorites
    private ProgressBar loadingIndicator;
    private TextView errorMessageDisplay;
    RadioGroup mRadioGroup;
    private AppDatabase mDb;
    private String favoritesJSONData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("favorites")){
                favoritesJSONData = savedInstanceState.getString("favorites");
            }
            if (savedInstanceState.containsKey("sortby")){
                sortBy = savedInstanceState.getInt("sortby");
            }
            if (savedInstanceState.containsKey("mMovieData")){
                mMovieData = savedInstanceState.getString("mMovieData");
            }
            Log.d(TAG, "onRestoreInstanceState: sortby="+sortBy+" favoritesJSONData="+favoritesJSONData);

        } else {
            favoritesJSONData = "";
            sortBy = 1;
        }
        setContentView(R.layout.activity_main);

        //Setup NetworkUtils with context so it can read the api key from res/values/keys.xml
        NetworkUtils.setup(this);
        errorMessageDisplay =  findViewById(R.id.tv_error_message_display);
        loadingIndicator =  findViewById(R.id.pb_loading_anim);
        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int whatId) {

                switch(whatId){
                    case R.id.radButton1: //Top Rated

                        Log.d(TAG, "onCheckedChanged: RadioGroup Changed button 1 sortBy=" +sortBy);
                        if (sortBy != 1) {
                            sortBy = 1;
                            mMovieData = null;
                            invalidateData();
                            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
                        }
                        break;
                    case R.id.radButton2: //Popularity
                        Log.d(TAG, "onCheckedChanged: RadioGroup Changed button 2 sortBy=" +sortBy);
                        if (sortBy != 2) {
                            sortBy = 2;
                            mMovieData = null;
                            invalidateData();
                            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
                        }
                        break;
                    case R.id.radButton3: //Favorites
                        Log.d(TAG, "onCheckedChanged: RadioGroup Changed button 3. favoritesJSONData=" + favoritesJSONData +"  sortBy=" +sortBy);
                        if (sortBy != 3) {
                            sortBy = 3;
                            //get favorites data as JSON
                            // mMovieData = JSONDATA
                            mMovieData = favoritesJSONData;
                            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
                        }
                        break;
                    default:
                        Log.d(TAG, "onCheckedChanged: RadioGroup Changed (DEFAULT) whatid=" + whatId);
                        sortBy = 0;
                        break;

                }

            }
        });

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
    @Override
    public @NonNull Loader<String> onCreateLoader(int i, Bundle bundle) {
        //Start the loader in the background
        return  new  AsyncTaskLoader<String>(this ) {

            @Override
            public String loadInBackground() {
                URL searchUrl;
                switch(sortBy){
                    case 1:
                        searchUrl = NetworkUtils.buildTopRatedURL();
                    break;
                    case 2:
                        searchUrl = NetworkUtils.buildPopularURL();
                        break;

                    default:
                        searchUrl = null;
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
                Log.d(TAG, "onStartLoading: ***** onStartLoading ******");
                if (mMovieData != null) {
                    Log.d(TAG, "onStartLoading: mMovieData NOT null:" + mMovieData.length());
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
                    Log.d("GREGOUT", "#####  deliverResult #####: data.length " + data.length());
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
       Log.d("GREGOUT", "onLoaderReset: ******************* ");
    }

    private void startMovieDataLoad() {
        Log.d("GREGOUT", " ########## startMovieDataLoad: ");
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
      //  Log.d("TAG", "######onClick: "+movieData);
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
        movieDetailIntent.putExtra("json", movieData);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: favoritesJSONData="+favoritesJSONData);
        outState.putString("favorites",favoritesJSONData );
        outState.putInt("sortby", sortBy);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("favorites")){
            favoritesJSONData = savedInstanceState.getString("favorites");
        }
        if (savedInstanceState.containsKey("sortby")){
            sortBy = savedInstanceState.getInt("sortby");
        }
        Log.d(TAG, "onRestoreInstanceState: sortby="+sortBy+" favoritesJSONData="+favoritesJSONData);
    }

    private void retrieveFavorites(){
        //Using LiveData instead of Executor because LiveData runs in a separate thread, and will keep track of changes

        final LiveData<List<FavoriteEntry>> favorites = mDb.favoriteDao().loadAllFavorites();
        favorites.observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Log.d(TAG, "Actively retrieving the favorites from the DataBase");
                setFavoritesJSONdata(favoriteEntries);
            }
        });
    }
    private void setFavoritesJSONdata(List<FavoriteEntry> favoriteEntries) {
        Log.d(TAG, "setFavoritesJSONdata: *** CLEARING favoritesJSONData");
        favoritesJSONData = "";
        String innerData = "";
        Iterator<FavoriteEntry> iterator = favoriteEntries.iterator();
        while (iterator.hasNext()) {
            if (innerData == "") {
                innerData = iterator.next().getMovieJSONString();
            } else {
                innerData = innerData + "," +  iterator.next().getMovieJSONString();
            }
        }
        favoritesJSONData = " { \"results\" : [" + innerData + "] }";

        //combine json data from each entry into a json string of the form:
        //   { "results" : [
        //                   <entryJSONSTRING 1>  ,
        //                   <entryJSONSTRING 2> ,
        //                   ...
        //                  ]
        //   }
    }

}
