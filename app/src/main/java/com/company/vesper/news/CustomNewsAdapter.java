package com.company.vesper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.vesper.services.StockNews;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CustomNewsAdapter extends RecyclerView.Adapter<CustomNewsAdapter.ViewHolder> {

    private List<StockNews.NewsItem> listNews;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView1;
        public final TextView textView2;
        public final TextView textView3;
        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NEWS TEST", "Element " + getAdapterPosition() + " clicked.");
                }
            });

            textView1 = (TextView) v.findViewById(R.id.textTitle);
            textView2 = (TextView) v.findViewById(R.id.textBody);
            textView3 = (TextView) v.findViewById(R.id.textSource);
            imageView = (ImageView) v.findViewById(R.id.newsImageView);
        }

        public TextView getTitleTextView() {
            return textView1;
        }

        public TextView getBodyTextView() {
            return textView2;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getSourceTextView(){
            return textView3;
        }

    }

    public CustomNewsAdapter(List<StockNews.NewsItem> listNews){
        this.listNews = listNews;
    }



    @NonNull
    @Override
    public CustomNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomNewsAdapter.ViewHolder holder, int position) {
        Log.d("NEWS TAG", "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        StockNews.NewsItem i = listNews.get(position);

        holder.getTitleTextView().setText(i.title);

        if(i.sentiment){
            holder.getTitleTextView().setTextColor(Color.parseColor("#1A9931"));
        }else{
            holder.getTitleTextView().setTextColor(Color.parseColor("#C73822"));

        }
        holder.getBodyTextView().setText(i.bodyText);
        holder.getSourceTextView().setText(i.sourceName);

        //Load news image into ImageView
        Picasso.get().load(i.pictureUrl).into(holder.getImageView());

    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

}