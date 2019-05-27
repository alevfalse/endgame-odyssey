package com.enhanced.endgameodyssey;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 *     This is the movies repository class that the ViewModel will get its data from. It will serve as the
 *     "middle-man" between the ViewModel and the data source(s). We can have many data source which can
 *     be a local database, a remote database, an XML, or an API, but in this case it will just be a
 *     SQLite Database which we abstracted using the Room library.
 *
 *     This repository is created so that the ViewModel does not have to know where its data are coming from
 *     and perform business logic to retrieve and update data from the data source(s). This repository
 *     will handle it.
 *
 *     This repository accesses the MovieDatabase by creating (or getting if one already exists) an
 *     instance of it and using its Data Access Object or (DAO) to access and manipulate data in the
 *     database.
 */
public class MovieRepository {

    private MovieDAO movieDAO;
    private LiveData<List<Movie>> allMovies;

    // Constructor
    public MovieRepository(Application application) {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDAO = database.movieDAO();
        allMovies = movieDAO.getAllMovies();
    }

    // Execute update on the background thread (asynchronously) since Room doesn't allow execution of
    // database operations on the main thread because this could freeze the app and can cause it to crash.
    public void update(Movie movie) {
        new UpdateMoveAsyncTask(movieDAO).execute(movie);
    }

    // Executed asynchronously (see update() comments).
    public void unwatchAllMovies() {
        new UnwatchAllMovies(movieDAO).execute();
    }

    // Room already executes the database operation that returns the LiveData of all movies
    // in the background thread so we don't have to explicitly execute it asynchronously.
    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Asynchronous Tasks

    // This has to be static so it does not have a reference to the MovieRepository class otherwise it could cause a memory leak.
    // AsyncTask is inherited with Movie data type since doInBackground needs a movie to execute.
    private static class UpdateMoveAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO movieDAO;

        // Constructor: Since this class is static, we can't access the movieDAO member variable of
        // MovieRepository class directly so we have to pass it onto the constructor.
        private UpdateMoveAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        // This function, invoked using the class method execute(), uses varargs or variable arguments since
        // we can pass any number of movies to update but in this case, we'll only update one.
        @Override
        protected Void doInBackground(Movie... movies) {
            movieDAO.update(movies[0]);
            return null;
        }
    }

    // This has to be static so it does not have a reference to the MovieRepository class otherwise it could cause a memory leak.
    // AsyncTask is inherited without any types (Void) since doInBackground does not need any data to execute.
    private static class UnwatchAllMovies extends AsyncTask<Void, Void, Void> {

        private MovieDAO movieDAO;

        // Constructor: Since this class is static, we can't access the movieDAO member variable of
        // MovieRepository class directly so we have to pass it onto the constructor.
        private UnwatchAllMovies(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        // This function, invoked using the class method execute(), uses varargs or variable arguments since
        // we can pass any number of movies to update but in this case, we'll only update one.
        @Override
        protected Void doInBackground(Void... voids) {
            movieDAO.unwatchAllMovies();
            return null;
        }
    }
}
