package app.com.connolly.dillon.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Dillon Connolly on 8/20/2015.
 */
public class FetchMoviesTask extends AsyncTask<String,Void,MovieDataStructure[]> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final String apiKey = null; //get an api key from themoviedb.org
    private OnAsyncCompletedListener listener;
    private int pageNumber = 0; // TODO: Have it save page to instance
    private String mImageSize = "w342"; // TODO: Adjust this for screen size
    private MovieDataStructure[] mMovieListResults;
    private ArrayList<MovieDataStructure> mMovieObjectArrayList;

    public FetchMoviesTask(OnAsyncCompletedListener listener, ArrayList<MovieDataStructure> test){
        this.mMovieObjectArrayList = test;
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
            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=92ac2720e1ed27c5e4dd88c0b4fa1e1b");
            // TODO: Create an onScrollListener and check if the list is at the bottom. If it is then change increment page variable and get more data to add to adapter.
            Uri.Builder urlBuilder = new Uri.Builder();
            urlBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by",sortBy)
                    .appendQueryParameter("api_key",apiKey)
                    .appendQueryParameter("page",Integer.toString(pageNumber));
            String movieListUrl = urlBuilder.toString();
            URL url = new URL(movieListUrl);

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
        final String OWM_MOVIE_ID = "id";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_MOVIE_LIST);

        MovieDataStructure[] results = new MovieDataStructure[movieArray.length()];
        String[] posterUrls = new String[movieArray.length()];
        String title;
        String plotSummary;
        String averageVote;
        String releaseDate;
        String posterUrl;
        int id;

        for(int i=0;i<movieArray.length();i++ ){
            //This points to the current movie in the array
            JSONObject currentMovie = movieArray.getJSONObject(i);
            title = currentMovie.getString(OWM_TITLE);
            plotSummary = currentMovie.getString(OWM_PLOT_SUMMARY);
            averageVote = currentMovie.getString(OWM_VOTE_AVG);
            releaseDate = currentMovie.getString(OWM_RELEASE_DATE);
            posterUrl = currentMovie.getString(OWM_POSTER_END_URL);
            id = currentMovie.getInt(OWM_MOVIE_ID);
            posterUrls[i] = posterUrl;
            results[i] = new MovieDataStructure(title,plotSummary,averageVote,releaseDate,posterUrl,id);
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