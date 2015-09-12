package app.com.connolly.dillon.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import junit.framework.Test;

import java.util.HashSet;

/**
 * Created by Dillon Connolly on 9/7/2015.
 */
public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteDatabase(){
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }
    // Delete any existing database so that the test is always done on a freshly made Database.
    public void setUp() {
        deleteDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MovieContract.TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.RatingsEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.MovieDetailsEntry.TABLE_NAME);

        // First test to make sure that the database is in fact deleted and then created
        // so that it is writable.
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());

        // Make sure the tables are created properly.
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
        assertTrue("Error: The database was not created correctly.", cursor.moveToFirst());
        do{
            tableNameHashSet.remove(cursor.getString(0));
        }while(cursor.moveToNext());
        assertTrue("Error: The database was created without the tables.", tableNameHashSet.isEmpty());

        cursor = db.rawQuery("PRAGMA table_info(" + MovieContract.TrailerEntry.TABLE_NAME + ")", null);
        assertTrue("Error: could not query TrailerEntry for table information.", cursor.moveToFirst());
        final HashSet<String> trailerColumnHashSet = new HashSet<>();
        trailerColumnHashSet.add(MovieContract.TrailerEntry._ID);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_MOVIE_ID);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY);

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            trailerColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required trailer entry columns.",
                trailerColumnHashSet.isEmpty());
        // TODO: do the same for the ratings entry and movie entry.
        db.close();
    }

    public void testMovieTable() {
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertTrue(db.isOpen());
        // Make sure there are no issues inserting the dummy data in these two tables.
        long trailerRowId = insertTrailer();
        assertTrue(trailerRowId != -1);
        long ratingRowId = insertRating();
        assertTrue(ratingRowId != -1);
        ContentValues testValues = TestUtilities.createMovieValues(trailerRowId,ratingRowId);

        long movieRowId = db.insert(MovieContract.MovieDetailsEntry.TABLE_NAME, null, testValues);
        assertTrue(movieRowId != -1);

        Cursor cursor = db.query(
                MovieContract.MovieDetailsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: no records returned from the movie query.", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Error: Movie query validation failed", cursor, testValues);
        assertFalse("Error: more than one record from movie query", cursor.moveToNext());

        cursor.close();
        db.close();
    }

    // Test inserting a row into the Trailer table.
    public long insertTrailer(){
        MovieDbHelper dbHelper = new MovieDbHelper(this.mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Set up a test row for the table.
        ContentValues testValues = TestUtilities.createTrailerValues();

        long trailerRowId;
        trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME,null,testValues);
        assertTrue(trailerRowId != -1);

        Cursor responseCursor;
        responseCursor = db.query(
                MovieContract.TrailerEntry.TABLE_NAME, // Table to query
                null, // all columns
                null, // columns for the "where" clause
                null, // vlaues for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue("Error: No records returned from the trailer query.", responseCursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Trailer query validation failed.",
                responseCursor,
                testValues);
        assertFalse("Error: More than one record from Trailer query.", responseCursor.moveToNext());
        responseCursor.close();
        db.close();

        return trailerRowId;
    }

    // This method works the same as the insertTrailer() method. Refer to that for details on the
    // method.
    public long insertRating(){
        MovieDbHelper dbHelper = new MovieDbHelper(this.mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Set up a test row for the table.
        ContentValues testValues = TestUtilities.createRatingValues();

        long RatingRowId;
        RatingRowId = db.insert(MovieContract.RatingsEntry.TABLE_NAME,null,testValues);
        assertTrue(RatingRowId != -1);

        Cursor responseCursor;
        responseCursor = db.query(
                MovieContract.RatingsEntry.TABLE_NAME, // Table to query
                null, // all columns
                null, // columns for the "where" clause
                null, // vlaues for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue("Error: No records returned from the trailer query.", responseCursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Trailer query validation failed.",
                responseCursor,
                testValues);
        assertFalse("Error: More than one record from Trailer query.", responseCursor.moveToNext());
        responseCursor.close();
        db.close();

        return RatingRowId;
    }
}
