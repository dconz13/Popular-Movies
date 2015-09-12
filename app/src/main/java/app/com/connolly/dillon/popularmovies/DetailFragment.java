package app.com.connolly.dillon.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.com.connolly.dillon.popularmovies.data.MovieContract;


/**
 * Created by Dillon Connolly on 8/22/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String MOVIE_OBJ = "selectedMovie";
    public static final String POSITION = "position";
    public boolean isTwoPane;
    private Uri mUri;
    private int mPosition;
    private int mMovieId;
    private static final int DETAIL_LOADER = 0;

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
    private TextView mVoteAverage;
    private TextView mOverview;
    private TextView mRating;
    private ImageView mPoster;
    private TextView mReview1Author;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("movieId",mMovieId);
    }

    private TextView mReview1Content;
    private TextView mReview2Author;
    private TextView mReview2Content;
    private TextView mReview3Author;
    private TextView mReview3Content;
    private TextView mTrailer1Name;
    private TextView mTrailer2Name;
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
                //Log.v(LOG_TAG, "Position: " + mPosition);
            }
        } else {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                mUri = intent.getData();
                mPosition = intent.getIntExtra(POSITION, 0);
                mMovieId = intent.getIntExtra("movieId", 0);
                //Log.v(LOG_TAG, "ID: " + mMovieId);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mMovieTitle = (TextView) rootView.findViewById(R.id.textView_detail_title);
        mReleaseDate = (TextView) rootView.findViewById(R.id.textView_detail_release_date);
        mVoteAverage = (TextView) rootView.findViewById(R.id.textView_detail_average_vote);
        mOverview = (TextView) rootView.findViewById(R.id.textView_detail_overview);
        mRating = (TextView) rootView.findViewById(R.id.textView_detail_rating);
        mPoster = (ImageView) rootView.findViewById(R.id.imageView_detail_poster);
        mReview1Author = (TextView) rootView.findViewById(R.id.textView_user1);
        mReview1Content = (TextView) rootView.findViewById(R.id.textView_user1_comment);
        mReview2Author = (TextView)rootView.findViewById(R.id.textView_user2);
        mReview2Content = (TextView)rootView.findViewById(R.id.textView_user2_comment);
        mReview3Author = (TextView)rootView.findViewById(R.id.textView_user3);
        mReview3Content = (TextView)rootView.findViewById(R.id.textView_user3_comment);
        mTrailer1Name = (TextView) rootView.findViewById(R.id.textView_trailer1);
        mTrailer2Name = (TextView) rootView.findViewById(R.id.textView_trailer2);
        mDuration = (TextView) rootView.findViewById(R.id.textView_detail_duration);
        mFavoriteButton = (Button) rootView.findViewById(R.id.favorite_button);
        //getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
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
            for(int i = 0; i<data.getCount(); i++){
                if(data.getInt(COL_MOVIE_ID)!= mMovieId)
                    data.moveToNext();
            }
            if(data.getPosition() >= data.getCount()){
                data.moveToFirst();
            }
            else if(data.getInt(COL_MOVIE_ID) == mMovieId) {
                final int movieId = data.getInt(COL_MOVIE_ID);
                final int isFavorite = data.getInt(COL_IS_FAVORITE);

                String movieTitle = data.getString(COL_MOVIE_TITLE);
                mMovieTitle.setText(movieTitle);

                String releaseDate = data.getString(COL_RELEASE_DATE);
                mReleaseDate.setText(releaseDate);

                String voteAverage = data.getString(COL_VOTE_AVG);
                mVoteAverage.setText(voteAverage + "/10");

                String overview = data.getString(COL_OVERVIEW);
                mOverview.setText(overview);

                String rating = data.getString(COL_MOVIE_RATING);
                mRating.setText(rating);

                String posterUrl = data.getString(COL_POSTER_URL);
                Picasso.with(getActivity()).load(posterUrl).into(mPoster);

                String duration = data.getString(COL_MOVIE_LENGTH);
                mDuration.setText(duration + "m");

                String trailerName = data.getString(COL_TRAILER_NAME);
                String trailerKey = data.getString(COL_TRAILER_KEY);
                checkAndSetTrailer(trailerName, trailerKey, mTrailer1Name, mTrailer2Name);

                String reviewAuthor = data.getString(COL_REVIEW_AUTHOR);
                String reviewContent = data.getString(COL_REVIEW_CONTENT);
                checkAndSetReview(reviewAuthor, reviewContent);

                if (data.getInt(COL_IS_FAVORITE) == 1) {
                    mFavoriteButton.setTextColor(getResources().getColor(R.color.red));
                } else {
                    mFavoriteButton.setTextColor(getResources().getColor(R.color.secondary_text));
                }
                mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFavorite == 1) {
                            mFavoriteButton.setTextColor(getResources().getColor(R.color.secondary_text));
                            getActivity().getContentResolver().update(MovieContract.MovieDetailsEntry.CONTENT_URI,
                                    unFavoriteMovie(),
                                    MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = " + movieId,
                                    null
                            );
                        } else {
                            mFavoriteButton.setTextColor(getResources().getColor(R.color.red));
                            getActivity().getContentResolver().update(MovieContract.MovieDetailsEntry.CONTENT_URI,
                                    favoriteMovie(),
                                    MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " = " + movieId,
                                    null
                            );
                        }
                    }
                });
            }
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

    public void checkAndSetTrailer(String name, String key, TextView trailer1, TextView trailer2) {
        if (name != null && key != null) {
            if (name.contains("|")) {
                int index = name.indexOf('|');
                trailer1.setText(name.substring(0, index));
                trailer2.setText(name.substring((index + 1)));
                index = key.indexOf('|');
                final String key1 = key.substring(0, index);
                final String key2 = key.substring(index + 1);
                trailer1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + key1);
                        //Log.v(LOG_TAG, key1);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_VIEW);
                        sendIntent.setData(uri);
                        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(sendIntent);

                        }
                    }
                });
                trailer2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + key2);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_VIEW);
                        sendIntent.setData(uri);
                        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(sendIntent);

                        }
                    }
                });
            } else {
                if(name.equals("NT_TAG")){
                    trailer1.setText("No trailers :(");
                }
                else{
                    trailer1.setText(name);
                }
            }
        }
    }

    public void checkAndSetReview(String name, String comment) {
        if (name != null && comment != null) {
            int amtOfComments = name.length() - name.replace("|", "").length();
            int index;
            //Log.v(LOG_TAG,"AMTOFCOMMENTS: " + amtOfComments);
           // Log.v(LOG_TAG,"COMMENT: "+ comment);
            switch (amtOfComments) {
                case 2:
                    index = name.indexOf('|');
                    mReview1Author.setText(name.substring(0, index));
                    name = name.substring(index+1);
                    index = name.indexOf('|');
                    mReview2Author.setText(name.substring(0,index));
                    mReview3Author.setText(name.substring(index+1));

                    index = comment.indexOf('|');
                    mReview1Content.setText(comment.substring(0,index));
                    comment = comment.substring(index+1);
                    index = comment.indexOf('|');
                    mReview2Content.setText(comment.substring(0,index));
                    mReview3Content.setText(comment.substring(index+1));
//                    Log.v(LOG_TAG,"3 reviews!");
                    break;
                case 1:
                    index = name.indexOf('|');
                    mReview1Author.setText(name.substring(0,index));
                    mReview2Author.setText(name.substring(index+1));
                    index = comment.indexOf('|');
                    mReview1Content.setText(comment.substring(0,index));
                    mReview2Content.setText(comment.substring(index + 1));
//                    Log.v(LOG_TAG,"2 reviews!");
                    break;
                case 0:
                    if(name.equals("NR_TAG")){
                        mReview1Content.setText("No reviews :(");
                    }else {
                        mReview1Author.setText(name);
                        mReview1Content.setText(comment);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}