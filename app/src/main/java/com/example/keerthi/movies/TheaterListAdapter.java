package com.example.keerthi.movies;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Selvanayagam on 4/22/2016.
 */
public class TheaterListAdapter extends RecyclerView.Adapter<TheaterListAdapter.TheaterHolder> {
    private Map<String, List<ShowTime>> mTheaters;
    private Context mContext;
    private List<String> mTheaterList;

    private ShowTimeListAdapter mShowTimeListAdapter;


    public TheaterListAdapter(Context context){
        mContext = context;
   }

    public void setTheaters(Map<String, List<ShowTime>> theaters) {
        if (theaters == null){
            return;
        }
        mTheaters = theaters;
        Set<String> theatersInfo = mTheaters.keySet();
        mTheaterList = new ArrayList<>();
        mTheaterList.addAll(theatersInfo);
        notifyDataSetChanged();
    }


    @Override
    public TheaterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.theater_list_item, parent, false);
        return new TheaterHolder(view);
    }

    @Override
    public void onBindViewHolder(TheaterHolder holder, int position) {
        holder.mTheaterTextView.setText(mTheaterList.get(position));
        List<ShowTime> showTime = mTheaters.get(mTheaterList.get(position));
        mShowTimeListAdapter = new ShowTimeListAdapter(mContext, showTime);
        holder.mShowTimeRecyclerView.setAdapter(mShowTimeListAdapter);
    }

    @Override
    public int getItemCount() {
        if (mTheaterList == null){
            return 0;
        }
        return mTheaterList.size();
    }

    class TheaterHolder extends RecyclerView.ViewHolder{

        public TextView mTheaterTextView;
        private RecyclerView mShowTimeRecyclerView;
        private Context mContext;

        public TheaterHolder(View itemView) {
            super(itemView);
            mTheaterTextView = (TextView)itemView.findViewById(R.id.theater_Name);

            mShowTimeRecyclerView = (RecyclerView)itemView.findViewById(R.id.showTimeList);
            mShowTimeRecyclerView.setHasFixedSize(true);
            LinearLayoutManager showTimeLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mShowTimeRecyclerView.setLayoutManager(showTimeLayoutManager);

        }
    }
}
