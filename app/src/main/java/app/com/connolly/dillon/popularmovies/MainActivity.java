package app.com.connolly.dillon.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements MoviesFragment.Callback {

    boolean mTwoPane;
    private final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container)!= null){
            mTwoPane = true;
            DetailFragment frag = new DetailFragment();
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, frag, DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else{
          mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        MoviesFragment moviesFragment = ((MoviesFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment));
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
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieDataStructure selectedMovie) {
        if(mTwoPane){
            if(selectedMovie != null) {
                Bundle args = new Bundle();
                args.putParcelable(DetailFragment.MOVIE_OBJ, selectedMovie);

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(args);
                detailFragment.isTwoPane = true;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("selectedMovie", selectedMovie);
            startActivity(intent);
        }
    }
}
