package app.com.connolly.dillon.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Simplify the code below a bit by importing these classes.
import app.com.connolly.dillon.popularmovies.data.MovieContract.RatingsEntry;
import app.com.connolly.dillon.popularmovies.data.MovieContract.MovieDetailsEntry;
import app.com.connolly.dillon.popularmovies.data.MovieContract.TrailerEntry;

/**
 * Created by Dillon Connolly on 8/20/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favoriteMovie.db";

    public MovieDbHelper(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_RATINGS_TABLE = "CREATE TABLE " + RatingsEntry.TABLE_NAME +" (" +
                RatingsEntry._ID + " INTEGER PRIMARY KEY, " +
                RatingsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"+
                RatingsEntry.COLUMN_REVIEW_AUTHOR + " TEXT," +
                RatingsEntry.COLUMN_REVIEW_CONTENT + " TEXT" + " );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY, " +
                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TRAILER_NAME + " TEXT," +
                TrailerEntry.COLUMN_TRAILER_KEY + " TEXT" + ");";

        final String SQL_CREATE_MOVIE_DETAILS_TABLE = "CREATE TABLE " + MovieDetailsEntry.TABLE_NAME + " (" +
                MovieDetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieDetailsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieDetailsEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieDetailsEntry.COLUMN_MOVIE_RATING + " TEXT," +
                MovieDetailsEntry.COLUMN_MOVIE_LENGTH + " INTEGER," +
                MovieDetailsEntry.COLUMN_RELEASE_DATE + " TEXT," +
                MovieDetailsEntry.COLUMN_VOTE_AVG + " INTEGER," +
                MovieDetailsEntry.COLUMN_IS_FAVORITE + " INTEGER NOT NULL," +
                MovieDetailsEntry.COLUMN_IS_POPULAR + " INTEGER NOT NULL," +
                MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED + " INTEGER NOT NULL," +
                MovieDetailsEntry.COLUMN_OVERVIEW + " TEXT," +
                MovieDetailsEntry.COLUMN_POSTER + " BLOB," +
                MovieDetailsEntry.COLUMN_POSTER_URL + " TEXT,"+
                MovieDetailsEntry.COLUMN_RATINGS_KEY + " INTEGER," +
                MovieDetailsEntry.COLUMN_TRAILER_KEY + " INTEGER," +
                "FOREIGN KEY (" + MovieDetailsEntry.COLUMN_RATINGS_KEY + ") REFERENCES " +
                RatingsEntry.TABLE_NAME + "(" + RatingsEntry._ID + "), " +
                " UNIQUE (" + MovieDetailsEntry.COLUMN_MOVIE_ID + ", " +
                MovieDetailsEntry.COLUMN_RATINGS_KEY + ") ON CONFLICT REPLACE," +
                "FOREIGN KEY (" + MovieDetailsEntry.COLUMN_TRAILER_KEY+") REFERENCES " +
                TrailerEntry.TABLE_NAME + "(" + TrailerEntry._ID + "), "+
                " UNIQUE (" + MovieDetailsEntry.COLUMN_MOVIE_ID + ", " +
                MovieDetailsEntry.COLUMN_TRAILER_KEY + ") ON CONFLICT REPLACE" + ");";

        db.execSQL(SQL_CREATE_MOVIE_DETAILS_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_RATINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Make code to transfer the old favorite details to the new database. Pull any additional
        // needed information from the api using the details of the old db. Afterwards delete old db.
        // For now do nothing.
    }
}
