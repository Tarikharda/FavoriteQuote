package com.example.miniproject02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.miniproject02.adapters.ListFavoriteAdapter;
import com.example.miniproject02.LocalDb.QuoteHelper;

import java.util.ArrayList;

public class ListFavoriteQuote extends AppCompatActivity {
    Button btn_back;
    ListFavoriteAdapter adapter;
    ArrayList<Quote> qList;
    QuoteHelper db;
    ListView lv_listFavorite;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite_quote);
        btn_back = findViewById(R.id.btn_back);
        lv_listFavorite = findViewById(R.id.lv_listFavorite);
        db = new QuoteHelper(this);
        qList = db.getQuotes();
        adapter = new ListFavoriteAdapter(this, qList);
        lv_listFavorite.setAdapter(adapter);
        btn_back.setOnClickListener(v -> {
            finish();
        });
    }
}