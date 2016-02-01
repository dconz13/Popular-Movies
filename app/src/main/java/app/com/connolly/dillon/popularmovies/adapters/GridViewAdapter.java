package app.com.connolly.dillon.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.com.connolly.dillon.popularmovies.R;


/**
 * Created by Dillon Connolly on 9/10/2015.
 */
public class GridViewAdapter extends CursorAdapter {
    String LOG_TAG = GridViewAdapter.class.getSimpleName();
    Context mContext;

    public GridViewAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Picasso.with(mContext)
                .load(cursor.getString(6)) //For some reason using the COL_POSTER_URL here didn't work but setting it to 6 did?
                .error(R.drawable.no_poster)
                .into(viewHolder.poster);
        //Log.v(LOG_TAG, "Inside bindView. Movie: " + cursor.getString(MoviesFragment.COL_POSTER_URL));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;

        layoutId = R.layout.grid_item_movies;
        View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }
    public static class ViewHolder {
        public final ImageView poster;

        public ViewHolder(View view){
            poster = (ImageView) view.findViewById(R.id.grid_item_movies_imageView);
        }
    }
}
