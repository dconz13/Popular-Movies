package app.com.connolly.dillon.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.Test;


/**
 * Created by Dillon Connolly on 9/7/2015.
 */
public class TestMovieProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieContract.MovieDetailsEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieContract.RatingsEntry.CONTENT_URI,
                null,
                null
        );
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieDetailsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: records not deleted from Trailer table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieContract.RatingsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: records not deleted from Rating table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        db.delete(MovieContract.MovieDetailsEntry.TABLE_NAME,null,null);
        db.delete(MovieContract.TrailerEntry.TABLE_NAME,null,null);
        db.delete(MovieContract.RatingsEntry.TABLE_NAME,null,null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Clear all records before running the test to make sure it is starting fresh.
    @Override
    public void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    // Test to make sure that the content provider is registered correctly. If it is not, the
    // packageManager will throw an exception.
    public void testProviderRegistered() {
        PackageManager packageManager = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try{
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
            "instead of: " + MovieContract.CONTENT_AUTHORITY, providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        }catch(PackageManager.NameNotFoundException e){
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    // Make sure that getType is working properly. This is basically the same thing as the TestUriMatcher class.
    // It literally tests the same thing.
    public void testGetType() {
        // content://app.com.connolly.dillon.popularmovies/movie
        String type = mContext.getContentResolver().getType(MovieContract.MovieDetailsEntry.CONTENT_URI);
        // vnd.android.cursor.dir/app.com.connolly.dillon.popularmovies/movie
        assertEquals("Error: the MovieDetailsEntry CONTENT_URI should return MovieDetailsEntry.CONTENT_TYPE",
                MovieContract.MovieDetailsEntry.CONTENT_TYPE, type);

        String sortSetting = "popularity.desc";
        type = mContext.getContentResolver()
                .getType(MovieContract.MovieDetailsEntry.buildMovieDetails(sortSetting));
        assertEquals("Error: the MovieDetailsEntry CONTENT_URI with Trailer and Rating should return MovieDetailsEntry.CONTENT_TYPE",
                MovieContract.MovieDetailsEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(MovieContract.TrailerEntry.CONTENT_URI);
        assertEquals("Error: the TrailerEntry CONTENT_URI should return TrailerEntry.CONTENT_TYPE",
                MovieContract.TrailerEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(MovieContract.RatingsEntry.CONTENT_URI);
        assertEquals("Error: the RatingsEntry CONTENT_URI should return RatingsEntry.CONTENT_TYPE",
                MovieContract.RatingsEntry.CONTENT_TYPE, type);
    }

    // Insert directly with the database and then use the content provider to read the data.
    // This tests the query part of the provider.
    public void testBasicMovieQuery() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        ContentValues ratingValues = TestUtilities.createRatingValues();
        ContentValues trailerValues = TestUtilities.createTrailerValues();
        long ratingRowId = db.insert(MovieContract.RatingsEntry.TABLE_NAME, null, ratingValues);
        assertTrue("Unable to insert RatingsEntry into the Database", ratingRowId != -1);
        long trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME,null,trailerValues);
        assertTrue("Unable to insert TrailerEntry into the Database", trailerRowId != -1);

        ContentValues movieValues = TestUtilities.createMovieValues(trailerRowId, ratingRowId);
        long movieRowId = db.insert(MovieContract.MovieDetailsEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to insert MovieDetailsEntry into the Database", movieRowId != -1);

        db.close();

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieDetailsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, movieValues);
    }

    // Test the providers insert and update methods.
    public void testUpdateAndInsert() {
        ContentValues trailerValues = TestUtilities.createTrailerValues();

        Uri trailerUri = mContext.getContentResolver()
                .insert(MovieContract.TrailerEntry.CONTENT_URI, trailerValues);
        long trailerRowId = ContentUris.parseId(trailerUri);
        assertTrue(trailerRowId != -1);
        Log.d(LOG_TAG, "New trailer row id: " + trailerRowId);

        ContentValues updatedValues = new ContentValues(trailerValues);
        updatedValues.put(MovieContract.TrailerEntry._ID, trailerRowId);
        updatedValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,"Super cool trailer");

        // Create cursor with observer so that the observer notifies on cursor change.
        Cursor trailerCursor = mContext.getContentResolver().query(MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        trailerCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieContract.TrailerEntry.CONTENT_URI, updatedValues,
                MovieContract.TrailerEntry._ID + "= ?",
                new String[]{Long.toString(trailerRowId)});
        assertEquals(count, 1);

        // Make sure the observer actually works too.
        tco.waitForNotificattionOrFail();
        trailerCursor.unregisterContentObserver(tco);
        trailerCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null, // projection
                MovieContract.TrailerEntry._ID + " = " + trailerRowId,
                null, // Values for "where"
                null // sort order
        );

        TestUtilities.validateCursor("testUpdateAndInsert. Error validating trailer entry update.",
                cursor,updatedValues);
        cursor.close();
    }

    // Test insert and query functionality of the provider.
    public void testInsertReadProvider() {
        ContentValues contentValues = TestUtilities.createRatingValues();
        TestUtilities.TestContentObserver tco = TestUtilities.TestContentObserver.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.RatingsEntry.CONTENT_URI,true,tco);
        Uri ratingUri = mContext.getContentResolver().insert(MovieContract.RatingsEntry.CONTENT_URI,contentValues);

        // If this fails, the culprit is the insert isn't calling getContext().getContentResolver().notifyChange(uri,null);
        tco.waitForNotificattionOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long ratingRowId = ContentUris.parseId(ratingUri);
        assertTrue(ratingRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.RatingsEntry.CONTENT_URI,
                null, // all columns
                null, // cols for "where"
                null, // values for "where"
                null // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating RatingEntry.",
                cursor, contentValues);

        ContentValues trailerValues = TestUtilities.createTrailerValues();
        Uri trailerUri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.CONTENT_URI, trailerValues);
        long trailerRowId = ContentUris.parseId(trailerUri);
        assertTrue(trailerRowId != -1);

        ContentValues movieValues = TestUtilities.createMovieValues(trailerRowId,ratingRowId);
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MovieContract.MovieDetailsEntry.CONTENT_URI, true, tco);

        Uri movieInsertUri = mContext.getContentResolver()
                .insert(MovieContract.MovieDetailsEntry.CONTENT_URI, movieValues);
        assertTrue(movieInsertUri != null);
        // If this fails then the problem is in the insert movie in the content provider
        tco.waitForNotificattionOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieDetailsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieDetailsEntry insert.",
                movieCursor, movieValues);
        // TODO: Not sure if this test is necessary or not
        //movieValues.putAll(trailerValues);
       // movieValues.putAll(contentValues);

//        movieCursor = mContext.getContentResolver().query(
//                MovieContract.MovieDetailsEntry.buildMovieDetails("popularity.desc"),
//                null,
//                null,
//                null,
//                null
//        );
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating joined tables.",
//               movieCursor, movieValues);
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        TestUtilities.TestContentObserver ratingObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.RatingsEntry.CONTENT_URI, true, ratingObserver);

        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.TrailerEntry.CONTENT_URI,true,trailerObserver);

        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieDetailsEntry.CONTENT_URI,true,movieObserver);

        deleteAllRecordsFromProvider();

        ratingObserver.waitForNotificattionOrFail();
        trailerObserver.waitForNotificattionOrFail();
        movieObserver.waitForNotificattionOrFail();

        mContext.getContentResolver().unregisterContentObserver(ratingObserver);
        mContext.getContentResolver().unregisterContentObserver(trailerObserver);
        mContext.getContentResolver().unregisterContentObserver(movieObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 20;
    static ContentValues[] createBulkInsertMovieValues(long trailerRowId, long ratingsRowId){
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
        for(int i=0; i<BULK_INSERT_RECORDS_TO_INSERT; i++){
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID, i);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RATING, "R");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_POSTER, 1);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_VOTE_AVG, "7.8");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_POSTER_URL, "www.youtube.com");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_RELEASE_DATE, "1-21-15");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_OVERVIEW, "The one guy goes on an adventure.");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE, "The One Guy");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_LENGTH, "120m");
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_RATINGS_KEY, ratingsRowId);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_TRAILER_KEY, trailerRowId);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE, 0);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED, 0);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_POPULAR, 0);
            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

    public void testBulkInsert() {
        ContentValues ratingValues = TestUtilities.createRatingValues();
        ContentValues trailerValues = TestUtilities.createTrailerValues();
        Uri ratingUri = mContext.getContentResolver().insert(MovieContract.RatingsEntry.CONTENT_URI, ratingValues);
        Uri trailerUri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.CONTENT_URI,trailerValues);
        long ratingRowId = ContentUris.parseId(ratingUri);
        long trailerRowId = ContentUris.parseId(trailerUri);

        assertTrue(ratingRowId != -1);
        assertTrue(trailerRowId != -1);

        Cursor ratingCursor = mContext.getContentResolver().query(
                MovieContract.RatingsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBulkInsert. Error validating RatingEntry.",
                ratingCursor, ratingValues);

        Cursor trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBulkInsert. Error validating TrailerEntry.",
                trailerCursor,trailerValues);

        ContentValues[] bulkInsertCV = createBulkInsertMovieValues(trailerRowId,ratingRowId);

        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieDetailsEntry.CONTENT_URI,true,movieObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(MovieContract.MovieDetailsEntry.CONTENT_URI,bulkInsertCV);
        // If this fails it is either the bulkInsert method or something in here.

        movieObserver.waitForNotificattionOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieDetailsEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " ASC"
        );

        assertEquals(movieCursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        movieCursor.moveToFirst();
        for(int i = 0; i< BULK_INSERT_RECORDS_TO_INSERT; i++,movieCursor.moveToNext()){
            TestUtilities.validateCurrentRecord("testBulkInsert. Error validating MovieDetailsEntry " + i,
                    movieCursor,bulkInsertCV[i]);
        }
        movieCursor.close();
        trailerCursor.close();
        ratingCursor.close();
    }
}
