package com.enhanced.endgameodyssey;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This is the Data Access Object or DAO interface for the Movie entity.
 * It is responsible for directly manipulating the movies in the database.
 * This is used by the MovieRepository class which provides the data to the ViewModel.
 */
@Dao
public interface MovieDAO {

    @Insert
    void insert(Movie movie);

    // Set watched to false, and set current to true
    @Query("UPDATE movie_table SET watched=0, current=1 WHERE id=:id")
    void unwatch(int id);

    // Set watched to true and current to false
    @Query("UPDATE movie_table SET watched=1, current=0 WHERE id=:id")
    void watch(int id);

    // Set current to true, this is usually called after calling watch
    @Query("UPDATE movie_table SET watched=0, current=1 WHERE id=:id")
    void setAsCurrentMovie(int id);

    // Reset all of the movies to not watched.
    @Query("UPDATE movie_table SET watched=0")
    void unwatchAllMovies();

    // Get all movies sorted sorted by chronological order in the MCU timeline and return
    // them as LiveData of List of Movies
    @Query("SELECT * FROM movie_table ORDER BY timelinePosition ASC")
    LiveData<List<Movie>> getAllMovies();
}
