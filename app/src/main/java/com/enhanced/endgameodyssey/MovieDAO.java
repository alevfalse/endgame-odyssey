package com.enhanced.endgameodyssey;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {

    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    // Reset all of the movies to not watched.
    @Query("UPDATE movie_table SET watched=0")
    void unwatchAllMovies();

    // Get all movies sorted sorted by chronological order in the MCU timeline.
    @Query("SELECT * FROM movie_table ORDER BY timelinePosition ASC")
    LiveData<List<Movie>> getAllMovies();
}
