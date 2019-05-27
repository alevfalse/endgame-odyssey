package com.enhanced.endgameodyssey;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 *     This is the movies repository class that the ViewModel will get its data from. It will serve as the
 *     "middle-man" between the ViewModel and the data source. We can have many data source which can
 *     be a local database, a remote database, an XML, or an API, but in this case it will just be a
 *     SQLite Database which we abstracted using the Room library.
 *
 *     This repository is created to provide an abstraction layer on top of the different data source.
 *     The ViewModel does not have to know where its data are coming from and does not have to perform
 *     business logic on how it will retrieve and update data from the data source. This repository
 *     will handle it.
 *
 *     This application will only use one data source, the MoveDatabase. This repository accesses the MovieDatabase
 *     by creating (or getting if one already exists) an instance of it and using its Data Access Object or (DAO)
 *     to access and manipulate data in the database.
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
    // database operations on the main thread which could freeze the app and can cause it to crash.
    public void watch(int id) {
        new WatchMovieAsyncTask(movieDAO).execute(id);
    }

    // Executed asynchronously (see update() comments).
    public void unwatch(int id) {
        new UnwatchMovieAsyncTask(movieDAO).execute(id);
    }

    // Executed asynchronously (see update() comments).
    public void setAsCurrentMovie(int id) {
        new SetAsCurrentMovieAsyncTask(movieDAO).execute(id);
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
    // AsyncTask is inherited with Integer data type since doInBackground needs an int movie id.
    private static class WatchMovieAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MovieDAO movieDAO;

        // Constructor: Since this class is static, we can't access the movieDAO member variable of
        // MovieRepository class directly so we have to pass it onto the constructor.
        private WatchMovieAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        // This function, invoked using the class method execute(), uses varargs or variable arguments since
        // we can pass any number of movies to update but in this case, we'll only update one.
        @Override
        protected Void doInBackground(Integer... integers) {
            movieDAO.watch(integers[0]);
            return null;
        }
    }

    // This has to be static so it does not have a reference to the MovieRepository class otherwise it could cause a memory leak.
    // AsyncTask is inherited with Integer data type since doInBackground needs a movie to execute.
    private static class UnwatchMovieAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MovieDAO movieDAO;

        // Constructor: Since this class is static, we can't access the movieDAO member variable of
        // MovieRepository class directly so we have to pass it onto the constructor.
        private UnwatchMovieAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        // This function, invoked using the class method execute(), uses varargs or variable arguments since
        // we can pass any number of ids to update but in this case, we'll only update one.
        @Override
        protected Void doInBackground(Integer... integers) {
            movieDAO.unwatch(integers[0]);
            return null;
        }
    }

    // This has to be static so it does not have a reference to the MovieRepository class otherwise it could cause a memory leak.
    // AsyncTask is inherited with Integer data type since doInBackground needs a movie to execute.
    private static class SetAsCurrentMovieAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MovieDAO movieDAO;

        // Constructor: Since this class is static, we can't access the movieDAO member variable of
        // MovieRepository class directly so we have to pass it onto the constructor.
        private SetAsCurrentMovieAsyncTask(MovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        // This function, invoked using the class method execute(), uses varargs or variable arguments since
        // we can pass any number of ids to update but in this case, we'll only update one.
        @Override
        protected Void doInBackground(Integer... integers) {
            movieDAO.setAsCurrentMovie(integers[0]);
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
