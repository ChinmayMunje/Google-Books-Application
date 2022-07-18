package com.gtappdevelopers.booksapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookRVAdapter extends RecyclerView.Adapter<BookRVAdapter.BookViewHolder> {

    private ArrayList<BookRVModal> bookRVModalArrayList;
    private Context context;

    public BookRVAdapter(ArrayList<BookRVModal> bookRVModalArrayList, Context context) {
        this.bookRVModalArrayList = bookRVModalArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public BookRVAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRVAdapter.BookViewHolder holder, int position) {
        BookRVModal bookInfo = bookRVModalArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());
        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BookDetailsActivity.class);
                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subtitle", bookInfo.getSubtitle());
                i.putExtra("authors", bookInfo.getAuthors());
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishedDate());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getPageCount());
                i.putExtra("thumbnail", bookInfo.getThumbnail());
                i.putExtra("previewLink", bookInfo.getPreviewLink());
                i.putExtra("infoLink", bookInfo.getInfoLink());
                i.putExtra("buyLink", bookInfo.getBuyLink());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookRVModalArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVPublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVBook);

        }
    }
}
