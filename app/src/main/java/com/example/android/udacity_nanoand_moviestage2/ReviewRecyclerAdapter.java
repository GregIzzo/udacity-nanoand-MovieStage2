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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Greg on 5/15/2018.
 */
//
public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewAdapterViewHolder>  {

    private static final String TAG = "ReviewOO";
    //private int mNumberOfItems;
    private String ReviewJsonData;
    private final ReviewAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;

    private JSONObject reader = null;
    private  JSONArray resArray=null;

    public interface ReviewAdapterOnClickHandler {
        void onClick(int type, String movieData) throws JSONException;
    }
    /*
     * Constructor for ReviewRecyclerAdapter - accepts number of items to display
     */
    public ReviewRecyclerAdapter(ReviewAdapterOnClickHandler mclick) {
        // mNumberOfItems = numberOfItems;
        mClickHandler = mclick;
    }


    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public final ImageView listItemReviewView;
        public final TextView listItemAuthorView;
        public final TextView listItemContentView;


        public ReviewAdapterViewHolder( View itemView) {

            super(itemView);
            Log.i(TAG, "ReviewAdapterViewHolder: !!! CREATOR!!!");
            listItemReviewView =  itemView.findViewById(R.id.iv_item_review);
            listItemAuthorView = itemView.findViewById(R.id.tv_author_review);
            listItemContentView = itemView.findViewById(R.id.tv_content_review);
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
                    mClickHandler.onClick(2, jObj.toString());//2=review
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }
    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the movie
     * image for the given position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param ReviewAdapterViewHolder The ReviewAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder ReviewAdapterViewHolder, int position) {
        //set the image of the ImageView

        if (ReviewJsonData == null){
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
            /*
                keys are:
                    author
                    content
                    id
                    url
             */
            String author = jObj.getString("author");
            String content = jObj.getString("content");
            String content_bit = "";
            if (content.length() > 49){
                content_bit = content.substring(0,49) + " . . . ";
            } else {
                content_bit = content;
            }
            String id = jObj.getString("id");
            String url = jObj.getString("url");

            Log.i(TAG, " GGonBindViewHolder (Review): POSITION="+position+ " author=" + author + " id="+ id + " url="+url);
            ReviewAdapterViewHolder.listItemReviewView.setImageResource(R.mipmap.ic_heart);
            ReviewAdapterViewHolder.listItemAuthorView.setText(author);
            ReviewAdapterViewHolder.listItemContentView.setText(content_bit);

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
            Picasso.with(viewGroupContext).load(imurl).into(ReviewAdapterViewHolder.listItemReviewView);
            */

        } catch (JSONException e) {
            Log.i(TAG, "onBindViewHolder: BAD error="+e.toString());
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
    public void setReviewData(String ReviewData) {
        ReviewJsonData = ReviewData;
        if (ReviewData == null){
            reader = null;
            resArray = null;
            //   Log.i(TAG, "**** ReviewRecyclerAdapter.setReviewData: str len= 0 calling notifyDataSetChanged");
        } else {
            try {
                reader = new JSONObject(ReviewJsonData);
                resArray = reader.getJSONArray("results");
            } catch (JSONException e) {
                //   Log.i(TAG, "^^^^^^^^^ ERROR onBindViewHolder - creating JSONObject");
                e.printStackTrace();
            }
            // Log.i(TAG, "**** ReviewRecyclerAdapter.setReviewData: str len= "+movieData.length() + " calling notifyDataSetChanged");
        }


        notifyDataSetChanged();
    }


}
