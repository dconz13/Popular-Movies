package app.com.connolly.dillon.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.MovieDataStructure;
import app.com.connolly.dillon.popularmovies.R;

/**
 * Created by Dillon Connolly on 8/20/2015.
 *
 *  A similar GridViewAdapter implementation was found on stack overflow as a
 * combined effort of users: user2029585 and James Baxter
 * URL: http://stackoverflow.com/questions/25651867/populate-gridview-from-array
 *
 * My code is a combination of referencing how they implemented a custom adapter and referencing the
 * documentation for the ArrayAdapter class.
 */
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
