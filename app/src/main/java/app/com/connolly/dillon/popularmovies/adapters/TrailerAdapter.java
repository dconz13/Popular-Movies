package app.com.connolly.dillon.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import app.com.connolly.dillon.popularmovies.R;


/**
 * Created by Dillon Connolly on 1/15/2016.
 */
public class TrailerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> mMovieTitles;

    public TrailerAdapter(Context context, int layoutFile, ArrayList<String> urlStrings){
        super(context, layoutFile, urlStrings);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        String youtubeUrl = getItem(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item_trailer, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
            Picasso.with(mContext)
                    .load(youtubeUrl)
                    .into(viewHolder.youtubeImg);
        viewHolder.youtubeTitle.setText(mMovieTitles.get(position));

        return convertView;
    }
    public void setTitles(ArrayList<String> titles){
        this.mMovieTitles = titles;
    }

    private static class ViewHolder {
        public ImageView youtubeImg;
        public TextView youtubeTitle;

        public ViewHolder(View view){
            youtubeImg = (ImageView) view.findViewById(R.id.detail_youtube_img_preview);
            youtubeTitle = (TextView) view.findViewById(R.id.detail_youtube_title);
        }
    }

}
