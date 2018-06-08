package com.example.android.udacity_nanoand_moviestage2.utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.URL;

/**
 * Created by gizzo_000 on 6/7/2018.
 * This class is based on the example found here:
 * https://stackoverflow.com/questions/20279216/asynctaskloader-basic-example-android
 */

public class ReadAPI extends AsyncTaskLoader<String> {
    private static final String TAG = "GGG";
    private URL mTargetUrl;
    public ReadAPI (Context context, URL targetUrl){
        super(context);
        mTargetUrl = targetUrl;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        if (mTargetUrl != null){
            try {
                return NetworkUtils
                        .getResponseFromHttpUrl(mTargetUrl);
            } catch (Exception e) {
                Log.i(TAG, "GGGGGG ReadApi.LoadInBackground: ERROR calling getReponseFromHttpUrl ");
                e.printStackTrace();
                return null;
            }
        } else {
            Log.i(TAG, "GGG loadInBackground: mTargetUrl is null");
            return "";
        }
    }
}
