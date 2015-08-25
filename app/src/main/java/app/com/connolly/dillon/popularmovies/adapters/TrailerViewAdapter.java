package app.com.connolly.dillon.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.MovieDataStructure;
import app.com.connolly.dillon.popularmovies.R;
import app.com.connolly.dillon.popularmovies.DetailFragment.Trailer;

/**
 * Created by Dillon Connolly on 8/23/2015.
 */
public class TrailerViewAdapter extends ArrayAdapter<Trailer> {
    Context mContext;
    private ArrayList<MovieDataStructure> mObjects;
    private int mLayoutId;
    private int mResource;
    private ArrayList<Trailer> mTrailer;

    public TrailerViewAdapter(Context context,int layout, int id, ArrayList<Trailer> trailer){
        super(context,layout,id,trailer);
        this.mContext = context;
        this.mLayoutId = layout;
        this.mResource = id;
        this.mTrailer = trailer;
    }

    @Override
    public int getCount() {
        return mTrailer.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if(convertView == null) {
//            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            linearLayout = inflater.inflate(mResource, parent, false);
            convertView = LayoutInflater.from(mContext).inflate(mResource,parent,false);
        }

        // this code must be outside the if statements so images don't repeat.
        TextView trailerName = (TextView) convertView.findViewById(R.id.textView_trailer);
        trailerName.setText(trailer.name);
        return convertView;
    }
}
