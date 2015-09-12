package app.com.connolly.dillon.popularmovies.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import app.com.connolly.dillon.popularmovies.util.PollingCheck;

/**
 * Created by Dillon Connolly on 9/7/2015.
 */
public class TestUtilities extends AndroidTestCase{
    static final int TEST_MOVIE_ID = 007;

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error,valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static ContentValues createMovieValues(long trailerRowId, long ratingsRowId) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
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

        return movieValues;
    }

    static ContentValues createTrailerValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        testValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, "Youtube trailer 1");
        testValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, "www.youtube.com");
        return testValues;
    }

    static ContentValues createRatingValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.RatingsEntry.COLUMN_MOVIE_ID, TEST_MOVIE_ID);
        testValues.put(MovieContract.RatingsEntry.COLUMN_REVIEW_AUTHOR, "Joe Shmoe");
        testValues.put(MovieContract.RatingsEntry.COLUMN_REVIEW_CONTENT, "This movie was awesome.");
        return testValues;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }
        // onChange method for earlier versions of android.
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange,null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri){
            mContentChanged = true;
        }

        public void waitForNotificattionOrFail() {
            // This is taken from the Android CTS (Compatibility Test Suite).
            new PollingCheck(5000) {
                @Override
                protected boolean check(){ return mContentChanged; }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
