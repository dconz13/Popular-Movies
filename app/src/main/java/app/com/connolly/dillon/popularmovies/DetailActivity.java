package app.com.connolly.dillon.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailFragment extends Fragment {
        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        // TODO: Handle Intent.extra stuff to display Movie crap and poster
        public DetailFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            if(intent!=null) {
                Bundle data = getActivity().getIntent().getExtras();
               MovieDataStructure selectedMovie = data.getParcelable("selectedMovie");
               ((TextView) rootView.findViewById(R.id.textView_detail_title))
                       .setText(selectedMovie.getTitle());
               ((TextView) rootView.findViewById(R.id.textView_detail_release_date))
                        .setText(selectedMovie.getReleaseDate());
               ((TextView) rootView.findViewById(R.id.textView_detail_average_vote))
                        .setText(selectedMovie.getAverageVote()+"/10");
               ((TextView) rootView.findViewById(R.id.textView_detail_plot))
                        .setText(selectedMovie.getPlotSummary());
                Picasso.with(getActivity()).load(selectedMovie.getPosterUrl()).into((ImageView)rootView.findViewById(R.id.imageView_detail_poster));

           }
            return rootView;
        }
    }
}
