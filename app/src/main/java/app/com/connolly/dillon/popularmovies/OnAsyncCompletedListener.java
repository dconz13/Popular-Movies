package app.com.connolly.dillon.popularmovies;

/**
 * Created by Dillon Connolly on 7/14/2015.
 *
 * As recommended in my project review, I have created a listener to update the array adapter
 * instead of doing it from postExecute in AsyncTask.
 */
public interface OnAsyncCompletedListener {
    void OnTaskCompleted(MovieDataStructure[] movies);
    void OnDetailTaskCompleted(MovieDataStructure movieDetail);
}
