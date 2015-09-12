package app.com.connolly.dillon.popularmovies.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Auto generated using jsonschema2pojo
// This is the json results for each individual movie with all the info collected together.

public class AllMovieResults {

    private boolean adult;
    private String backdropPath;
    private BelongsToCollection belongsToCollection;
    private int budget;
    private List<Genre> genres = new ArrayList<Genre>();
    private String homepage;
    private int id;
    private String imdb_id;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private float popularity;
    private String poster_path;
    private List<ProductionCompany> productionCompanies = new ArrayList<ProductionCompany>();
    private List<ProductionCountry> productionCountries = new ArrayList<ProductionCountry>();
    private String release_date;
    private int revenue;
    private int runtime;
    private List<SpokenLanguage> spokenLanguages = new ArrayList<SpokenLanguage>();
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private float vote_average;
    private int vote_count;
    private Videos videos;
    private Reviews reviews;
    private Releases releases;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The adult
     */
    public boolean isAdult() {
        return adult;
    }

    /**
     *
     * @param adult
     * The adult
     */
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    /**
     *
     * @return
     * The backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     *
     * @param backdropPath
     * The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /**
     *
     * @return
     * The belongsToCollection
     */
    public BelongsToCollection getBelongsToCollection() {
        return belongsToCollection;
    }

    /**
     *
     * @param belongsToCollection
     * The belongs_to_collection
     */
    public void setBelongsToCollection(BelongsToCollection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    /**
     *
     * @return
     * The budget
     */
    public int getBudget() {
        return budget;
    }

    /**
     *
     * @param budget
     * The budget
     */
    public void setBudget(int budget) {
        this.budget = budget;
    }

    /**
     *
     * @return
     * The genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     * The genres
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     *
     * @return
     * The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     *
     * @param homepage
     * The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The imdbId
     */
    public String getImdbId() {
        return imdb_id;
    }

    /**
     *
     * @param imdbId
     * The imdb_id
     */
    public void setImdbId(String imdbId) {
        this.imdb_id = imdbId;
    }

    /**
     *
     * @return
     * The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     *
     * @param originalLanguage
     * The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /**
     *
     * @return
     * The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     *
     * @param originalTitle
     * The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     *
     * @return
     * The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     *
     * @param overview
     * The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     *
     * @return
     * The popularity
     */
    public float getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The poster_path
     */
    public String getPosterPath() {
        return poster_path;
    }

    /**
     *
     * @param posterPath
     * The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.poster_path = posterPath;
    }

    /**
     *
     * @return
     * The productionCompanies
     */
    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    /**
     *
     * @param productionCompanies
     * The production_companies
     */
    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    /**
     *
     * @return
     * The productionCountries
     */
    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    /**
     *
     * @param productionCountries
     * The production_countries
     */
    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    /**
     *
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return release_date;
    }

    /**
     *
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    /**
     *
     * @return
     * The revenue
     */
    public int getRevenue() {
        return revenue;
    }

    /**
     *
     * @param revenue
     * The revenue
     */
    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    /**
     *
     * @return
     * The runtime
     */
    public int getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime
     * The runtime
     */
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return
     * The spokenLanguages
     */
    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    /**
     *
     * @param spokenLanguages
     * The spoken_languages
     */
    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The tagline
     */
    public String getTagline() {
        return tagline;
    }

    /**
     *
     * @param tagline
     * The tagline
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The video
     */
    public boolean isVideo() {
        return video;
    }

    /**
     *
     * @param video
     * The video
     */
    public void setVideo(boolean video) {
        this.video = video;
    }

    /**
     *
     * @return
     * The voteAverage
     */
    public float getVoteAverage() {
        return vote_average;
    }

    /**
     *
     * @param voteAverage
     * The vote_average
     */
    public void setVoteAverage(float voteAverage) {
        this.vote_average = voteAverage;
    }

    /**
     *
     * @return
     * The voteCount
     */
    public int getVoteCount() {
        return vote_count;
    }

    /**
     *
     * @param voteCount
     * The vote_count
     */
    public void setVoteCount(int voteCount) {
        this.vote_count = voteCount;
    }

    /**
     *
     * @return
     * The videos
     */
    public Videos getVideos() {
        return videos;
    }

    /**
     *
     * @param videos
     * The videos
     */
    public void setVideos(Videos videos) {
        this.videos = videos;
    }

    /**
     *
     * @return
     * The reviews
     */
    public Reviews getReviews() {
        return reviews;
    }

    /**
     *
     * @param reviews
     * The reviews
     */
    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    /**
     *
     * @return
     * The releases
     */
    public Releases getReleases() {
        return releases;
    }

    /**
     *
     * @param releases
     * The releases
     */
    public void setReleases(Releases releases) {
        this.releases = releases;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}