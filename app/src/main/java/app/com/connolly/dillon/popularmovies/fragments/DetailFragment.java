package app.com.connolly.dillon.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.com.connolly.dillon.popularmovies.R;
import app.com.connolly.dillon.popularmovies.adapters.DetailFragmentPagerAdapter;
import app.com.connolly.dillon.popularmovies.data.MovieContract;


/**
 * Created by Dillon Connolly on 8/22/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String MOVIE_OBJ = "selectedMovie";
    public static final String POSITION = "position";
    public static final String TRANSITION_ANIMATION = "TA";
    public boolean isTwoPane;
    private Uri mUri;
    private int mPosition;
    private int mMovieId;
    private static final int DETAIL_LOADER = 0;
    private boolean mTransitionAnimation;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieDetailsEntry.TABLE_NAME + "." + MovieContract.MovieDetailsEntry._ID,
            MovieContract.MovieDetailsEntry.TABLE_NAME + "." + MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_LENGTH,
            MovieContract.MovieDetailsEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieDetailsEntry.COLUMN_VOTE_AVG,
            MovieContract.MovieDetailsEntry.COLUMN_POSTER_URL,
            MovieContract.MovieDetailsEntry.COLUMN_POSTER,
            MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RATING,
            MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE,
            MovieContract.MovieDetailsEntry.COLUMN_IS_HIGHLY_VOTED,
            MovieContract.MovieDetailsEntry.COLUMN_IS_POPULAR,
            MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,
            MovieContract.TrailerEntry.COLUMN_TRAILER_KEY,
            MovieContract.RatingsEntry.COLUMN_REVIEW_AUTHOR,
            MovieContract.RatingsEntry.COLUMN_REVIEW_CONTENT,
            MovieContract.MovieDetailsEntry.COLUMN_OVERVIEW
    };

    private static final int COL_ID = 0;
    private static final int COL_MOVIE_ID = 1;
    private static final int COL_MOVIE_TITLE = 2;
    private static final int COL_MOVIE_LENGTH = 3;
    private static final int COL_RELEASE_DATE = 4;
    private static final int COL_VOTE_AVG = 5;
    private static final int COL_POSTER_URL = 6;
    private static final int COL_POSTER = 7;
    private static final int COL_MOVIE_RATING = 8;
    private static final int COL_IS_FAVORITE = 9;
    private static final int COL_IS_HIGHLY_VOTED = 10;
    private static final int COL_IS_POPULAR = 11;
    private static final int COL_TRAILER_NAME = 12;
    private static final int COL_TRAILER_KEY = 13;
    private static final int COL_REVIEW_AUTHOR = 14;
    private static final int COL_REVIEW_CONTENT = 15;
    private static final int COL_OVERVIEW = 16;

    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private RatingBar mVoteAverage;
    private TextView mRating;
    private ImageView mPoster;

    public interface Callback {
        public void setPagerVariable(ViewPager pager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("movieId", mMovieId);
    }

    private TextView mDuration;
    private Button mFavoriteButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getInt("movieId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isTwoPane) {
            Bundle args = getArguments();
            if (args != null) {
                mUri = args.getParcelable(MOVIE_OBJ);
                mPosition = args.getInt(POSITION);
                mMovieId = args.getInt("movieId");
                mTransitionAnimation = args.getBoolean(DetailFragment.TRANSITION_ANIMATION, false);
                //Log.v(LOG_TAG, "Position: " + mPosition);
            }
        } else {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                mUri = intent.getData();
                mPosition = intent.getIntExtra(POSITION, 0);
                mMovieId = intent.getIntExtra("movieId", 0);
                mTransitionAnimation = intent.getBooleanExtra(DetailFragment.TRANSITION_ANIMATION, false);
                //Log.v(LOG_TAG, "ID: " + mMovieId);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mMovieTitle = (TextView) rootView.findViewById(R.id.textView_detail_title);
        mReleaseDate = (TextView) rootView.findViewById(R.id.textView_detail_release_date);
        mVoteAverage = (RatingBar) rootView.findViewById(R.id.detail_average_vote);
        mRating = (TextView) rootView.findViewById(R.id.textView_detail_rating);
        mPoster = (ImageView) rootView.findViewById(R.id.imageView_detail_poster);
        mDuration = (TextView) rootView.findViewById(R.id.textView_detail_duration);
//        mFavoriteButton = (Button) rootView.findViewById(R.id.favorite_button);

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new DetailFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // This prevents the Adapter from deleting the furthest most item
        // If the app is having memory issues this is likely the cause
        mPager.setOffscreenPageLimit(2);

        ((Callback) getActivity()).setPagerVariable(mPager);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieDetailsEntry.TABLE_NAME + "." + MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " ASC";
        String selection = MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = " + mMovieId;
        //Log.v(LOG_TAG, "SELECTION: " + selection);
        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    selection,
                    null,
                    sortOrder
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if ((data != null) && data.moveToFirst()) {
            // TODO: figure out how to just pass 1 row so I don't have to loop through all of them. This is inefficient.
            for (int i = 0; i < data.getCount(); i++) {
                if (data.getInt(COL_MOVIE_ID) != mMovieId)
                    data.moveToNext();
            }
            if (data.getPosition() >= data.getCount()) {
                data.moveToFirst();
            } else if (data.getInt(COL_MOVIE_ID) == mMovieId) {
                String overview = data.getString(COL_OVERVIEW);
                ((DetailFragmentPagerAdapter) mPager.getAdapter()).setOverviewFragmentVariable(overview);

                String trailerKeys = data.getString(COL_TRAILER_KEY);
                String trailerNames = data.getString(COL_TRAILER_NAME);
                ((DetailFragmentPagerAdapter) mPager.getAdapter()).setTrailerFragmentVariable(trailerKeys,trailerNames);

                String reviewAuthors = data.getString(COL_REVIEW_AUTHOR);
                String reviewComments = data.getString(COL_REVIEW_CONTENT);
                ((DetailFragmentPagerAdapter) mPager.getAdapter()).setCommentsFragmentVariable(reviewAuthors,reviewComments);

                String movieTitle = data.getString(COL_MOVIE_TITLE);
                mMovieTitle.setText(movieTitle);

                String releaseDate = data.getString(COL_RELEASE_DATE);
                mReleaseDate.setText(releaseDate);

                String duration = data.getString(COL_MOVIE_LENGTH);
                mDuration.setText(duration + "m");

                String rating = data.getString(COL_MOVIE_RATING);
                mRating.setText(rating);

                float voteAverage = data.getFloat(COL_VOTE_AVG);
                mVoteAverage.setRating(voteAverage);

                String posterUrl = data.getString(COL_POSTER_URL);
                Picasso.with(getActivity()).load(posterUrl).into(mPoster);
            }

//                final int isFavorite = data.getInt(COL_IS_FAVORITE);
//
//
//                if (data.getInt(COL_IS_FAVORITE) == 1) {
//                    mFavoriteButton.setTextColor(getResources().getColor(R.color.red));
//                } else {
//                    mFavoriteButton.setTextColor(getResources().getColor(R.color.secondary_text));
//                }
//                mFavoriteButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isFavorite == 1) {
//                            mFavoriteButton.setTextColor(getResources().getColor(R.color.secondary_text));
//                            getActivity().getContentResolver().update(MovieContract.MovieDetailsEntry.CONTENT_URI,
//                                    unFavoriteMovie(),
//                                    MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = " + movieId,
//                                    null
//                            );
//                        } else {
//                            mFavoriteButton.setTextColor(getResources().getColor(R.color.red));
//                            getActivity().getContentResolver().update(MovieContract.MovieDetailsEntry.CONTENT_URI,
//                                    favoriteMovie(),
//                                    MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = " + movieId,
//                                    null
//                            );
//                        }
//                    }
//                });
            //}
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if(mTransitionAnimation){
            appCompatActivity.supportStartPostponedEnterTransition();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ContentValues favoriteMovie() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE, 1);
        return contentValues;
    }

    private ContentValues unFavoriteMovie() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieDetailsEntry.COLUMN_IS_FAVORITE, 0);
        return contentValues;
    }

    public void onSettingChanged(int movieId) {
//        Uri uri = mUri;
//        if (uri != null) {
//            Uri updatedUri = MovieContract.MovieDetailsEntry.builderMovieWithRatingAndTrailer(movieId);
//            mUri = updatedUri;
//            mMovieId = movieId;
//            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
//        }
    }

}