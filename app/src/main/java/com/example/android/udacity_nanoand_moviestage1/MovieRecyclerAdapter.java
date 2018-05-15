package com.example.android.udacity_nanoand_moviestage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Greg on 5/15/2018.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private static final String TAG = MovieRecyclerAdapter.class.getSimpleName();
    private int mNumberOfItems;
    /*
     * Constructor for MovieRecyclerAdapter - accepts number of items to display
     */
    public MovieRecyclerAdapter(int numberOfItems) { mNumberOfItems = numberOfItems;}



    @NonNull
    @Override
    public MovieRecyclerAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerAdapter.MovieViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView listItemMovieView;

        public MovieViewHolder( View itemView) {
            super(itemView);
            listItemMovieView = (ImageView) itemView.findViewById(R.id.iv_item_movie);
        }

        void bind(int listIndex){
            //
        }

    }
}
