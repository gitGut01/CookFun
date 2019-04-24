package com.example.recipesapp.rv_adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipesapp.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PrevRecipeAdapter extends RecyclerView.Adapter<PrevRecipeAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> imgUrls;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public PrevRecipeAdapter(Context context, List<String> data, List<String> imgUrls) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.imgUrls = imgUrls;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_prev_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mData != null){
            String b = mData.get(position);
            holder.tvTitle.setText(b);
            new ImageDownloadTask(holder.imgDish).execute(imgUrls.get(position));
        }else{
            holder.imgDish.setImageResource(R.drawable.nopicture);
            holder.tvTitle.setText("Loading...");
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        ImageView imgDish;

        ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgDish = itemView.findViewById(R.id.imgDish);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



    //ASYNC task the pictrue download from the URL
    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imgView;

        public ImageDownloadTask(ImageView imgView) {
            //imgView.setImageBitmap(BitmapFactory.decodeResource(, R.drawable.nopicture));
            //int id = 2131099758;
            //imgView.setImageResource(id);
            this.imgView = imgView;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            try {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 4; // 1 = 100% if you write 4 means 1/4 = 25%
                mIcon11 = BitmapFactory.decodeStream((InputStream)new URL(urls[0]).getContent(), null, bmOptions);
            } catch (Exception e) {

                Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }


        protected void onPostExecute(Bitmap result) {
            imgView.setImageBitmap(result);
        }
    }
}
