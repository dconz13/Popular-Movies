package app.com.connolly.dillon.popularmovies.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import java.util.List;

import app.com.connolly.dillon.popularmovies.API.MovieService;
import app.com.connolly.dillon.popularmovies.Model.AllMovieResults;
import app.com.connolly.dillon.popularmovies.Model.DiscoverResults;
import app.com.connolly.dillon.popularmovies.Model.Result;
import app.com.connolly.dillon.popularmovies.Model.Result_;
import app.com.connolly.dillon.popularmovies.R;
import app.com.connolly.dillon.popularmovies.adapters.GridViewAdapter;
import app.com.connolly.dillon.popularmovies.data.MovieContract;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    GridViewAdapter mGridViewAdapter;
    private GridView mGridView;
    private static Context mContext;
    private static final int MOVIE_LOADER = 0;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_pos";
    private String sortSetting;
    private final String API = "http://api.themoviedb.org/3"; // Base URL
    private final String params = "videos,reviews,releases";
    RestAdapter mRestAdapter;
    private String mImageSize = "w342";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieDetailsEntry.TABLE_NAME + "." + MovieContract.MovieDetailsEntry._ID,
            MovieContract.MovieDetailsEntry.COLUMN_POSTER,
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE,
            MovieContract.MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED,
            MovieContract.MovieDetailsEntry.COLUMN_IS_POPULAR,
            MovieContract.MovieDetailsEntry.COLUMN_POSTER_URL
    };

    static final int COL_MOVIE_ENTRY_ID = 0;
    public static final int COL_POSTER = 1;
    public static final int COL_MOVIE_ID = 2;
    static final int COL_IS_FAVORITE = 3;
    static final int COL_IS_HIGHLY_VOTED = 4;
    static final int COL_IS_POPULAR = 5;
    public static final int COL_POSTER_URL = 6;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortSetting = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getActivity().getString(R.string.pref_sort_key),
                        getActivity().getString(R.string.pref_sort_default));
        String sortOrder = MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " ASC";
        Uri movieListUri = MovieContract.MovieDetailsEntry.buildMovieDetails(sortSetting);
        String selection;
        if (sortSetting.equals("favorite")) {
            selection = " " + MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE + " = 1 ";
        } else if (sortSetting.equals("popularity.desc")) {
            selection = " " + MovieContract.MovieDetailsEntry.COLUMN_IS_POPULAR + " = 1 ";
        } else {
            selection = " " + MovieContract.MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED + " = 1 ";
        }

        return new CursorLoader(
                getActivity(),
                movieListUri,
                MOVIE_COLUMNS,
                selection,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //if(mGridViewAdapter == null)
        //   mGridViewAdapter = new GridViewAdapter(getActivity(), data, 0);
        // else
        //mProgress.setVisibility(View.GONE);
        //mGridView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mGridViewAdapter.swapCursor(data);
        //mGridView.setAdapter(mGridViewAdapter);
        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        }
        //Log.v(LOG_TAG, "onLoadFinished: ");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGridViewAdapter.swapCursor(null);
    }

    public void onSettingChanged() {
        updateMovieList();
        Log.v(LOG_TAG, "SETTING CHANGED");
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    public interface Callback {
        public void onItemSelected(Uri selectedMovie, int position, int movieId, View view);
    }

    @Override
    public void onRefresh() {
        updateMovieList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(API)
                .build();
        mContext = getActivity();

        if(savedInstanceState != null){
            sortSetting = savedInstanceState.getString("sortSetting");
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            //Log.v(LOG_TAG,"RESTORING MOVIE LIST");
        }else{
            updateMovieList();
            //Log.v(LOG_TAG,"UPDATING MOVIE LIST");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Log.v(LOG_TAG,"MPOSITION: "+mPosition + " " + GridView.INVALID_POSITION);
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
            outState.putString("sortSetting", sortSetting);
        }else{
            mPosition = 0;
            outState.putInt(SELECTED_KEY, mPosition);
            outState.putString("sortSetting", sortSetting);
        }
        super.onSaveInstanceState(outState);
    }

    public MoviesFragment() {
        // Uncomment if refresh button is necessary. Otherwise leave it.
        setHasOptionsMenu(true);
        //mMovieObjectArrayList = new ArrayList<MovieDataStructure>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            // TODO: Enable this as needed by programmer. Enabled by uncommenting setHasOptionsMenu from MoviesFragment()
            onRefresh();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mGridViewAdapter = new GridViewAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        mGridView.setAdapter(mGridViewAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String sortSetting = PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .getString("sort", "popularity.desc");
                    int movieId = cursor.getInt(COL_MOVIE_ID);
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieDetailsEntry.builderMovieWithRatingAndTrailer(cursor.getInt(COL_MOVIE_ID)), position
                                    , movieId,view);
                    //Log.v(LOG_TAG,"MOVIE ID: "+ cursor.getInt(COL_MOVIE_ID));
                    mPosition = position;
                }
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    public void networkErrorToast() {
        Toast toast = Toast.makeText(getActivity(), "Could not connect to Internet! Check connection settings and swipe down to refresh.", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sortSetting != PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("sort", "popularity.desc")) {
            updateMovieList();
        }
    }

    private void updateMovieList() {
        sortSetting = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("sort", "popularity.desc");

        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);

        if (!sortSetting.equals("favorite") && sortSetting != null) {
            //Log.v(LOG_TAG, "sortSetting: " + sortSetting);
            String api_key = getResources().getString(R.string.api_key);
            MovieService movieService = mRestAdapter.create(MovieService.class);
            retrofit.Callback callback = createDiscoverResultsCallback();
            movieService.getMovieList(sortSetting, api_key, callback);
        }
    }

    public retrofit.Callback<DiscoverResults> createDiscoverResultsCallback() {
        retrofit.Callback callback = new retrofit.Callback() {
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(LOG_TAG, "Retrofit error: ", retrofitError);
                networkErrorToast();
            }

            // TODO: try creating an AsyncTask in the success method. Have it handle the progress bar.
            @Override
            public void success(Object o, Response response) {
                String api_key = getResources().getString(R.string.api_key);
                DiscoverResults discoverResults = (DiscoverResults) o;
                if (discoverResults.getResults().size() == 0) {
                    //Log.v(LOG_TAG, "Woops the array is length 0.");
                } else {
                    MovieService movieService = mRestAdapter.create(MovieService.class);
                    for (int i = 0; i < discoverResults.getResults().size(); i++) {
                        retrofit.Callback allTestCallback = new retrofit.Callback() {
                            @Override
                            public void success(Object o, Response response) {
                               // Log.v(LOG_TAG, ((AllMovieResults) o).getTitle());
                                ContentValues movieValues = makeMovieContentValue(o);

                                if (movieValues.size() > 0) {
                                    String selection = MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = " + ((AllMovieResults) o).getId();
                                    Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieDetailsEntry.CONTENT_URI, MOVIE_COLUMNS, selection, null, null);

                                    // If the cursor can't move to first then just insert the new record.
                                    if (!cursor.moveToFirst()) {
                                       Uri inserted = mContext.getContentResolver().insert(MovieContract.MovieDetailsEntry.CONTENT_URI, movieValues);
                                    } else {
                                        // If it can move to first and the line is not a favorite, delete it and insert the new data.
                                        if (cursor.getInt(COL_IS_FAVORITE) != 1) {
                                            mContext.getContentResolver().update(MovieContract.MovieDetailsEntry.CONTENT_URI, movieValues,selection,null);
                                        }
                                        // Do nothing because it is a favorite.
                                        // TODO: update favorite information.
                                    }
                                    //Log.v(LOG_TAG,"inserted " + ContentUris.parseId(inserted) + " movies into db");
                                    cursor.close();
                                }
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.e(LOG_TAG, "Woops too much!");
                            }
                        };
                        movieService.getMovieInfo(discoverResults.getResults().get(i).getId(), api_key, params, allTestCallback);
                    }
                }

            }
        };
        return callback;
    }

    public ContentValues makeMovieContentValue(Object o) {
        AllMovieResults movie = (AllMovieResults) o;
        ContentValues trailerValues = makeTrailerContentValue(movie.getVideos().getResults(), movie.getId());
        ContentValues ratingsValues = makeReviewContentValues(movie.getReviews().getResults(), movie.getId());
        ContentValues movieValues = new ContentValues();
        Uri trailerUri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.CONTENT_URI, trailerValues);
        Uri ratingsUri = mContext.getContentResolver().insert(MovieContract.RatingsEntry.CONTENT_URI, ratingsValues);
        long trailerRowId = ContentUris.parseId(trailerUri);
        long ratingsRowId = ContentUris.parseId(ratingsUri);
        String posterPath = movie.getPosterPath();
        posterPath = buildPosterURL(posterPath);
        String movieRating = "";
        if (movie.getReleases().getCountries().size() > 0) {
            movieRating = movie.getReleases().getCountries().get(0).getCertification();
        }

        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RATING, movieRating);
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_POSTER, 1);
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_VOTE_AVG, movie.getVoteAverage());
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_POSTER_URL, posterPath);
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_LENGTH, movie.getRuntime());
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_RATINGS_KEY, ratingsRowId);
        movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_TRAILER_KEY, trailerRowId);

        if (sortSetting.equals("popularity.desc")) {
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE, 0);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED, 0);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_POPULAR, 1);
          //  Log.v(LOG_TAG, "is popular");
        } else {
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE, 0);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED, 1);
            movieValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_POPULAR, 0);
          //  Log.v(LOG_TAG, "highest rated");
        }

        return movieValues;
    }

    // Important to note that this method stores all the trailers in a single string separated by the | character.
    // This isn't the best method because it relies on the Strings not containing | for it to work.

    public ContentValues makeTrailerContentValue(List<Result> trailerResult, int movieId) {
        ContentValues trailerValues = new ContentValues();
        if (trailerResult.size() > 0) {

            String names = "";
            String keys = "";
            for(int i = 0; i<trailerResult.size(); i++) {
                if(i == 0){
                    names = names.concat(trailerResult.get(i).getName());
                    keys = keys.concat(trailerResult.get(i).getKey());
                }else {
                    names = names.concat("|" + trailerResult.get(i).getName());
                    keys = keys.concat("|" + trailerResult.get(i).getKey());
                }
            }
           // Log.v(LOG_TAG,"TRAILERNAME: "+names);
           // Log.v(LOG_TAG,"TRAILERKEY: " + keys);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, names);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, keys);
        } else {
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, "NT_TAG");
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, "NT_TAG");
        }
        return trailerValues;
    }

    // Important to note that this method stores all the comments and authors in a single string separated by the | character.
    // This isn't the best method because it relies on the Strings not containing | for it to work.

    public ContentValues makeReviewContentValues(List<Result_> reviewResult, int movieId) {
        ContentValues reviewValues = new ContentValues();
        if (reviewResult.size() > 0) {
            String authors = "";
            String contents = "";
            for(int i = 0; i<reviewResult.size(); i++) {
                if(i == 0){
                    authors = authors.concat(reviewResult.get(i).getAuthor());
                    contents = contents.concat(reviewResult.get(i).getContent());
                }else {
                    authors = authors.concat("|" + reviewResult.get(i).getAuthor());
                    contents = contents.concat("|" + reviewResult.get(i).getContent());
                }
            }
            //Log.v(LOG_TAG,"authors: "+ authors + " content: " + contents);
            reviewValues.put(MovieContract.RatingsEntry.COLUMN_MOVIE_ID, movieId);
            reviewValues.put(MovieContract.RatingsEntry.COLUMN_REVIEW_AUTHOR, authors);
            reviewValues.put(MovieContract.RatingsEntry.COLUMN_REVIEW_CONTENT, contents);
        } else {
            reviewValues.put(MovieContract.RatingsEntry.COLUMN_MOVIE_ID, movieId);
            reviewValues.put(MovieContract.RatingsEntry.COLUMN_REVIEW_AUTHOR, "NR_TAG");
            reviewValues.put(MovieContract.RatingsEntry.COLUMN_REVIEW_CONTENT, "NR_TAG");
        }
        return reviewValues;
    }

    public String buildPosterURL(String movieUrl) {
        String fullUrl = "http://image.tmdb.org/t/p/" + mImageSize + "/" + movieUrl;
        return fullUrl;
    }
}
