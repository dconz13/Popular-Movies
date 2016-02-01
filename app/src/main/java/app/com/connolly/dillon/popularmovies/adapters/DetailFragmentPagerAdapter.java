package app.com.connolly.dillon.popularmovies.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.com.connolly.dillon.popularmovies.fragments.OverviewFragment;
import app.com.connolly.dillon.popularmovies.fragments.CommentsFragment;
import app.com.connolly.dillon.popularmovies.fragments.TrailerFragment;

/**
 * Created by Dillon Connolly on 1/15/2016.
 * This Adapter handles the View Pager that is within the Detail Fragment. It contains methods to
 * send data to the other fragments that it has as different tabs of the pager.
 */
public class DetailFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;
    private OverviewFragment overviewFragment;
    private TrailerFragment trailerFragment;
    private CommentsFragment commentsFragment;

    public DetailFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        overviewFragment = new OverviewFragment();
        trailerFragment = new TrailerFragment();
        commentsFragment = new CommentsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return overviewFragment;
            case 1:
                return trailerFragment;
            case 2:
                return commentsFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Overview";
            case 1:
                return "Trailers";
            case 2:
                return "Reviews";
            default:
                return null;
        }
    }

    public void setOverviewFragmentVariable(String text) {
        overviewFragment.setOverview(text);
    }

    public void setTrailerFragmentVariable(String keys, String titles) {
        trailerFragment.updateAdapter(keys, titles);

    }

    public void setCommentsFragmentVariable(String authors, String comments) {
        commentsFragment.updateAdapter(authors, comments);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
