package app.com.connolly.dillon.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.adapters.TrailerViewAdapter;


/**
 * Created by Dillon Connolly on 8/22/2015.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    // TODO: Handle Intent.extra stuff to display Movie crap and poster
    public static final String MOVIE_OBJ = "selectedMovie";
    MovieDataStructure mSelectedMovie;
    public boolean isTwoPane;
    TextView trailer1;
    TextView trailer2;
    ArrayList<Trailer> trailers;

    public DetailFragment() {
        trailers = new ArrayList<>();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState == null || !savedInstanceState.containsKey("selectedMovie") || mSelectedMovie.getRating() == null){
//            getMovieDetails();
//        }
//        else{
//            mSelectedMovie = savedInstanceState.getParcelable("selectedMovie");
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable("selectedMovie", mSelectedMovie);
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (intent != null) {

            final TextView trailer1View = (TextView) rootView.findViewById(R.id.textView_trailer1);
            final TextView trailer2View = (TextView) rootView.findViewById(R.id.textView_trailer2);
            final Button favorite = (Button) rootView.findViewById(R.id.favorite_button);

            trailer1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + trailers.get(0).key);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_VIEW);
                    sendIntent.setData(uri);
                    if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(sendIntent);

                    }
                }
            });
            trailer2View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + trailers.get(1).key);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_VIEW);
                    sendIntent.setData(uri);
                    if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(sendIntent);

                    }
                }
            });

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Add to favorite list.
                }
            });


            Bundle data = getArguments();
            if(data != null) {

                if(isTwoPane){
                    mSelectedMovie = data.getParcelable("selectedMovie");
                } else{
                    mSelectedMovie = getActivity().getIntent().getParcelableExtra("selectedMovie");
                }

                if(mSelectedMovie!= null) {
                    String[] trailerName = mSelectedMovie.getTrailers();
                    String[] trailerKey = mSelectedMovie.getTrailerKey();

                    ((TextView) rootView.findViewById(R.id.textView_detail_title))
                            .setText(mSelectedMovie.getTitle());
                    ((TextView) rootView.findViewById(R.id.textView_detail_release_date))
                            .setText(mSelectedMovie.getReleaseDate());
                    ((TextView) rootView.findViewById(R.id.textView_detail_average_vote))
                            .setText(mSelectedMovie.getAverageVote() + "/10");
                    ((TextView) rootView.findViewById(R.id.textView_detail_overview))
                            .setText(mSelectedMovie.getPlotSummary());
                    ((TextView) rootView.findViewById(R.id.textView_detail_rating))
                            .setText(mSelectedMovie.getRating());

                    checkAndSetTrailer(trailerName, trailerKey, trailer1View, trailer2View);
                    checkAndSetReview(mSelectedMovie.getReviewAuthor(), mSelectedMovie.getReviews(), rootView);

                    Picasso.with(getActivity()).load(mSelectedMovie.getPosterUrl()).into((ImageView) rootView.findViewById(R.id.imageView_detail_poster));
                }
            }
        }
        return rootView;
    }

    public void checkAndSetTrailer(String[] name, String[] key, TextView trailer1, TextView trailer2) {
        if (name != null && key != null) {
            int amtTrailers = ((name.length >= key.length) ? key.length : name.length);
            switch (amtTrailers) {
                case 5:
                case 4:
                case 3:
                case 2:
                    trailer1.setText(name[0]);
                    trailer2.setText(name[1]);
                    trailers.add(0, new Trailer(name[0], key[0]));
                    trailers.add(1, new Trailer(name[1], key[1]));
                    break;
                case 1:
                    trailer1.setText(name[0]);
                    trailers.add(0, new Trailer(name[0], key[0]));
                    break;
                case 0:
                default:
                    break;
            }
        }
    }

    public void checkAndSetReview(String[] name, String[] comment, View view) {
        if (name != null && comment != null) {
            int amtComments = name.length;
            if (amtComments > 3) {
                amtComments = 3;
            }
            TextView user1 = (TextView) view.findViewById(R.id.textView_user1);
            TextView user2 = (TextView) view.findViewById(R.id.textView_user2);
            TextView user3 = (TextView) view.findViewById(R.id.textView_user3);
            TextView user1Comment = (TextView) view.findViewById(R.id.textView_user1_comment);
            TextView user2Comment = (TextView) view.findViewById(R.id.textView_user2_comment);
            TextView user3Comment = (TextView) view.findViewById(R.id.textView_user3_comment);
            switch (amtComments) {
                case 3:
                    user1.setText(name[0]);
                    user1Comment.setText(comment[0]);
                    user2.setText(name[1]);
                    user2Comment.setText(comment[1]);
                    user3.setText(name[2]);
                    user3Comment.setText(comment[2]);
                    break;
                case 2:
                    user1.setText(name[0]);
                    user1Comment.setText(comment[0]);
                    user2.setText(name[1]);
                    user2Comment.setText(comment[1]);
                    break;
                case 1:
                    user1.setText(name[0]);
                    user1Comment.setText(comment[0]);
                    break;
                default:
                    break;
            }
        }
    }

    public class Trailer {
        public String name;
        public String key;

        public Trailer(String name, String key) {
            this.name = name;
            this.key = key;
        }
    }

}