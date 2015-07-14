package app.com.connolly.dillon.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoviesFragment extends Fragment implements OnAsyncCompletedListener{
    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    GridViewAdapter adapter;
    private static Context mContext;
    private String mImageSize = "w185";
    private MovieDataStructure[] mMovieListResults;
    private ArrayList<MovieDataStructure> mMovieObjectArrayList;
    private int pageNumber;
    private String sortSetting;
    private OnAsyncCompletedListener listener;

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
                }
                else{
                    networkErrorToast();
                }
            }
        };
        pageNumber = 0;
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

                // Create a new explicit intent
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("selectedMovie", selectedPoster);

                startActivity(detailIntent);
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
        new FetchMoviesTask(listener).execute(sortSetting);
    }

        /* A similar GridViewAdapter implementation was found on stack overflow as a
         * combined effort of users: user2029585 and James Baxter
         * URL: http://stackoverflow.com/questions/25651867/populate-gridview-from-array
         *
         * My code is a combination of referencing how they implemented a custom adapter and referencing the
         * documentation for the ArrayAdapter class. */

    public class GridViewAdapter extends ArrayAdapter<MovieDataStructure> {
        Context mContext;
        private ArrayList<MovieDataStructure> mObjects;
        private int mImageViewResourceId;
        private int mResource;

        public GridViewAdapter(Context context,int resource, int imageViewResourceId, ArrayList<MovieDataStructure> objects){
            super(context,resource,imageViewResourceId,objects);
            this.mContext = context;
            this.mResource = resource;
            this.mImageViewResourceId = imageViewResourceId;
            this.mObjects = objects;
        }
        public int getCount() {
            return mObjects.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View imageView;

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                imageView = inflater.inflate(mResource, parent, false);
            }
            else{
                imageView = (View)convertView;
            }

            // this code must be outside the if statements so images don't repeat.
            ImageView gridImageView = (ImageView) imageView.findViewById(mImageViewResourceId);

            Picasso.with(mContext)
                    .load(mObjects.get(position).getPosterUrl())
                    .error(R.drawable.error)
                    .placeholder(R.drawable.loading_animation)
                    .into(gridImageView);

            return imageView;
        }
    }

    @Override
    public void OnTaskCompleted(MovieDataStructure[] movies) {
        if(movies!=null) {
            adapter.clear();
            adapter.addAll(movies);
        }
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,MovieDataStructure[]>{

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String apiKey = "92ac2720e1ed27c5e4dd88c0b4fa1e1b";
        private OnAsyncCompletedListener listener;

        public FetchMoviesTask(OnAsyncCompletedListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(MovieDataStructure[] movies) {
            if(listener!=null) {
                listener.OnTaskCompleted(movies);
            }
        }

        @Override
        protected MovieDataStructure[] doInBackground(String... params) {
            if(params.length == 0) {
                return null;
            }
            String sortBy = params[0];
            if(pageNumber == 0){pageNumber = 1;}
            // Close these variables in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try{
                // API Key = 92ac2720e1ed27c5e4dd88c0b4fa1e1b
                //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=92ac2720e1ed27c5e4dd88c0b4fa1e1b");
                // TODO: Create an onScrollListener and check if the list is at the bottom. If it is then change increment page variable. Decrement at top.
                Uri.Builder urlBuilder = new Uri.Builder();
                urlBuilder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("sort_by",sortBy)
                        .appendQueryParameter("api_key",apiKey)
                        .appendQueryParameter("page",Integer.toString(pageNumber));
                String myUrl = urlBuilder.toString();
                URL url = new URL(myUrl);

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    // For some reason there was no inputStream. WTF
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    // Adding the new line isn't necessary. It's just for human reading purposes
                    buffer.append(line + "\n");
                }
                if(buffer.length() == 0){
                    // WTF buffer length was 0. Don't bother parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                //Log.v(LOG_TAG,"Movie JSON String: " + movieJsonStr);

            }catch(IOException e){
                Log.e(LOG_TAG, "Error: ", e);
                return null;
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    }catch(IOException e){
                        Log.e(LOG_TAG,"Error closing BufferedReader: ", e);
                    }
                }
            }
            try {
                mMovieListResults = getMovieInfoFromJSON(movieJsonStr);
                mMovieObjectArrayList.clear();
                for(int i = 0; i<mMovieListResults.length;i++){
                    mMovieObjectArrayList.add(mMovieListResults[i]);
                    //Log.v(LOG_TAG, "POSTER URL: " + mMovieListResults[i].getPosterUrl());
                }
                return mMovieListResults;
            }catch(JSONException e){
                Log.e(LOG_TAG,"JSON ERROR: ", e);
                return null;
            }
        }
    }

    public MovieDataStructure[] getMovieInfoFromJSON(String movieJsonStr) throws JSONException{
        final String LOG_TAG = "getMovieInfoFromJSON";

        // Movie detail layout contains title, release date, movie poster, vote average, and plot synopsis
        // Objects to be extracted:
        final String OWM_VOTE_AVG = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_TITLE = "original_title";
        final String OWM_PLOT_SUMMARY = "overview";
        final String OWM_POSTER_END_URL = "poster_path";
        final String OWM_MOVIE_LIST = "results";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_MOVIE_LIST);

        MovieDataStructure[] results = new MovieDataStructure[movieArray.length()];
        String[] posterUrls = new String[movieArray.length()];
        String title;
        String plotSummary;
        String averageVote;
        String releaseDate;
        String posterUrl;

        for(int i=0;i<movieArray.length();i++ ){
            //This points to the current movie in the array
            JSONObject currentMovie = movieArray.getJSONObject(i);
            title = currentMovie.getString(OWM_TITLE);
            plotSummary = currentMovie.getString(OWM_PLOT_SUMMARY);
            averageVote = currentMovie.getString(OWM_VOTE_AVG);
            releaseDate = currentMovie.getString(OWM_RELEASE_DATE);
            posterUrl = currentMovie.getString(OWM_POSTER_END_URL);
            posterUrls[i] = posterUrl;
            results[i] = new MovieDataStructure(title,plotSummary,averageVote,releaseDate,posterUrl);
        }
        posterUrls = buildPosterURLFromJSON(posterUrls);

        for(int i = 0; i<movieArray.length();i++){
            results[i].setPosterUrl(posterUrls[i]);
        }


        return results;
    }

    public String[] buildPosterURLFromJSON(String[] movieUrls) {
        final String LOG_TAG = "getPosterURLFromJson";

        String[] fullUrls =  movieUrls;
        for(int i = 0; i<fullUrls.length;i++){
            fullUrls[i] = "http://image.tmdb.org/t/p/" + mImageSize + "/" + fullUrls[i];
        }
        return fullUrls;
    }


}
