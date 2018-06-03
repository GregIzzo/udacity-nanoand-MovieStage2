package com.example.android.udacity_nanoand_moviestage2.utilities;

import android.content.Context;
import android.net.Uri;
//import android.provider.Settings;
//import android.util.Log;

import com.example.android.udacity_nanoand_moviestage2.R ;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {
    private static final String MOVIE_BASE_URL=
            "http://api.themoviedb.org/3/movie";

    private static final String MOVIE_POPULAR_BASE_URL=
            MOVIE_BASE_URL + "/popular";

    private static final String MOVIE_TOPRATED_BASE_URL=
            MOVIE_BASE_URL + "/top_rated";

    private static final String MOVIE_VIDEOS_SUFFIX =
            "/videos";
    private static final String MOVIE_REVIEWS_SUFFIX =
            "/reviews";
    private static  String APIKEY = "";

    private static final String APIKEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE_VALUE = "en-US";
    //private static final String PAGE_PARAM = "page";
    private static final String REGION_PARAM = "region";
    private static final String REGION_VALUE = "US";//Must be Upper Case

    public static void setup(Context inContext){
        //Need context in order to read keys.xml in the res/values folder
        APIKEY = inContext.getString(R.string.themoviedb);
    }
    public static URL buildPopularURL(){
        Uri builtUri = Uri.parse(MOVIE_POPULAR_BASE_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, APIKEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .appendQueryParameter(REGION_PARAM, REGION_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildTopRatedURL(){
        Uri builtUri = Uri.parse(MOVIE_TOPRATED_BASE_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, APIKEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .appendQueryParameter(REGION_PARAM, REGION_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildVideosURL(String id){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL +"/"+ id + MOVIE_VIDEOS_SUFFIX ).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, APIKEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .appendQueryParameter(REGION_PARAM, REGION_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildReviewsURL(String id){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL +"/"+ id + MOVIE_REVIEWS_SUFFIX ).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, APIKEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                .appendQueryParameter(REGION_PARAM, REGION_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        //based on NetworkUtils.java from Udacity Google Challenge S05.01
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            InputStream inStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
