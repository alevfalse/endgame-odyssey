package com.enhanced.endgameodyssey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.enhanced.endgameodyssey.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.enhanced.endgameodyssey.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.enhanced.endgameodyssey.EXTRA_DESCRIPTION";
    public static final String EXTRA_IMAGE_FILENAME = "com.enhanced.endgameodyssey.EXTRA_IMAGE_FILENAME";
    public static final String EXTRA_RELEASE_DATE = "com.enhanced.endgameodyssey.EXTRA_RELEASE_DATE";
    public static final String EXTRA_TIMELINE_POSITION = "com.enhanced.endgameodyssey.EXTRA_TIMELINE_POSITION";
    public static final String EXTRA_RATING = "com.enhanced.endgameodyssey.EXTRA_RATING";
    public static final String EXTRA_WATCHED = "com.enhanced.endgameodyssey.EXTRA_WATCHED";
    public static final String EXTRA_RUNTIME_MINUTES = "com.enhanced.endgameodyssey.EXTRA_RUNTIME_MINUTES";

    ImageView imageViewPoster;
    TextView textViewDescription;
    TextView textViewReleaseDate;
    TextView textViewRating;
    TextView textViewRuntime;

    ImageView imageViewWatchButton;
    TextView textViewWatched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();

        imageViewPoster = findViewById(R.id.image_view_movie_details_poster);
        textViewDescription = findViewById(R.id.text_view_movie_details_description);
        textViewReleaseDate = findViewById(R.id.text_view_movie_details_release_date);
        textViewRating = findViewById(R.id.text_view_movie_details_rating);
        textViewRuntime = findViewById(R.id.text_view_runtime);

        imageViewWatchButton = findViewById(R.id.image_view_watched);
        textViewWatched = findViewById(R.id.text_view_watched);

        if (intent.getIntExtra(EXTRA_TIMELINE_POSITION, -1) == 22) {
            imageViewWatchButton.setImageResource(R.drawable.infinity_gauntlet);
        }

        // Set the data provided by the MainActivity
        loadImage(intent.getStringExtra(EXTRA_IMAGE_FILENAME), imageViewPoster);
        textViewDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        textViewReleaseDate.setText(intent.getStringExtra(EXTRA_RELEASE_DATE));
        textViewRating.setText(Float.toString(intent.getFloatExtra(EXTRA_RATING, 0)));

        int runtimeMinutes = intent.getIntExtra(EXTRA_RUNTIME_MINUTES, 0);

        System.out.println("Runtime: " + runtimeMinutes);
        String timeString = getTimeString(runtimeMinutes);

        textViewRuntime.setText("Runtime: " + timeString);

        imageViewWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMovie();
            }
        });
    }

    private void loadImage(String imageName, ImageView imageView){
        int resID = this.getResources().getIdentifier(imageName , "drawable", this.getPackageName());
        if(resID!=0) {//The associated resource identifier. Returns 0 if no such resource was found. (0 is not a valid resource ID.)
            imageView.setImageResource(resID);
        } else {
            System.err.println(imageName + " not found.");
        }
    }

    // TODO: Add comment
    private void updateMovie() {
        Intent data = new Intent();

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        int timelinePosition = getIntent().getIntExtra(EXTRA_TIMELINE_POSITION, -1);
        boolean isWatched = getIntent().getBooleanExtra(EXTRA_WATCHED, false);

        data.putExtra(EXTRA_WATCHED, isWatched);
        data.putExtra(EXTRA_TIMELINE_POSITION, timelinePosition);

        setResult(RESULT_OK, data);
        finish();
    }

    public static String getTimeString(int time) {
        System.out.println("Time: " + time);
        String hours = String.valueOf(time/60);
        String minutes = String.valueOf(time%60);
        String str = hours + " hours and " + minutes + " minutes";

        return str;
    }
}
