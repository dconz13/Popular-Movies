package app.com.connolly.dillon.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.adapters.GridViewAdapter;

public class MoviesFragment extends Fragment implements OnAsyncCompletedListener{
    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    GridViewAdapter adapter;
    private static Context mContext;
    private ArrayList<MovieDataStructure> mMovieObjectArrayList;
    private String sortSetting;
    private OnAsyncCompletedListener listener;

    public interface Callback {

        public void onItemSelected(MovieDataStructure selectedMovie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movieArrayList")
                || (savedInstanceState.get("sortSetting")!= PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("sort", "popularity.desc"))){
            updateMovieList();
            Log.v(LOG_TAG,"UPDATING MOVIE LIST");
        }
        else{
            mMovieObjectArrayList = savedInstanceState.getParcelableArrayList("movieArrayList");
            sortSetting = savedInstanceState.getString("sortSetting");
            Log.v(LOG_TAG,"RESTORING MOVIE LIST");
        }
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieArrayList", mMovieObjectArrayList);
        outState.putString("sortSetting",sortSetting);
        super.onSaveInstanceState(outState);
    }

    public MoviesFragment() {
        // Uncomment if refresh button is necessary. Otherwise leave it.
        setHasOptionsMenu(true);
        mMovieObjectArrayList = new ArrayList<MovieDataStructure>();
        listener = new OnAsyncCompletedListener() {
            @Override
            public void OnTaskCompleted(MovieDataStructure[] movies) {
                if(movies!=null) {
                    adapter.clear();
                    adapter.addAll(movies);

                    for(MovieDataStructure movie : movies){
                        if(movie.getRating() == null) {
                            new FetchDetailsTask(listener).execute(movie);
                        }
                    }
                }
                else{
                    networkErrorToast();
                }
            }

            @Override
            public void OnDetailTaskCompleted(MovieDataStructure movieDetail) {
                if(movieDetail!=null){
                    for(MovieDataStructure movie: mMovieObjectArrayList){
                        int index = mMovieObjectArrayList.indexOf(movie);
                        if(movieDetail.getId() == (mMovieObjectArrayList.get(index).getId())){
                            mMovieObjectArrayList.set(index,movieDetail);
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_refresh){
            // TODO: Enable this as needed by programmer. Enabled by uncommenting setHasOptionsMenu from MoviesFragment()
            updateMovieList();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new GridViewAdapter(mContext, R.layout.grid_item_movies, R.id.grid_item_movies_imageView, mMovieObjectArrayList);
        GridView myGrid = (GridView)rootView.findViewById(R.id.gridview_movies);
        myGrid.setAdapter(adapter);
        myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDataStructure selectedPoster = mMovieObjectArrayList.get(position);

                ((Callback)getActivity())
                        .onItemSelected(selectedPoster);
            }
        });
        return rootView;
    }

    public void networkErrorToast(){
        Toast toast = Toast.makeText(getActivity(),"Could not connect to Internet! Check connection settings and refresh.",Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onStart(){
        super.onStart();
        if(sortSetting != PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("sort","popularity.desc")){
            updateMovieList();
        }
    }

    private void updateMovieList(){
        sortSetting = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString("sort","popularity.desc");
        //Log.v(LOG_TAG,"Sort Method: " + sortMethod);
        new FetchMoviesTask(listener,mMovieObjectArrayList).execute(sortSetting);
    }

    @Override
    public void OnTaskCompleted(MovieDataStructure[] movies) {
        if(movies!=null) {
            adapter.clear();
            adapter.addAll(movies);
        }
    }

    @Override
    public void OnDetailTaskCompleted(MovieDataStructure movieDetail) {

    }
}
