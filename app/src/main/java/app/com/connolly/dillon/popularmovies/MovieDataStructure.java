package app.com.connolly.dillon.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dillon Connolly on 7/10/2015.
 *
 * Custom data structure that combines all the movie information for a given movie
 * into a single object and makes it parcelable. This allows for transport via Intents.
 * My research showed that Parcelable objects are about 10x faster than Serializable objects which
 * is why I went with the Parcelable option.
 */

public class MovieDataStructure implements Parcelable {
    private String title;
    private String plotSummary;
    private String averageVote;
    private String releaseDate;
    private String posterUrl;

    MovieDataStructure(String title,String plotSummary, String averageVote, String releaseDate, String posterUrl){
        this.title = title;
        this.plotSummary = plotSummary;
        this.averageVote = averageVote;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
    }
    //getters
    public String getTitle(){return title;}
    public String getPlotSummary(){return plotSummary;}
    public String getAverageVote(){return averageVote;}
    public String getReleaseDate(){return releaseDate;}
    public String getPosterUrl(){return posterUrl;}
    //setters
    public void setTitle(String title){this.title = title;}
    public void setPlotSummary(String plotSummary){this.plotSummary = plotSummary;}
    public void setAverageVote(String averageVote){this.averageVote = averageVote;}
    public void setReleaseDate(String releaseDate){this.releaseDate = releaseDate;}
    public void setPosterUrl(String posterUrl){this.posterUrl = posterUrl;}

    // Parcelable methods:
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(title);
        out.writeString(plotSummary);
        out.writeString(averageVote);
        out.writeString(releaseDate);
        out.writeString(posterUrl);
    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator(){

        public MovieDataStructure createFromParcel(Parcel in) {
            return new MovieDataStructure(in);
        }
        public MovieDataStructure[] newArray(int size) {
            return new MovieDataStructure[size];
        }
    };
    private MovieDataStructure(Parcel in){
        this.title = in.readString();
        this.plotSummary = in.readString();
        this.averageVote = in.readString();
        this.releaseDate = in.readString();
        this.posterUrl = in.readString();
    }

}