package com.example.keerthi.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Selvanayagam on 4/25/2016.
 */
public class ShowTimeListAdapter extends RecyclerView.Adapter<ShowTimeListAdapter.ShowTimeHolder> {
    private List<ShowTime> mShowTime;
    private Context mContext;
    private DateFormat mOriginalFormat;
    private DateFormat mTargetFormat;

    private static final String TAG = "ShowTimeListAdapter";

    public ShowTimeListAdapter(Context context, List<ShowTime> showTime){
        mShowTime = showTime;
        mContext = context;
        mOriginalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
        mTargetFormat = new SimpleDateFormat("h:mm a");
    }

    @Override
    public ShowTimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.show_time_list_item, parent, false);
        return new ShowTimeHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowTimeHolder holder, int position) {
        String formattedDate;
        try {
           Date date = mOriginalFormat.parse(mShowTime.get(position).getTime());
           formattedDate = mTargetFormat.format(date);
            holder.mShowTimeTextView.setText(formattedDate);
        } catch (ParseException ex){
           Log.e(TAG, ex.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return mShowTime.size();
    }

    class ShowTimeHolder extends RecyclerView.ViewHolder{

        public TextView mShowTimeTextView;

        public ShowTimeHolder(View itemView) {
            super(itemView);
            mShowTimeTextView = (TextView)itemView.findViewById(R.id.show_time);
        }
    }
}
