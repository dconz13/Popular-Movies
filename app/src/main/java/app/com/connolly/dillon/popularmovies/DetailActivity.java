package app.com.connolly.dillon.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import app.com.connolly.dillon.popularmovies.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity implements DetailFragment.Callback {

    private ViewPager mFragmentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_fragment_toolbar);
//        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.MOVIE_OBJ, getIntent().getData());
            //args.putBoolean(DetailFragment.TRANSITION_ANIMATION, true);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, fragment)
                    .commit();

            supportPostponeEnterTransition();
        }

    }

    @Override
    public void onBackPressed() {
        if(mFragmentPager.getCurrentItem() == 0){
            super.onBackPressed();
        } else {
            mFragmentPager.setCurrentItem(mFragmentPager.getCurrentItem() - 1);
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

    // This is called from the Detail fragment callback because the ViewPager is within the Detail
    // Fragment layout file. As soon as it's created the Detail fragment sends the reference back
    // to the activity so that there can be special onBackPressed functionality.

    @Override
    public void setPagerVariable(ViewPager pager) {
        this.mFragmentPager = pager;
    }
}
