package com.enhanced.endgameodyssey;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Constants
    private String title;
    private String description;
    private String imageFilename;
    private Date releasedAt;
    private int timelinePosition;
    private float rating;

    // The only field that can be set
    private boolean watched;

    public Movie(String title, String description, String imageFilename, Date releasedAt, int timelinePosition, float rating) {
        this.title = title;
        this.description = description;
        this.releasedAt = releasedAt;
        this.timelinePosition = timelinePosition;
        this.rating = rating;
        this.imageFilename = imageFilename;
        this.watched = false;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void watch() {
        this.watched = true;
    }

    public void unwatch() {
        this.watched = false;
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

    public Date getReleasedAt() {
        return releasedAt;
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
}
