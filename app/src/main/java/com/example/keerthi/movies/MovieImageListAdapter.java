package com.example.keerthi.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Selvanayagam on 2/28/2016.
 */
public class MovieImageListAdapter extends RecyclerView.Adapter<MovieImageListAdapter.ImageHolder> {

    private static final String TAG = "MovieImageListAdapter";
    public static final String IMAGE_URL = "C.Android.Movies.image_position";
    private List<String> mImageUrls;
    private LayoutInflater mLayoutInflater;

    private Context mContext;

    public MovieImageListAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setImageUrls(List<String> imageUrls) {
        mImageUrls = imageUrls;
        notifyDataSetChanged();
    }

    class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;

        public ImageHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView)itemView.findViewById(R.id.movieImage);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(mContext,"The Item Clicked is: "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ImageActivity.class);
            intent.putExtra(IMAGE_URL, mImageUrls.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.movie_image_list_item, parent, false);
        ImageHolder imageHolder = new ImageHolder(view);
        return imageHolder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        String imageUrl = MovieApiWrapper.getInstance().getImageBaseUrl() + "w300" + mImageUrls.get(position);
        Picasso.with(mContext)
                .load(imageUrl)
                .into(holder.mImageView);
        Log.i(TAG, MovieConfiguration.getInstance().getImageBaseUrl() + "w300" + imageUrl);

    }

    @Override
    public int getItemCount() {
        if (mImageUrls == null){
            return 0;
        }
        return mImageUrls.size();
    }
}
