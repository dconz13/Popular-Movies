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
import java.util.List;

/**
 * Created by Dillon Connolly on 8/22/2015.
 */
public class FetchDetailsTask extends AsyncTask<MovieDataStructure,Void,MovieDataStructure> {
    private static final String LOG_TAG = FetchDetailsTask.class.getSimpleName();
    private final String api_key = null; // get an api key from themoviedb.org
    private OnAsyncCompletedListener listener;

    FetchDetailsTask(OnAsyncCompletedListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(MovieDataStructure movieDataStructure) {
        listener.OnDetailTaskCompleted(movieDataStructure);
    }

    @Override
    protected MovieDataStructure doInBackground(MovieDataStructure... params) {
        if(params[0] == null)
            return null;

        MovieDataStructure movieDataStructure = params[0];
        HttpURLConnection urlConnection1 = null;
        HttpURLConnection urlConnection2 = null;
        HttpURLConnection urlConnection3 = null;
        String trailerJsonStr = null;
        String reviewJsonStr = null;
        String ratingJsonStr = null;

        try{
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Integer.toString(movieDataStructure.getId()))
                    .appendPath("videos")
                    .appendQueryParameter("api_key",api_key);
            String trailerUrlStr = uriBuilder.toString();
            URL trailerUrl = new URL(trailerUrlStr);

            Uri.Builder uriBuilder2 = new Uri.Builder();
            uriBuilder2.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Integer.toString(movieDataStructure.getId()))
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", api_key);
            String reviewsUrlStr = uriBuilder2.toString();
            URL reviewUrl = new URL(reviewsUrlStr);

            Uri.Builder uriBuilder3 = new Uri.Builder();
            uriBuilder3.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(Integer.toString(movieDataStructure.getId()))
                    .appendPath("releases")
                    .appendQueryParameter("api_key", api_key);
            String ratingUrlStr = uriBuilder3.toString();
            URL ratingUrl = new URL(ratingUrlStr);

            urlConnection1 = (HttpURLConnection)trailerUrl.openConnection();
            urlConnection1.setRequestMethod("GET");
            urlConnection1.connect();
            urlConnection2 = (HttpURLConnection)reviewUrl.openConnection();
            urlConnection2.setRequestMethod("GET");
            urlConnection2.connect();
            urlConnection3 = (HttpURLConnection)ratingUrl.openConnection();
            urlConnection3.setRequestMethod("GET");
            urlConnection3.connect();

            InputStream inputStream = urlConnection1.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                // For some reason there was no inputStream. WTF
                return null;
            }
            buffer = readBuffer(inputStream);
            if(buffer == null){
                return null;
            }
            trailerJsonStr = buffer.toString();

            inputStream = urlConnection2.getInputStream();

            if(inputStream == null){
                // For some reason there was no inputStream. WTF
                return null;
            }
            buffer = readBuffer(inputStream);
            if(buffer == null){
                return null;
            }
            reviewJsonStr = buffer.toString();

            inputStream = urlConnection3.getInputStream();
            buffer = new StringBuffer();

            if(inputStream == null){
                // For some reason there was no inputStream. WTF
                return null;
            }
            buffer = readBuffer(inputStream);
            if(buffer == null){
                return null;
            }
            ratingJsonStr = buffer.toString();


        }catch(IOException e){
            Log.e(LOG_TAG,"Error: ", e);
        }finally{
            if(urlConnection1 != null){
                urlConnection1.disconnect();
            }
            if(urlConnection2 != null){
                urlConnection2.disconnect();
            }
            if(urlConnection3 != null){
                urlConnection3.disconnect();
            }
        }
        try{
            // Messy but the only way I could think to do it.
            if(trailerJsonStr!=null) {
                List<String[]> trailerInfo = getTrailersFromJSON(trailerJsonStr);
                movieDataStructure.setTrailers(trailerInfo.get(0));
                movieDataStructure.setTrailerKey(trailerInfo.get(1));
            }
            if(reviewJsonStr!=null) {
                List<String[]> reviewInfo = getReviewsFromJSON(reviewJsonStr);
                movieDataStructure.setReviews(reviewInfo.get(0));
                movieDataStructure.setReviewAuthor(reviewInfo.get(1));
            }
            if(ratingJsonStr!=null) {
                movieDataStructure.setRating(getRatingFromJSON(ratingJsonStr));
            }
            return movieDataStructure;
        }catch(JSONException e){
            Log.e(LOG_TAG,"Error: ", e);
        }
        return null;
    }

    /*
        I need the trailer name and the key for the youtube video. The only way I could think of to
        make this work with my data structure was to bind the two String[] arrays into a list to return
        and then set the variables in the movieDataStructure object. This allows me to still have a
        parcelable object for when I pass the explicit intent to the DetailActivity.
     */

    public List<String[]> getTrailersFromJSON(String value) throws JSONException {
        final String LOG_TAG = "getTrailersFromJSON";
        final String OWM_TRAILER_LIST = "results";
        final String OWM_TRAILER_NAME = "name";
        final String OWM_TRAILER_KEY = "key";
        List<String[]> list = new ArrayList<String[]>();

        JSONObject trailerResults = new JSONObject(value);
        JSONArray trailerDetails = trailerResults.getJSONArray(OWM_TRAILER_LIST);

        String[] trailerName = new String[trailerDetails.length()];
        String[] trailerKey = new String[trailerDetails.length()];
        for(int i = 0; i<trailerDetails.length(); i++){
            JSONObject currentTrailer = trailerDetails.getJSONObject(i);
            trailerName[i] = currentTrailer.getString(OWM_TRAILER_NAME);
            trailerKey[i] = currentTrailer.getString(OWM_TRAILER_KEY);
        }

        list.add(trailerName);
        list.add(trailerKey);

        return list;
    }

    public List<String[]> getReviewsFromJSON(String value)throws JSONException {
        final String LOG_TAG = "getReviewsFromJSON";
        final String OWM_REVIEW_LIST = "results";
        final String OWM_REVIEW_AUTHOR = "author";
        final String OWM_REVIEW_CONTENT = "content";
        List<String[]> list = new ArrayList<String[]>();

        JSONObject reviewResults = new JSONObject(value);
        JSONArray reviewDetails = reviewResults.getJSONArray(OWM_REVIEW_LIST);

        String[] reviewAuthor = new String[reviewDetails.length()];
        String[] reviewContent = new String[reviewDetails.length()];
        for(int i = 0; i<reviewDetails.length(); i++){
            JSONObject currentTrailer = reviewDetails.getJSONObject(i);
            reviewAuthor[i] = currentTrailer.getString(OWM_REVIEW_AUTHOR);
            reviewContent[i] = currentTrailer.getString(OWM_REVIEW_CONTENT);
        }

        list.add(reviewAuthor);
        list.add(reviewContent);

        return list;
    }

    public String getRatingFromJSON(String value) throws JSONException {
        final String LOG_TAG = "getRatingFromJSON";
        final String OWM_COUNTRY_LIST = "countries";
        final String OWM_CERTIFICATION = "certification";
        final String OWM_COUNTRY = "iso_3166_1";

        JSONObject certResults = new JSONObject(value);
        JSONArray countries = certResults.getJSONArray(OWM_COUNTRY_LIST);
        String certification = null;

        for(int i =0; i<countries.length(); i++){
            JSONObject currentCountry = countries.getJSONObject(i);
            if(currentCountry.getString(OWM_COUNTRY).equals("US"))
                certification = currentCountry.getString(OWM_CERTIFICATION);
        }
        if(certification == null)
            certification = "UR";
        return certification;
    }

    public StringBuffer readBuffer(InputStream inputStream){
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if(buffer.length() == 0){
                return null;
            }
        }catch(IOException e){
            Log.e(LOG_TAG + " in readBuffer","Error: ", e);
        }finally{
            if(reader!= null){
                try{
                    reader.close();
                }catch(IOException e){
                    Log.e(LOG_TAG + " in readBuffer","Error: ", e);
                }
            }
            return buffer;
        }
    }
}
