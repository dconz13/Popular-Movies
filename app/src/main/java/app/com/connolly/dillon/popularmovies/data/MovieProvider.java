package app.com.connolly.dillon.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Dillon Connolly on 8/20/2015.
 */
public class MovieProvider extends ContentProvider{

    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_RATINGS_AND_TRAILERS = 101;
    static final int RATINGS = 200;
    static final int TRAILERS = 300;

    private static final SQLiteQueryBuilder mMovieQueryBuilder;

    // note: This is an initialization block.
    static{
        mMovieQueryBuilder = new SQLiteQueryBuilder();

        mMovieQueryBuilder.setTables(
                MovieContract.MovieDetailsEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.RatingsEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieDetailsEntry.TABLE_NAME +
                        "." + MovieContract.MovieDetailsEntry.COLUMN_RATINGS_KEY +
                        " = " + MovieContract.RatingsEntry.TABLE_NAME +
                        "." + MovieContract.RatingsEntry._ID
        );
    }
    //Trailer.trailer_name = ? AND trailer_key = ? AND movie_id = ?
    private static final String sTrailerMovieIdSelection =
            MovieContract.TrailerEntry.TABLE_NAME +
                    "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " = ? AND" +
            MovieContract.TrailerEntry.COLUMN_TRAILER_NAME + " = ? AND" +
            MovieContract.TrailerEntry.COLUMN_TRAILER_KEY + " = ?";

    //Rating.review_author = ? AND review_key = ? AND movie_id = ?
    private static final String sRatingsMovieIdSelection =
            MovieContract.RatingsEntry.TABLE_NAME +
                    "." + MovieContract.RatingsEntry.COLUMN_MOVIE_ID + " = ? AND" +
                    MovieContract.RatingsEntry.COLUMN_REVIEW_AUTHOR + " = ? AND" +
                    MovieContract.RatingsEntry.COLUMN_REVIEW_CONTENT + " = ?";


    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE_MOVIE, MOVIE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE_MOVIE + "/*", MOVIE_WITH_RATINGS_AND_TRAILERS);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE_MOVIE_RATING, RATINGS);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE_MOVIE_TRAILER, TRAILERS);

        return matcher;
    }

    private Cursor getFavoriteMovieAlphabetically(Uri uri, String[] projection, String sortOrder) {
        String selection = null;
        String[] selectionArgs = null;
        // TODO: figure out WTF is going on here

        return mMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted = 0;

        switch(match){
            case MOVIE:
                rowsDeleted = db.delete(MovieContract.MovieDetailsEntry.TABLE_NAME,selection,selectionArgs);
                break;

            case RATINGS:
                rowsDeleted = db.delete(MovieContract.RatingsEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case TRAILERS:
                rowsDeleted = db.delete(MovieContract.TrailerEntry.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI" + uri);
        }
        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor= null;
        switch(mUriMatcher.match(uri)){
            case MOVIE_WITH_RATINGS_AND_TRAILERS:

                break;
            // "movie"
            case MOVIE:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "ratings"
            case RATINGS:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.RatingsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAILERS:
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        if(returnCursor!= null)
            returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);

        switch(match){
            case MOVIE:
                return MovieContract.MovieDetailsEntry.CONTENT_TYPE;
            case MOVIE_WITH_RATINGS_AND_TRAILERS:
                return MovieContract.MovieDetailsEntry.CONTENT_TYPE;
            case TRAILERS:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case RATINGS:
                return MovieContract.RatingsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case MOVIE:{
                long _id = db.insert(MovieContract.MovieDetailsEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.MovieDetailsEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case RATINGS:{
                long _id = db.insert(MovieContract.RatingsEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.RatingsEntry.buildRatingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            case TRAILERS:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);

        switch(match) {
            case MOVIE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieDetailsEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case TRAILERS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case RATINGS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.RatingsEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int numUpdated = 0;

        switch(match){
            case MOVIE:{
                numUpdated = db.update(MovieContract.MovieDetailsEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case RATINGS:{
                numUpdated = db.update(MovieContract.RatingsEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case TRAILERS:{
                numUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI" + uri);
        }
        if(numUpdated!= 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
