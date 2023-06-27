package com.example.miniproject02.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miniproject02.LocalDb.QuoteHelper;
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

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.favorite_item, null);
        TextView id = convertView.findViewById(R.id.tv_iditemquote);
        TextView quote = convertView.findViewById(R.id.tv_itemquote);
        TextView author = convertView.findViewById(R.id.tv_itemauthor);
        ImageView btn_delete = convertView.findViewById(R.id.iv_deleteitemfavorite);
        db = new QuoteHelper(context);
        Quote q = quotes.get(position);
        id.setText("#"+q.getId());
        quote.setText(spannableQuote(q.getQuote()));
        author.setText(spannableAuthor(q.getAuthor()));

        btn_delete.setOnClickListener(v -> {
            db.remove(q.getId());
            quotes.remove(q);
            notifyDataSetChanged();
        });

        return convertView;
    }

    private SpannableString spannableQuote(String quote) {
        SpannableString spannableString = new SpannableString(quote);
        spannableString.setSpan(new RelativeSizeSpan(2), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SpannableString spannableAuthor(String author) {
        SpannableString spannableString = new SpannableString(author);
        spannableString.setSpan(new UnderlineSpan(), 0, author.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}

