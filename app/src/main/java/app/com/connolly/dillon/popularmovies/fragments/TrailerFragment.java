package app.com.connolly.dillon.popularmovies.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.R;
import app.com.connolly.dillon.popularmovies.adapters.TrailerAdapter;

/**
 * Created by Dillon Connolly on 1/15/2016.
 */
public class TrailerFragment extends Fragment {
    private TrailerAdapter mTrailerAdapter;
    private ListView mListView;
    private TextView mEmptyListMessage;

    private ArrayList<String> mKeyHolder;
    private ArrayList<String> mTitleHolder;
    // Base link: "http://img.youtube.com/vi/VIDEO_ID/0.jpg" -> 480 x 360 img

    private static final String baseYoutubeUrl = "http://img.youtube.com/vi/VIDEO_ID/maxresdefault.jpg";
    private static final String TITLE_KEY = "title_key";
    private static final String KEY_KEY = "key_key";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_trailer, container, false);

        mListView = (ListView) rootView.findViewById(R.id.trailer_listview);
        mTrailerAdapter = new TrailerAdapter(getActivity(), R.layout.fragment_trailer, new ArrayList<String>());
        mListView.setAdapter(mTrailerAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + mKeyHolder.get(position));
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setData(uri);
                if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(sendIntent);
                }
            }
        });

        mEmptyListMessage = (TextView) rootView.findViewById(R.id.empty_trailer_text_view);

        if (savedInstanceState != null) {
            updateAdapter(savedInstanceState.getStringArrayList(KEY_KEY), savedInstanceState.getStringArrayList(TITLE_KEY));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(TITLE_KEY, mTitleHolder);
        outState.putStringArrayList(KEY_KEY, mKeyHolder);
        super.onSaveInstanceState(outState);
    }

    public ArrayList<String> parseTrailerString(String input) {
        ArrayList<String> resultList = new ArrayList<>();

        if (input.contains("|")) {
            for (String temp : input.split("[\\|]")) {
                resultList.add(temp);
            }
        } else {
            resultList.add(input);
        }

        return resultList;
    }

    // Only used for restoring the savedInstanceState on rotate
    private void updateAdapter(ArrayList<String> keys, ArrayList<String> titles) {
        if (mTrailerAdapter != null) {
            mTitleHolder = new ArrayList<>(titles);
            mKeyHolder = new ArrayList<>(keys);
            keys = buildImgUrl(keys);
            mTrailerAdapter.setTitles(titles);
            mTrailerAdapter.addAll(keys);
        }
    }

    public void updateAdapter(String keys, String titles) {
        if (mTrailerAdapter != null) {
            if (keys.equals("NT_TAG")) {
                mEmptyListMessage.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            } else {
                ArrayList<String> key_list = parseTrailerString(keys);
                ArrayList<String> title_list = parseTrailerString(titles);
                //Log.v("TrailerFragment", titles);
                mKeyHolder = new ArrayList<>(key_list);
                mTitleHolder = new ArrayList<>(title_list);
                key_list = buildImgUrl(key_list);
                mTrailerAdapter.setTitles(title_list);
                mTrailerAdapter.addAll(key_list);
            }
        }
    }

    public ArrayList<String> buildImgUrl(ArrayList<String> urls) {
        for (int i = 0; i < urls.size(); i++) {
            urls.set(i, baseYoutubeUrl.replace("VIDEO_ID", urls.get(i)));
        }
        return urls;
    }

}
