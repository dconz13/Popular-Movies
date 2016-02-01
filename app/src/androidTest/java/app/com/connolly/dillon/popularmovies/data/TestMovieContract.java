package app.com.connolly.dillon.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

import app.com.connolly.dillon.popularmovies.R;

/**
 * Created by Dillon Connolly on 9/7/2015.
 * Most of this testing is referenced from Udacity's testing
 */
public class TestMovieContract extends AndroidTestCase {

    private static final String SORT_SETTING = "vote_average.desc";//"popularity.desc";
    private static final String SORT_RETURN_VALUE = "vote_average";//"popular";

    public void testBuildMovieDetails() {
        Uri testUri = MovieContract.MovieDetailsEntry.buildMovieDetails(SORT_SETTING);
        assertNotNull("Error: Null Uri returned. You must fill-in buildMovieDetails in " +
                MovieContract.MovieDetailsEntry.class.getSimpleName(),
                testUri);
        assertEquals("Error: Sort setting not properly appended to the end of the Uri",
                SORT_RETURN_VALUE, testUri.getLastPathSegment());
        assertEquals("Error: Movie Uri doesn't match expected result.",
                testUri.toString(),
                "content://app.com.connolly.dillon.popularmovies/movie/" + SORT_RETURN_VALUE);
    }
}
