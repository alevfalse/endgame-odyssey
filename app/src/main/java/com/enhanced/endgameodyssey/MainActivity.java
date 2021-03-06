package com.enhanced.endgameodyssey;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This project uses the Model-View-ViewModel pattern.
 * The View  informs the ViewModel about the user’s actions
 * The ViewModel  exposes streams of data relevant to the View and works with the DataModel to get and save the data.
 * The DataModel  abstracts the data source.
 *
 * For the DataModel we have the MovieDatabase class as the database,
 * Movie class as the entity or table in the database and MovieDAO class
 * as the Data Access Object which allows us to manipulate the movies inside
 * the database. Lastly, the MovieRepository class that acts as the middle-man
 * between the data source and the ViewModel.
 *
 * For the ViewModel, we have the MovieViewModel class which is responsible for retrieving and storing
 * data from the repository and passing them onto the View or user interface so that the user interface
 * can present or draw them onto the screen. The View does store the data but it observes the LiveData
 * that is stored in the ViewModel for changes.
 *
 * Lastly, for the View, are the MainActivity and MovieDetailsActivity classes.
 * These are what user interacts with. For the MainActivity, we use the RecyclerView to present the
 * list of movies on the user and sorted them chronologically in the MCU timeline.
 */
public class MainActivity extends AppCompatActivity {

    public static final int WATCH_MOVIE_REQUEST = 1;

    private LinearLayoutManager linearLayoutManager;
    private MovieViewModel viewModel;

    private DrawerLayout drawer;

    private ProgressBar progressBar;
    private TextView textViewPercent;
    private TextView textViewWatched;
    private TextView textViewDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Every RecyclerView needs a LayoutManger and so we pass an anonymous instance of LinearLayoutManager
        // where we pass the Context. The LinearLayoutManager takes care of displaying the vertical stack of movie items.
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Since we have set the RecyclerView's height and width to match parent which is the MainActivity,
        // we can then set this true for more efficiency.
        recyclerView.setHasFixedSize(true);

        // We create the MovieAdapter and set it as the RecyclerView's adapter.
        final MovieAdapter adapter = new MovieAdapter();
        // By default the adapter's List of Movies is empty so we have to update it on onChanged callback
        // of the LiveData observer which is immediately invoked when the movies are retrieved from the ViewModel.
        recyclerView.setAdapter(adapter);

        // We do not call new ViewModel() because that would just create a new instance of ViewModel
        // every time this activity is created. We don't want that, instead, we want to retrieve
        // the same data from the same instance of ViewModel. Instead we ask the Android system for the ViewModel.
        // It knows when it has to create a new ViewModel instance or provide an existing instance.
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        // This observer is attached to the Movies LiveData which is retrieved from the ViewModel
        // and observes/waits/listens for changes in the Movie entity/table in the MovieDatabase
        // which then updates the user interface or RecyclerView's itemViews accordingly.
        Observer<List<Movie>> observer = new Observer<List<Movie>>() {

            // This is only called when this Activity is the foreground (visible) and if this activity
            // is destroyed this will not hold a reference to this activity anymore.
            @Override
            public void onChanged(List<Movie> movies) {

                // Submit the list to the adapter and let the ListAdapter class handle the changes and animation
                adapter.submitList(movies);

                // Update the progress tracker each time something is changed
                updateProgress(movies);
            }
        };

        // We call getAllMovies() from the ViewModel which returns a LiveData which we can observe for changes.
        // LiveData is lifecycle aware that's why we pass this Activity as the owner and it will only update
        // the Activity when it is on the foreground (visible), when it is destroyed either due to configuration changes
        // or switched to a different activity, it will automatically clean up the reference to the activity
        // which can help avoid memory leaks and crashes.
        viewModel.getAllMovies().observe(this, observer);

        // Here we attach an ItemTouchHelper to our RecyclerView. We passed an ItemTouchHelper.SimpleCallback with 0
        // as its drag directions to disable dragging, and ItemTouchHelper.LEFT to only support swiping of items to the left.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Movie movie = adapter.getMovieAt(position);
                viewMovieDetails(movie);
                adapter.notifyItemChanged(position);
            }

            // We check first if the movie being swiped is the current movie to watch or is already watched
            // else do not allow the swipe to happen.
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                Movie movie = adapter.getMovieAt(viewHolder.getAdapterPosition());
                if (movie.isCurrent()) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }
                Toast.makeText(MainActivity.this, R.string.locked_movie, Toast.LENGTH_SHORT).show();
                return 0;
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.25f;
            }
        }).attachToRecyclerView(recyclerView);

        // Set the MovieAdapter's OnItemClickListener member variable and pass an anonymous implementation of
        // the OnItemClickListener interface so that the adapter can know what it will do whenever an item is clicked.
        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {

            // Newbie Note From A Newbie Like Me: We are not executing the following onItemClick() method,
            // we are just implementing the OnItemClickListener interface and its onItemClick method and setting it
            // as the MovieAdapter's listener. The MovieAdapter's listener will then bind a View.OnClickListener
            // to each itemView in a ViewHolder and call this onItemClick method and perform the following
            // implementation for for each movie item clicked.
            @Override
            public void onItemClick(Movie movie) {
                viewMovieDetails(movie);
            }
        });

        // Replace default action bar with our custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.draw_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        progressBar = findViewById(R.id.progress_bar);
        textViewPercent = findViewById(R.id.text_view_percent);
        textViewWatched = findViewById(R.id.text_view_watched_count);
        textViewDuration = findViewById(R.id.text_view_duration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the button is pressed
        if (requestCode == WATCH_MOVIE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(MovieDetailsActivity.EXTRA_ID, -1);
            int timelinePosition = data.getIntExtra(MovieDetailsActivity.EXTRA_TIMELINE_POSITION, -1);
            boolean isWatched = data.getBooleanExtra(MovieDetailsActivity.EXTRA_WATCHED, false);

            if (id == -1 || timelinePosition == -1) return;

            // If the movie is not yet watched
            if (!isWatched) {
                // Set it to watched
                viewModel.watch(id);

                // Set the next movie as the current movie to watch if not last movie
                if (timelinePosition < 22) {
                    viewModel.setAsCurrentMovie(id+1);
                    Toast.makeText(this, R.string.marked_as_watched, Toast.LENGTH_SHORT).show();
                } else if (timelinePosition == 22) {
                    viewModel.unwatchAllMovies();
                    viewModel.setAsCurrentMovie(1);
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                    Toast.makeText(this, R.string.snap, Toast.LENGTH_SHORT).show();
                }

            // Set the movie as unwatched and as the current movie to watch
            } else {
                viewModel.unwatch(id);
                viewModel.setAsCurrentMovie(id+1);
                Toast.makeText(this, R.string.marked_as_unwatched, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void viewMovieDetails(Movie movie) {

        if (movie.isCurrent()) {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);

            intent.putExtra(MovieDetailsActivity.EXTRA_ID, movie.getId());
            intent.putExtra(MovieDetailsActivity.EXTRA_TITLE, movie.getTitle());
            intent.putExtra(MovieDetailsActivity.EXTRA_DESCRIPTION, movie.getDescription());
            intent.putExtra(MovieDetailsActivity.EXTRA_IMAGE_FILENAME, movie.getImageFilename());
            intent.putExtra(MovieDetailsActivity.EXTRA_RELEASE_DATE, movie.getReleaseDate());
            intent.putExtra(MovieDetailsActivity.EXTRA_TIMELINE_POSITION, movie.getTimelinePosition());
            intent.putExtra(MovieDetailsActivity.EXTRA_RATING, movie.getRating());
            intent.putExtra(MovieDetailsActivity.EXTRA_WATCHED, movie.isWatched());
            intent.putExtra(MovieDetailsActivity.EXTRA_RUNTIME_MINUTES, movie.getRuntimeMinutes());

            startActivityForResult(intent, WATCH_MOVIE_REQUEST);
        } else if (!movie.isWatched()) {
            Toast.makeText(MainActivity.this, R.string.locked_movie, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProgress(List<Movie> movies) {

        int watchedCount = 0;
        int minutesLeft = 0;

        for (Movie movie : movies) {
            if (movie.isWatched()) {
                watchedCount++;
            } else {
                minutesLeft += movie.getRuntimeMinutes();
            }
        }

        int percent = (watchedCount * 100) / 22;
        String timeLeftString = MovieDetailsActivity.getTimeString(minutesLeft);

        progressBar.setProgress(percent);
        textViewPercent.setText(getString(R.string.percent, percent));
        textViewWatched.setText(String.valueOf(watchedCount));
        textViewDuration.setText(getString(R.string.time_left, timeLeftString));
    }
}
