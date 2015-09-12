package app.com.connolly.dillon.popularmovies.API;


import app.com.connolly.dillon.popularmovies.Model.AllMovieResults;
import app.com.connolly.dillon.popularmovies.Model.DiscoverResults;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Dillon Connolly on 9/2/2015.
 */
public interface MovieService {
    // Request method and URL
    // Last parameter is the callback with the parsed response

    @GET("/discover/movie")
    public void getMovieList(@Query("sort_by") String sort, @Query("api_key") String api_key, Callback<DiscoverResults> discoverResultsCallback);

    @GET("/movie/{id}")
    public void getMovieInfo(@Path("id") int movieId, @Query("api_key") String api_key, @Query("append_to_response") String params, Callback<AllMovieResults> allMovieResultsCallback);

}
