package com.example.miniproject02.LocalDb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miniproject02.Quote;
import com.example.miniproject02.R;

import java.util.ArrayList;

public class ListFavoriteAdapter extends BaseAdapter {
    Context context;
    ArrayList<Quote> quotes;
    LayoutInflater inflater;

    QuoteHelper db;

    public ListFavoriteAdapter(Context context, ArrayList<Quote> qList) {
        this.context = context;
        this.quotes = qList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.quotes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.quotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.favorite_item, null);
        TextView id = convertView.findViewById(R.id.tv_iditemquote);
        TextView quote = convertView.findViewById(R.id.tv_itemquote);
        TextView author = convertView.findViewById(R.id.tv_itemauthor);
        ImageView btn_delete = convertView.findViewById(R.id.iv_deleteitemfavorite);
        db = new QuoteHelper(context);
        Quote q = quotes.get(position);
        id.setText(q.getId());
        quote.setText(q.getQuote());
        author.setText(q.getAuthor());

        btn_delete.setOnClickListener(v -> {
            db.remove(q.getId());
            quotes.remove(q);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
