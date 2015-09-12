package app.com.connolly.dillon.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import app.com.connolly.dillon.popularmovies.R;

/**
 * Created by Dillon Connolly on 8/20/2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "app.com.connolly.dillon.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_MOVIE_RATING = "rating";
    public static final String PATH_FAVORITE_MOVIE = "movie";
    public static final String PATH_FAVORITE_MOVIE_TRAILER = "trailer";

    public static final class RatingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "rating";

        // Column with the movie id
        public static final String COLUMN_MOVIE_ID = "movie_id";
        // Column with the user ratings | String
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        // Column with the user review | String
        public static final String COLUMN_REVIEW_CONTENT = "review_content";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE_RATING).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIE_RATING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIE_RATING;

        public static Uri buildRatingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "trailer";

        //Column with the movie id
        public static final String COLUMN_MOVIE_ID = "movie_id";
        //Column with the trailer name
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        //Column with the trailer link
        public static final String COLUMN_TRAILER_KEY = "trailer_key";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"+ PATH_FAVORITE_MOVIE_TRAILER;
        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class MovieDetailsEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        // Movie rating, ie. "R", "PG-13" stored as a String.
        public static final String COLUMN_MOVIE_RATING = "movie_rating";
        // Movie release date stored as a String.
        public static final String COLUMN_RELEASE_DATE = "release_date";
        // Movie length in minutes stored as an int.
        public static final String COLUMN_MOVIE_LENGTH = "movie_length";

        // Movie overview stored as a String.
        public static final String COLUMN_OVERVIEW = "overview";
        // Vote average store as a String
        public static final String COLUMN_VOTE_AVG = "vote_avg";
        // Url for movie poster stored as a String.
        public static final String COLUMN_POSTER_URL = "poster_url";
        // Blob for movie poster.
        public static final String COLUMN_POSTER = "poster";
        // Movie title stored as a String.
        public static final String COLUMN_MOVIE_TITLE = "title";
        // Movie id returned by the api stored as an int.
        public static final String COLUMN_MOVIE_ID = "movie_id";
        // Boolean. true if it is a favorite movie
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
        // Boolean. true if it came from popular api call
        public static final String COLUMN_IS_POPULAR = "is_popular";
        // Boolean. true if it came from highly vote api call
        public static final String COLUMN_IS_HIGHLY_VOTED = "is_high";


        // Foreign key for the ratings table.
        public static final String COLUMN_RATINGS_KEY = "ratings_id";
        // Foreign key for the trailer table.
        public static final String COLUMN_TRAILER_KEY = "trailer_id";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE).build();
        // CURSOR_DIR_BASE_TYPE -> For URI containing a Cursor of zero of more items.
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIE;
        // CURSOR_ITEM_BASE_TYPE -> For URI containing a Cursor of a single item.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIE;

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieDetails(String sortSetting){
            if(sortSetting.equals("favorite")) sortSetting = "favorite";
            if(sortSetting.equals("popularity.desc")) sortSetting = "popular";
            if(sortSetting.equals("vote_average.desc")) sortSetting = "vote_average";
            return CONTENT_URI;
        }

        public static Uri builderMovieWithRatingAndTrailer(int movieId){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build();
        }

    }
}
