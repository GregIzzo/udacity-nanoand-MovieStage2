package com.example.android.udacity_nanoand_moviestage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Context context = getBaseContext();
        String posterPath = intent.getStringExtra("poster_path");
        String imurl = "https://image.tmdb.org/t/p/w500" + posterPath;
        ImageView posterImageView =  findViewById(R.id.poster_iv);
        Picasso.with(context).load(imurl).into(posterImageView);

        TextView year_tv =  findViewById(R.id.year_tv);
        year_tv.setText(intent.getStringExtra("release_date"));

        TextView summary_tv =  findViewById(R.id.summary_tv);
        summary_tv.setText(intent.getStringExtra("overview"));

        TextView popularity_tv =  findViewById(R.id.popularity_tv);
        popularity_tv.setText(String.valueOf(intent.getDoubleExtra("popularity",0))) ;

        TextView voteave_tv =  findViewById(R.id.voteave_tv);
        voteave_tv.setText(String.valueOf(intent.getDoubleExtra("vote_average",0))) ;

    }
}
