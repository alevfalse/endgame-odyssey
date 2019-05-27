package com.enhanced.endgameodyssey;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * This is the MovieViewModel class which inherits the AndroidViewModel class.
 * AndroidViewModel is a subclass of ViewModel. With the AndroidViewModel, we are able to pass
 * the Application to the constructor which we passes to the MovieRepository's constructor, which
 * the repository passes to the MovieDatabase constructor which it will use as a parameter
 * for the Room.databaseBuilder();
 *
 * This class is used so that the Activity will not be responsible for storing and processing data to
 * be presented into the user interface. It requests data from the repository so the Activity can draw it
 * onto the screen and it forwards user actions from the interface back to the repository.
 *
 * The Activity will only have a reference to the ViewModel but not to the
 * repository nor the data source(s). We store UI-related data in the ViewModel instead of the Activity
 * because ViewModel survives or retains data even after configuration changes.
 *
 * Whenever a configuration change occurs, such as when we rotate the device,
 * the Activity is destroyed and recreated therefore all the state and values of its member variables
 * are lost. The ViewModel on the other hand stays alive and the Activity can just retrieve the same
 * ViewModel instance and immediately access the same variables. It does not have to save or re-fetch
 * anything.
 */
public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> allMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);

        // Pass the application to the MovieRepository constructor
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
    }

    public void watch(int id) {
        repository.watch(id);
    }

    public void unwatch(int id) {
        repository.unwatch(id);
    }

    public void setAsCurrentMovie(int id) {
        repository.setAsCurrentMovie(id);
    }

    public void unwatchAllMovies(Movie movie) {
        repository.unwatchAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }
}
