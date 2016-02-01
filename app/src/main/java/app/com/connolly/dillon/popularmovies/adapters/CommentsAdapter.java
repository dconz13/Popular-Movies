package app.com.connolly.dillon.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.connolly.dillon.popularmovies.R;

/**
 * Created by Dillon Connolly on 1/15/2016.
 */
public class CommentsAdapter extends ArrayAdapter<String> {

    private ArrayList<String> mCommentAuthors;
    private Context mContext;

    public CommentsAdapter(Context context, int resource, ArrayList<String> arrayList){
        super(context,resource,arrayList);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        String comment = getItem(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.author.setText(mCommentAuthors.get(position));
        viewHolder.comment.setText(comment);

        return convertView;
    }

    public void setAuthors(ArrayList<String> authors){
        this.mCommentAuthors = authors;
    }

    private static class ViewHolder {
        public TextView author;
        public TextView comment;

        public ViewHolder(View view){
            author = (TextView) view.findViewById(R.id.textView_username);
            comment = (TextView) view.findViewById(R.id.textView_review);
        }
    }
}
