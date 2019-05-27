package com.enhanced.endgameodyssey;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This is the Movie entity which represents a table in the MovieDatabase.
 * It is a simple class that is annotated with @Entity so that Room will know that this is an entity
 * of the MovieDatabase.
 *
 * We do not set/update any data directly onto the Movie class instances.
 * We update them on the database directly using this entity's Data Access Object,
 * the MovieDAO class.
 */
@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Constants
    private String title;
    private String description;
    private String imageFilename;
    private String releaseDate;

    private int timelinePosition;
    private float rating;

    // The only field that can be updated in the database
    private boolean watched;
    private boolean current;

    public Movie(String title, String description, String imageFilename, String releaseDate, int timelinePosition, float rating, boolean current) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.timelinePosition = timelinePosition;
        this.rating = rating;
        this.imageFilename = imageFilename;
        this.current = current;
        this.watched = false;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getTimelinePosition() {
        return timelinePosition;
    }

    public float getRating() {
        return rating;
    }

    public boolean isWatched() {
        return watched;
    }

    public boolean isCurrent() {
        return current;
    }
}
