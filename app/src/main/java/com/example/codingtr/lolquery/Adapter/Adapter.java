package com.example.codingtr.lolquery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codingtr.lolquery.Activity.News_Activity;
import com.example.codingtr.lolquery.Management.News;
import com.example.codingtr.lolquery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;


public class Adapter extends RecyclerView.Adapter<Adapter.NewsViewHolder> {

    private Context mCtx;
    private List<News> newsList;

    public Adapter(Context mCtx, List<News> newsList) {
        this.mCtx = mCtx;
        this.newsList = newsList;

    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);

        return new NewsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        News news = newsList.get(position);

        newsViewHolder.textViewTitle.setText(news.getTitle());
        newsViewHolder.textViewCategory.setText(news.getCategory());
        newsViewHolder.textViewAuthor.setText(news.getAuthor());
        final String url = news.getLink();
        final String photo = "http://lolquery.codingtr.com/img/"+news.getImage();


        newsViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mCtx, "Tıklandı", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mCtx, News_Activity.class);
                intent.putExtra("send_url", url);
                mCtx.startActivity(intent);
            }
        });

        Picasso.get()
                .load(photo)
                .transform(new BlurTransformation(mCtx))
                .into(newsViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder  {

        private ImageView imageView;
        private TextView textViewTitle, textViewCategory, textViewAuthor;

        public NewsViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listLayoutImageView);
            textViewTitle = itemView.findViewById(R.id.listLayoutTextViewBaslik);
            textViewCategory = itemView.findViewById(R.id.listLayoutTextViewKategori);
            textViewAuthor = itemView.findViewById(R.id.listLayoutTextViewAuthor);
        }


    }


}
