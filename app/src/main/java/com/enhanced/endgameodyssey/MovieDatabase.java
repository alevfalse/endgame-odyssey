package com.enhanced.endgameodyssey;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Locale;

/**
 * This is the singleton MovieDatabase abstract class that Room will subclass.
 * <p>
 * A Room database can have multiple entities (tables) with each entity having its own corresponding
 * Data Access Object, therefore a Room database can have multiple DAO's but since we only have one
 * entity (Movie), we only have one Data Access Object in this class-- MovieDAO.
 * <p>
 * This class doesn't have a MovieDAO member variable. We can access the Movie's DAO using the abstract method movieDAO()
 * which we'll call using the database's single instance. But since this class is an abstract class, we cannot create
 * an instance of the MovieDatabase using the 'new' keyword. That is why we have annotated this class with @Database
 * so that Room will subclass MovieDatabase implement the class for us.
 * <p>
 * We can now then get a single instance of MovieDatabase using the getInstance() synchronized method and then
 * access the method movieDAO from it.
 */
@Database(entities = Movie.class, version = 5)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance; // Singleton
    private static Context mContext;

    // Room subclasses the MovieDatabase class therefore we can call this abstract method after MovieDatabase
    // is instantiated using MovieDatabase.getInstance() and be able to get the MovieDAO
    public abstract MovieDAO movieDAO();

    // This builds an instance of MovieDatabase if none exists, then returns it.
    // It is synchronized to prevent creation of multiple instances.
    public static synchronized MovieDatabase getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), // Application context
                    MovieDatabase.class, // Database class
                    "movie_database") // Database name
                    .fallbackToDestructiveMigration() // Recreate database from scratch whenever version is incremented
                    .addCallback(roomCallback) // Add an onCreate callback
                    .build();
        }

        return instance;
    }

    // A callback that is added to Room's database builder that populates the database upon creation only.
    // This must be executed on the background thread (asynchronously) since Room doesn't allow execution of
    // database operations on the main thread which could freeze the app and can cause it to crash.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    // This is called upon creation of the database only.
    // AsyncTask is inherited without any types (Void) since the method doInBackground() does not need any parameter to execute.
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

        // This is pretty much self-explanatory otherwise re-enroll to Dean Mitch's OOP.
        private static void insertMovies(MovieDAO movieDAO) throws Exception {

            // Create a Locale for the SimpleDateFormat's constructor with arguments of language and country.
            Locale locale = new Locale("en", "ph");

            // new Movie(String title, String description, String imageFilename, Date releasedAt, int timelinePosition, float rating)

            // TODO: Research on XML parser for the movies strings.

            // Captain America: The First Avenger
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.captain_america1_title),
                    mContext.getString(R.string.captain_america1_description),
                    mContext.getString(R.string.captain_america1_image_filename),
                    mContext.getString(R.string.captain_america1_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.captain_america1_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.captain_america1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.captain_america1_rating)),
                    true));

            // Captain Marvel
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.captain_marvel_title),
                    mContext.getString(R.string.captain_marvel_description),
                    mContext.getString(R.string.captain_marvel_image_filename),
                    mContext.getString(R.string.captain_marvel_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.captain_marvel_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.captain_marvel_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.captain_marvel_rating)),
                    false));

            // Iron Man
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.iron_man1_title),
                    mContext.getString(R.string.iron_man1_description),
                    mContext.getString(R.string.iron_man1_image_filename),
                    mContext.getString(R.string.iron_man1_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.iron_man1_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.iron_man1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.iron_man1_rating)),
                    false));

            // Iron Man 2
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.iron_man2_title),
                    mContext.getString(R.string.iron_man2_description),
                    mContext.getString(R.string.iron_man2_image_filename),
                    mContext.getString(R.string.iron_man2_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.iron_man2_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.iron_man2_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.iron_man2_rating)),
                    false));

            // The Incredible Hulk
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.hulk_title),
                    mContext.getString(R.string.hulk_description),
                    mContext.getString(R.string.hulk_image_filename),
                    mContext.getString(R.string.hulk_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.hulk_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.hulk_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.hulk_rating)),
                    false));

            // Thor
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.thor1_title),
                    mContext.getString(R.string.thor1_description),
                    mContext.getString(R.string.thor1_image_filename),
                    mContext.getString(R.string.thor1_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.thor1_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.thor1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.thor1_rating)),
                    false));

            // The Avengers
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.avengers1_title),
                    mContext.getString(R.string.avengers1_description),
                    mContext.getString(R.string.avengers1_image_filename),
                    mContext.getString(R.string.avengers1_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.avengers1_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.avengers1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.avengers1_rating)),
                    false));

            // Iron Man 3
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.iron_man3_title),
                    mContext.getString(R.string.iron_man3_description),
                    mContext.getString(R.string.iron_man3_image_filename),
                    mContext.getString(R.string.iron_man3_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.iron_man3_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.iron_man3_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.iron_man3_rating)),
                    false));

            // Thor: The Dark World
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.thor2_title),
                    mContext.getString(R.string.thor2_description),
                    mContext.getString(R.string.thor2_image_filename),
                    mContext.getString(R.string.thor2_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.thor2_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.thor2_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.thor2_rating)),
                    false));

            // Captain America: The Winter Soldier
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.captain_america2_title),
                    mContext.getString(R.string.captain_america2_description),
                    mContext.getString(R.string.captain_america2_image_filename),
                    mContext.getString(R.string.captain_america2_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.captain_america2_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.captain_america2_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.captain_america2_rating)),
                    false));

            // Guardians of the Galaxy
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.guardians1_title),
                    mContext.getString(R.string.guardians1_description),
                    mContext.getString(R.string.guardians1_image_filename),
                    mContext.getString(R.string.guardians1_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.guardians1_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.guardians1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.guardians1_rating)),
                    false));

            // Guardians of the Galaxy Vol. 2
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.guardians2_title),
                    mContext.getString(R.string.guardians2_description),
                    mContext.getString(R.string.guardians2_image_filename),
                    mContext.getString(R.string.guardians2_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.guardians2_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.guardians2_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.guardians2_rating)),
                    false));

            // Avengers: Age of Ultron
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.avengers2_title),
                    mContext.getString(R.string.avengers2_description),
                    mContext.getString(R.string.avengers2_image_filename),
                    mContext.getString(R.string.avengers2_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.avengers2_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.avengers2_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.avengers2_rating)),
                    false));

            // Ant-Man
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.ant_man1_title),
                    mContext.getString(R.string.ant_man1_description),
                    mContext.getString(R.string.ant_man1_image_filename),
                    mContext.getString(R.string.ant_man1_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.ant_man1_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.ant_man1_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.ant_man1_rating)),
                    false));

            // Captain America: Civil War
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.captain_america3_title),
                    mContext.getString(R.string.captain_america3_description),
                    mContext.getString(R.string.captain_america3_image_filename),
                    mContext.getString(R.string.captain_america3_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.captain_america3_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.captain_america3_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.captain_america3_rating)),
                    false));

            // Black Panther
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.black_panther_title),
                    mContext.getString(R.string.black_panther_description),
                    mContext.getString(R.string.black_panther_image_filename),
                    mContext.getString(R.string.black_panther_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.black_panther_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.black_panther_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.black_panther_rating)),
                    false));

            // Spider-Man: Homecoming
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.spider_man_title),
                    mContext.getString(R.string.spider_man_description),
                    mContext.getString(R.string.spider_man_image_filename),
                    mContext.getString(R.string.spider_man_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.spider_man_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.spider_man_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.spider_man_rating)),
                    false));

            // Doctor Strange
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.doctor_strange_title),
                    mContext.getString(R.string.doctor_strange_description),
                    mContext.getString(R.string.doctor_strange_image_filename),
                    mContext.getString(R.string.doctor_strange_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.doctor_strange_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.doctor_strange_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.doctor_strange_rating)),
                    false));

            // Thor: Ragnarok
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.thor3_title),
                    mContext.getString(R.string.thor3_description),
                    mContext.getString(R.string.thor3_image_filename),
                    mContext.getString(R.string.thor3_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.thor3_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.thor3_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.thor3_rating)),
                    false));

            // Ant-Man and the Wasp
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.ant_man2_title),
                    mContext.getString(R.string.ant_man2_description),
                    mContext.getString(R.string.ant_man2_image_filename),
                    mContext.getString(R.string.ant_man2_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.ant_man2_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.ant_man2_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.ant_man2_rating)),
                    false));

            // Avengers: Infinity War
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.avengers3_title),
                    mContext.getString(R.string.avengers3_description),
                    mContext.getString(R.string.avengers3_image_filename),
                    mContext.getString(R.string.avengers3_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.avengers3_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.avengers3_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.avengers3_rating)),
                    false));

            // Avengers: Endgame
            movieDAO.insert(new Movie(
                    mContext.getString(R.string.avengers4_title),
                    mContext.getString(R.string.avengers4_description),
                    mContext.getString(R.string.avengers4_image_filename),
                    mContext.getString(R.string.avengers4_releasedAt),
                    Integer.parseInt(mContext.getString(R.string.avengers4_durationMinutes)),
                    Integer.parseInt(mContext.getString(R.string.avengers4_timelinePosition)),
                    Float.parseFloat(mContext.getString(R.string.avengers4_rating)),
                    false));
        }
    }
}
