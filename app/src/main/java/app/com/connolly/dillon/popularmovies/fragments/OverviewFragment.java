package app.com.connolly.dillon.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.com.connolly.dillon.popularmovies.R;

/**
 * Created by Dillon Connolly on 1/15/2016.
 */
public class OverviewFragment extends Fragment {

    private TextView mOverview;
    private static final String OVERVIEW_KEY = "OV_KEY";
    private static final String LOG_TAG = OverviewFragment.class.getSimpleName();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save data so that fragment can restore the previous state
        outState.putCharSequence(OVERVIEW_KEY, mOverview.getText());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_overview, container, false);
        mOverview = (TextView) rootView.findViewById(R.id.textView_detail_overview);

        if (savedInstanceState != null) {
            setOverview((String)savedInstanceState.getCharSequence(OVERVIEW_KEY));
        }

        return rootView;
    }


    public void setOverview(String text) {
        if (mOverview != null) {
            mOverview.setText(text);
        }
    }
}
