package com.example.android.udacity_nanoand_moviestage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacity_nanoand_moviestage1.utilities.DataUtilities;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        //Put Movie Title in Detail Title

        //setTitle(intent.getStringExtra("title"));
        TextView title_tv = findViewById(R.id.title_tv);
        title_tv.setText(intent.getStringExtra("title"));

        Context context = getBaseContext();
        String posterPath = intent.getStringExtra("backdrop_path");
        String imurl = "https://image.tmdb.org/t/p/w780" + posterPath;
        ImageView posterImageView =  findViewById(R.id.poster_iv);
        Picasso.with(context).load(imurl).into(posterImageView);

        TextView year_tv =  findViewById(R.id.year_tv);
        Log.i("GGG", "yearRelase_lable["+getString(R.string.yearReleased_label)+"]: ");
        String releaseText = getString(R.string.yearReleased_label) + DataUtilities.getFormattedDate(intent.getStringExtra("release_date"));
        year_tv.setText(releaseText);

        TextView summary_tv =  findViewById(R.id.summary_tv);
        summary_tv.setText(intent.getStringExtra("overview"));

        TextView popularity_tv =  findViewById(R.id.popularity_tv);
        popularity_tv.setText(String.valueOf(intent.getDoubleExtra("popularity",0))) ;

        TextView voteave_tv =  findViewById(R.id.voteave_tv);
        voteave_tv.setText(String.valueOf(intent.getDoubleExtra("vote_average",0))) ;

    }
}
