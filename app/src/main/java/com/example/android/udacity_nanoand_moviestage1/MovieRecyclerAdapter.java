package com.example.android.udacity_nanoand_moviestage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieRecyclerAdapter.class.getSimpleName();
    private int mNumberOfItems;
    private String movieJsonData;
    private final MovieAdapterOnClickHandler mClickHandler;
    private Context viewGroupContext;

   private JSONObject reader = null;
  private  JSONArray resArray=null;

    public interface MovieAdapterOnClickHandler {
        void onClick(String movieData) throws JSONException;
    }
    /*
     * Constructor for MovieRecyclerAdapter - accepts number of items to display
     */
    public MovieRecyclerAdapter(MovieAdapterOnClickHandler mclick) {
       // mNumberOfItems = numberOfItems;
        mClickHandler = mclick;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public final ImageView listItemMovieView;

        public MovieAdapterViewHolder( View itemView) {
            super(itemView);
            listItemMovieView =  itemView.findViewById(R.id.iv_item_movie);
            Log.i(TAG, " $$$$ class MovieAdapterViewHolder - listItemMovieView = " +listItemMovieView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            JSONObject jObj = null;
            Log.i(TAG, "^^^^^^^^^^onClick: "+ adapterPosition);

             if (resArray != null){
                 try {
                      jObj = resArray.getJSONObject(adapterPosition);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
            try {
                mClickHandler.onClick(jObj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.i(TAG, " $$$$ onCreateViewHolder: ");

        viewGroupContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroupContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }
    /**
     * OnBindViewHolder is called by RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the movie
     * image for the given position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The MovieAdapterViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        //set the image of the ImageView
        Log.i(TAG, "++++++ ** ** ** onBindViewHolder: pos="+position);
        if (movieJsonData == null){
            Log.i(TAG, "       ++++++ movieJsonData = null");
            return;
        }
        try {
            JSONObject jObj = resArray.getJSONObject(position);
            String posterPath = jObj.getString("poster_path");
            String imurl = "https://image.tmdb.org/t/p/w500" + posterPath;

            Picasso.with(viewGroupContext).load(imurl).into(movieAdapterViewHolder.listItemMovieView);
        } catch (JSONException e) {
            Log.i(TAG, "^^^^^^^^^ ERROR onBindViewHolder - reading json object");
            e.printStackTrace();
        }



    }


    @Override
    public int getItemCount() {

        if (null == resArray){
            Log.i(TAG, " ** ** ** getItemCountbn ull: 0");
            return 0;
        }
        Log.i(TAG, " ** ** ** getItemCount: "+resArray.length());
        return resArray.length();

    }

      /*

     */
    public void setMovieData(String movieData) {
        movieJsonData = movieData;
        try {
            reader = new JSONObject(movieJsonData);
            resArray = reader.getJSONArray("results");
        } catch (JSONException e) {
            Log.i(TAG, "^^^^^^^^^ ERROR onBindViewHolder - creating JSONObject");
            e.printStackTrace();
        }

        Log.i(TAG, "**** setMovieData: "+movieData);
        notifyDataSetChanged();
    }

}
