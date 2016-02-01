package app.com.connolly.dillon.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.R;
import app.com.connolly.dillon.popularmovies.adapters.CommentsAdapter;

/**
 * Created by Dillon Connolly on 1/15/2016.
 */
public class CommentsFragment extends Fragment {

    // TODO: Implement empty list functionality
    private CommentsAdapter mCommentsAdapter;
    private ListView mListView;
    private ArrayList<String> mAuthorHolder;
    private ArrayList<String> mCommentHolder;

    private static final String AUTHOR_KEY = "ar_key";
    private static final String COMMENT_KEY = "comm_key";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_comments, container, false);
        mListView = (ListView) rootView.findViewById(R.id.comments_list);

        mCommentsAdapter = new CommentsAdapter(getActivity(), R.layout.fragment_comments, new ArrayList<String>());
        mListView.setAdapter(mCommentsAdapter);

        if(savedInstanceState != null){
            updateAdapter(savedInstanceState.getStringArrayList(AUTHOR_KEY), savedInstanceState.getStringArrayList(COMMENT_KEY));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(AUTHOR_KEY,mAuthorHolder);
        outState.putStringArrayList(COMMENT_KEY, mCommentHolder);
        super.onSaveInstanceState(outState);
    }

    public ArrayList<String> parseCommentString(String input) {
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
    private void updateAdapter(ArrayList<String> authors, ArrayList<String> comments){
        if(mCommentsAdapter!= null){
            mAuthorHolder = new ArrayList<>(authors);
            mCommentHolder = new ArrayList<>(comments);

            mCommentsAdapter.setAuthors(authors);
            mCommentsAdapter.addAll(comments);
        }
    }

    public void updateAdapter(String authors, String comments) {
        if (mCommentsAdapter != null) {
            if (authors.equals("NR_TAG")) {
                //mEmptyListMessage.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            } else {
                ArrayList<String> author_list = parseCommentString(authors);
                ArrayList<String> comment_list = parseCommentString(comments);
                mAuthorHolder = new ArrayList<>(author_list);
                mCommentHolder = new ArrayList<>(comment_list);

                mCommentsAdapter.setAuthors(author_list);
                mCommentsAdapter.addAll(comment_list);
            }
        }
    }
}
