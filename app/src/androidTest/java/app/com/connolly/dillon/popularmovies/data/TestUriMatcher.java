package app.com.connolly.dillon.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Dillon Connolly on 9/7/2015.
 */
public class TestUriMatcher extends AndroidTestCase{
    private static final String SORT_SETTING = "vote_average.desc";//"popularity.desc";
    private static final String SORT_RETURN_VALUE = "vote_average";//"popular";

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieDetailsEntry.CONTENT_URI;
    private static final Uri TEST_TRAILER_DIR = MovieContract.TrailerEntry.CONTENT_URI;
    private static final Uri TEST_RATING_DIR = MovieContract.RatingsEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_TRAILER_AND_RATING_DIR = MovieContract.MovieDetailsEntry.buildMovieDetails(SORT_SETTING);

    public void testUriMatcher() {
        UriMatcher uriMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                uriMatcher.match(TEST_MOVIE_DIR),MovieProvider.MOVIE);
        assertEquals("Error: The TRAILER URI was matched incorrectly.",
                uriMatcher.match(TEST_TRAILER_DIR),MovieProvider.TRAILERS);
        assertEquals("Error: The RATING URI was matched incorrectly.",
                uriMatcher.match(TEST_RATING_DIR), MovieProvider.RATINGS);
        assertEquals("Error: The MOVIE WITH TRAILER AND RATINGS URI was matched incorrectly.",
                uriMatcher.match(TEST_MOVIE_WITH_TRAILER_AND_RATING_DIR), MovieProvider.MOVIE_WITH_RATINGS_AND_TRAILERS);
    }
}
