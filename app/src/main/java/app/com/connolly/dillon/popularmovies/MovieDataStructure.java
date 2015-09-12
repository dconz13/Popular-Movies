//package app.com.connolly.dillon.popularmovies;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * Created by Dillon Connolly on 7/10/2015.
// *
// * Custom data structure that combines all the movie information for a given movie
// * into a single object and makes it parcelable. This allows for transport via Intents.
// * My research showed that Parcelable objects are about 10x faster than Serializable objects which
// * is why I went with the Parcelable option.
// */
//
//public class MovieDataStructure implements Parcelable {
//    private String title;
//    private String plotSummary;
//    private String averageVote;
//    private String releaseDate;
//    private String posterUrl;
//    private int id;
//    private String[] trailers;
//    private String[] trailerKey;
//    private String[] reviews;
//    private String[] reviewAuthor;
//    private String rating;
//
//    MovieDataStructure(String title,String plotSummary, String averageVote, String releaseDate, String posterUrl, int id){
//        this.title = title;
//        this.plotSummary = plotSummary;
//        this.averageVote = averageVote;
//        this.releaseDate = releaseDate;
//        this.posterUrl = posterUrl;
//        this.id = id;
//        this.trailers = null;
//        this.trailerKey = null;
//        this.reviews = null;
//        this.reviewAuthor = null;
//        this.rating = null;
//    }
//    //getters
//    public String getTitle(){return title;}
//    public String getPlotSummary(){return plotSummary;}
//    public String getAverageVote(){return averageVote;}
//    public String getReleaseDate(){return releaseDate;}
//    public String getPosterUrl(){return posterUrl;}
//    public int getId(){return id;}
//    public String[] getTrailers(){return trailers;}
//    public String[] getReviews(){return reviews;}
//    public String[] getTrailerKey(){return trailerKey;}
//    public String[] getReviewAuthor(){return reviewAuthor;}
//    public String getRating(){return rating;}
//    //setters
//    public void setTitle(String title){this.title = title;}
//    public void setPlotSummary(String plotSummary){this.plotSummary = plotSummary;}
//    public void setAverageVote(String averageVote){this.averageVote = averageVote;}
//    public void setReleaseDate(String releaseDate){this.releaseDate = releaseDate;}
//    public void setPosterUrl(String posterUrl){this.posterUrl = posterUrl;}
//    public void setId(int id){this.id = id;}
//    public void setTrailers(String[] trailers){this.trailers = trailers;}
//    public void setReviews(String[] reviews){this.reviews = reviews;}
//    public void setTrailerKey(String[] trailerKey){this.trailerKey = trailerKey;}
//    public void setReviewAuthor(String[] reviewAuthor){this.reviewAuthor = reviewAuthor;}
//    public void setRating(String rating){this.rating = rating;}
//
//    // Parcelable methods:
//    @Override
//    public int describeContents(){
//        return 0;
//    }
//    @Override
//    public void writeToParcel(Parcel out, int flags){
//        out.writeString(title);
//        out.writeString(plotSummary);
//        out.writeString(averageVote);
//        out.writeString(releaseDate);
//        out.writeString(posterUrl);
//        out.writeInt(id);
//        out.writeStringArray(trailers);
//        out.writeStringArray(trailerKey);
//        out.writeStringArray(reviews);
//        out.writeStringArray(reviewAuthor);
//        out.writeString(rating);
//    }
//
//    public static final Parcelable.Creator CREATOR
//            = new Parcelable.Creator(){
//
//        public MovieDataStructure createFromParcel(Parcel in) {
//            return new MovieDataStructure(in);
//        }
//        public MovieDataStructure[] newArray(int size) {
//            return new MovieDataStructure[size];
//        }
//    };
//    private MovieDataStructure(Parcel in){
//        this.title = in.readString();
//        this.plotSummary = in.readString();
//        this.averageVote = in.readString();
//        this.releaseDate = in.readString();
//        this.posterUrl = in.readString();
//        this.id = in.readInt();
//        this.trailers = in.createStringArray();
//        this.trailerKey = in.createStringArray();
//        this.reviews = in.createStringArray();
//        this.reviewAuthor = in.createStringArray();
//        this.rating = in.readString();
//    }
//
//}