package com.enhanced.endgameodyssey;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *     This is the singleton MovieDatabase abstract class that Room will subclass.
 *
 *     A Room database can have multiple entities (tables) with each entity having its own corresponding
 *     Data Access Oject, therefore a Room database can have multiple DAO's but since we only have one
 *     entity (Movie), we only have one Data Access Object in this class-- MovieDAO.
 *
 *     This class doesn't have a MovieDAO variable. We can access the Movie's DAO using the abstract method movieDAO()
 *     which we'll call using the database's single instance. but since this class is an abstract class, we cannot create
 *     an instance of the MovieDatabase using the 'new' keyword. That is why we have annotated this class with @Database
 *     so that Room will subclass MovieDatabase implement the class for us.
 *
 *     We can now then get a single instance of MovieDatabase using the getInstance() synchronized method.
 */
@Database(entities = Movie.class, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    // Create a singleton
    private static MovieDatabase instance;
    private static Context mContext;

    // Room subclasses the MovieDatabase class therefore we can call this abstract method
    // after MovieDatabase is instantiated using MovieDatabase.getInstance() and be able to get the MovieDAO
    public abstract MovieDAO movieDAO();

    // This builds an instance of MovieDatabase if none exists, then returns it.
    // It is synchronized to prevent creation of multiple instances.
    public static synchronized MovieDatabase getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, "movie_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    // A callback that is added to Room's database builder that populates the database upon creation only.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    // This is called upon creation of the database. AsyncTask is inherited without
    // any types (Void) since doInBackground does not need any data to execute.
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        // We don't have a movieDAO member variable in MovieDatabase class (see MovieDatabase class javadocs comment)
        // that's why we'll get it from the database's instance passed onto this class's constructor.
        private MovieDAO movieDAO;

        // Constructor
        private PopulateDBAsyncTask(MovieDatabase db) {
            // We are allowed to call MovieDatabase.movieDAO() here since this constructor is called only
            // after the database is created or instantiated.
            movieDAO = db.movieDAO();
        }

        // Invoked using the execute() method.
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                insertMovies(movieDAO);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // This is pretty much self-explanatory else re-enroll to Dean Mitch's OOP.
        private static void insertMovies(MovieDAO movieDAO) throws Exception{

            Locale locale = new Locale("en", "ph");

            // new Movie(String title, String description, String imageFilename, Date releasedAt, int timelinePosition, float rating)

            // Captain America: The First Avenger
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.captain_america1_title),
                    mContext.getString(R.string.captain_america1_description),
                    mContext.getString(R.string.captain_america1_image_filename),
                    new SimpleDateFormat("dd/MM/yyyy", locale).parse(mContext.getString(R.string.captain_america1_releasedAt)),
                    Integer.parseInt(mContext.getString(R.string.captain_america1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.captain_america1_rating))
            ));

            // Captain Marvel
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.captain_marvel_title),
                    mContext.getString(R.string.captain_marvel_description),
                    mContext.getString(R.string.captain_marvel_image_filename),
                    new SimpleDateFormat("dd/MM/yyyy", locale).parse(mContext.getString(R.string.captain_marvel_releasedAt)),
                    Integer.parseInt(mContext.getString(R.string.captain_marvel_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.captain_marvel_rating))
            ));

            // Iron Man
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.iron_man1_title),
                    mContext.getString(R.string.iron_man1_description),
                    mContext.getString(R.string.iron_man1_image_filename),
                    new SimpleDateFormat("dd/MM/yyyy", locale).parse(mContext.getString(R.string.iron_man1_releasedAt)),
                    Integer.parseInt(mContext.getString(R.string.iron_man1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.iron_man1_rating))
            ));
        }
    }
}
