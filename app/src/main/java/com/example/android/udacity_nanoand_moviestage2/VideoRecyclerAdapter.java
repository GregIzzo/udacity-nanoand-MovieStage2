package com.example.android.udacity_nanoand_moviestage2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Greg on 5/15/2018.
 */
//
public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.VideoAdapterViewHolder>  {

    private static final String TAG = "VIDEOOO";
    //private int mNumberOfItems;
    private String videoJsonData;
    private final VideoAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;

    private JSONObject reader = null;
    private  JSONArray resArray=null;

    public interface VideoAdapterOnClickHandler {
        void onClick(String movieData) throws JSONException;
    }
    /*
     * Constructor for VideoRecyclerAdapter - accepts number of items to display
     */
    public VideoRecyclerAdapter(VideoAdapterOnClickHandler mclick) {
        // mNumberOfItems = numberOfItems;
        mClickHandler = mclick;
    }


    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public final ImageView listItemVideoView;

        public VideoAdapterViewHolder( View itemView) {
            super(itemView);
            listItemVideoView =  itemView.findViewById(R.id.iv_item_video);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            JSONObject jObj = null;

            if (resArray != null){
                try {
                    jObj = resArray.getJSONObject(adapterPosition);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (jObj != null){
                try {
                    mClickHandler.onClick(jObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    @NonNull
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new VideoAdapterViewHolder(view);
    }
    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the movie
     * image for the given position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param VideoAdapterViewHolder The VideoAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull VideoAdapterViewHolder VideoAdapterViewHolder, int position) {
        //set the image of the ImageView

        if (videoJsonData == null){
            return;
        }
        if (resArray == null) {
            Log.i(TAG, "GG onBindViewHolder: resArray = null");
            return;
        }
        try {
            JSONObject jObj = resArray.getJSONObject(position);// jObj
            if (jObj == null) {
                //no good
                Log.i(TAG, "GG onBindViewHolder: resArray is an object but position "+position+" returned null");
                return;
            }
           // JSONArray resultJArray = jObj.getJSONArray("results");
            String site = jObj.getString("site");
            String key = jObj.getString("key");
            String nname = jObj.getString("name");
            Log.i(TAG, " GGonBindViewHolder: POSITION="+position+ " site=" + site + " key="+ key + " name="+nname);
            VideoAdapterViewHolder.listItemVideoView.setImageResource(R.mipmap.ic_sun);
            /*
            	{
            	"id": 278,
                "results": [
                    {
                        "id": "533ec653c3a3685448000249",
                        "iso_639_1": "en",
                        "iso_3166_1": "US",
                        "key": "K_tLp7T6U1c",
                        "name": "The Shawshank Redemption - Trailer",
                        "site": "YouTube",
                        "size": 480,
                        "type": "Trailer"
                    },...
                    ]
                 }

             */


            /*
            String posterPath = jObj.getString("poster_path");
            String imurl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Picasso.with(viewGroupContext).load(imurl).into(VideoAdapterViewHolder.listItemVideoView);
            */

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    @Override
    public int getItemCount() {

        if (null == resArray){
            //  Log.i(TAG, " ** ** ** getItemCountbn null: 0");
            return 0;
        }
        //   Log.i(TAG, " ** ** ** getItemCount: "+resArray.length());
        return resArray.length();

    }

    /*

   */
    public void setVideoData(String videoData) {
        videoJsonData = videoData;
        if (videoData == null){
            reader = null;
            resArray = null;
            //   Log.i(TAG, "**** VideoRecyclerAdapter.setVideoData: str len= 0 calling notifyDataSetChanged");
        } else {
            try {
                reader = new JSONObject(videoJsonData);
                resArray = reader.getJSONArray("results");
            } catch (JSONException e) {
                //   Log.i(TAG, "^^^^^^^^^ ERROR onBindViewHolder - creating JSONObject");
                e.printStackTrace();
            }
            // Log.i(TAG, "**** VideoRecyclerAdapter.setVideoData: str len= "+movieData.length() + " calling notifyDataSetChanged");
        }


        notifyDataSetChanged();
    }


}
