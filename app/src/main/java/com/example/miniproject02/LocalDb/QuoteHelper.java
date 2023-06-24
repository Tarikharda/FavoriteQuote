package com.example.miniproject02.LocalDb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.miniproject02.Quote;

import java.util.ArrayList;

public class QuoteHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "db_quote";

    private static final String SQL_CREATE_FAV_TABLE = String.format(
            "CREATE TABLE %s (%s INTEGER NOT NULL PRIMARY KEY , %s TEXT , %s TEXT )",
            FavQuoteModel.TABLE_NAME,
            FavQuoteModel.COLUMN_ID,
            FavQuoteModel.COLUMN_QUOTE,
            FavQuoteModel.COLUMN_AUTHOR
    );

    private static final String SQL_DROP_FAV_TABLE = String.format(
            "DROP TABLE %s",
            FavQuoteModel.TABLE_NAME
    );

    public QuoteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_FAV_TABLE);
        onCreate(db);
    }

    public void add(String id, String quote, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(FavQuoteModel.COLUMN_ID, id);
        insertValues.put(FavQuoteModel.COLUMN_QUOTE, quote);
        insertValues.put(FavQuoteModel.COLUMN_AUTHOR, author);
        db.insert(FavQuoteModel.TABLE_NAME, null, insertValues);
    }

    public void remove(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(String.format(
                "DELETE FROM %s WHERE %s == %s",
                FavQuoteModel.TABLE_NAME,
                FavQuoteModel.COLUMN_ID,
                id
        ));

    }

    public long FavCounter() {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, FavQuoteModel.TABLE_NAME);
    }

    public ArrayList<Quote> getQuotes() {
        ArrayList<Quote> QuoteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FavQuoteModel.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(FavQuoteModel.COLUMN_ID));
                @SuppressLint("Range") String quote = cursor.getString(cursor.getColumnIndex(FavQuoteModel.COLUMN_QUOTE));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(FavQuoteModel.COLUMN_AUTHOR));
                Quote q = new Quote(id, quote, author);
                QuoteList.add(q);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return QuoteList;
    }

}
