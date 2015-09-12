package app.com.connolly.dillon.popularmovies;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements MoviesFragment.Callback {

    boolean mTwoPane;
    private final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mSortSetting;
    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSortSetting = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(this.getString(R.string.pref_sort_key),
                        this.getString(R.string.pref_sort_default));

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            DetailFragment frag = new DetailFragment();
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, frag, DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortSetting = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(this.getString(R.string.pref_sort_key),
                        this.getString(R.string.pref_sort_default));
        if (sortSetting != null && !sortSetting.equals(mSortSetting)) {
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            if (moviesFragment != null) {
                moviesFragment.onSettingChanged();
            }
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if (detailFragment != null) {
                detailFragment.onSettingChanged(mMovieId);
            }
            mSortSetting = sortSetting;
        }
    }

    @Override
    public void onItemSelected(Uri selectedMovie, int position, int movieId) {
        mMovieId = movieId;
        if (mTwoPane) {
            if (selectedMovie != null) {
                Bundle args = new Bundle();
                args.putParcelable(DetailFragment.MOVIE_OBJ, selectedMovie);
                args.putInt(DetailFragment.POSITION, position);
                args.putInt("movieId", movieId);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(args);
                detailFragment.isTwoPane = true;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(selectedMovie)
                    .putExtra(DetailFragment.POSITION, position)
                    .putExtra("movieId", movieId);
            //Log.v("Main Activity", "Position: " );
            startActivity(intent);
        }
    }
}
