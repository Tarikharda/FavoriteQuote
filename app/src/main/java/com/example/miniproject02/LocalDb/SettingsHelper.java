package com.example.miniproject02.LocalDb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.miniproject02.Color;

import java.util.ArrayList;

public class SettingsHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "db_settings";

    private static final String SQL_CREATE_COLOR_TABLE = String.format(
            "CREATE TABLE %s (%s TEXT, %s TEXT, FOREIGN KEY (%s) REFERENCES %s(%s))",
            SettingsModel.colorModel.TABLE_NAME,
            SettingsModel.colorModel.COLUMN_NAME,
            SettingsModel.colorModel.COLUMN_CODE,
            SettingsModel.colorModel.COLUMN_NAME,
            SettingsModel.InnerSettingsModel.TABLE_NAME,
            SettingsModel.InnerSettingsModel.COLUMN_VALUE
    );

    private static final String SQL_CREATE_SETTINGS_TABLE = String.format(
            "CREATE TABLE %s (%s TEXT, %s TEXT)",
            SettingsModel.InnerSettingsModel.TABLE_NAME,
            SettingsModel.InnerSettingsModel.COLUMN_NAME,
            SettingsModel.InnerSettingsModel.COLUMN_VALUE
    );

    private static final String SQL_DROP_COLOR_TABLE = String.format(
            "DROP TABLE IF EXISTS %s",
            SettingsModel.colorModel.TABLE_NAME
    );

    private static final String SQL_DROP_SETTINGS_TABLE = String.format(
            "DROP TABLE IF EXISTS %s",
            SettingsModel.InnerSettingsModel.TABLE_NAME
    );

    private static final String SQL_INSERT_STATIC_COLOR = String.format(
            "INSERT INTO %s (%s,%s) VALUES " +
                    "('Default', '#FFFFFFFF')," +
                    "('LightSalmon', '#FFA07A')," +
                    "('Plum', '#DDA0DD')," +
                    "('PaleGreen', '#98FB98')," +
                    "('CornflowerBlue', '#6495ED');",
            SettingsModel.colorModel.TABLE_NAME,
            SettingsModel.colorModel.COLUMN_NAME,
            SettingsModel.colorModel.COLUMN_CODE
    );

    private static final String SQL_INSERT_DEFAULT_COLOR = String.format(
            "INSERT INTO %s (%s,%s) VALUES ('bg_color','Default')",
            SettingsModel.InnerSettingsModel.TABLE_NAME,
            SettingsModel.InnerSettingsModel.COLUMN_NAME,
            SettingsModel.InnerSettingsModel.COLUMN_VALUE
    );

    public SettingsHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COLOR_TABLE);
        db.execSQL(SQL_CREATE_SETTINGS_TABLE);
        db.execSQL(SQL_INSERT_STATIC_COLOR);
        db.execSQL(SQL_INSERT_DEFAULT_COLOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_COLOR_TABLE);
        db.execSQL(SQL_DROP_SETTINGS_TABLE);
        onCreate(db);
    }

    public ArrayList<Color> getColors() {
        ArrayList<Color> AList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SettingsModel.colorModel.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(SettingsModel.colorModel.COLUMN_NAME));
                @SuppressLint("Range") String code = cursor.getString(cursor.getColumnIndex(SettingsModel.colorModel.COLUMN_CODE));
                AList.add(new Color(name, code));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return AList;
    }

    @SuppressLint("Range")
    public String getBackgroundName() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = String.format(
                "SELECT %s FROM %s WHERE %s = 'bg_color'",
                SettingsModel.InnerSettingsModel.COLUMN_VALUE,
                SettingsModel.InnerSettingsModel.TABLE_NAME,
                SettingsModel.InnerSettingsModel.COLUMN_NAME
        );

        Cursor cursor = db.rawQuery(query, null);
        String backgroundName = null;
        if (cursor.moveToFirst()) {
            backgroundName = cursor.getString(cursor.getColumnIndex(SettingsModel.InnerSettingsModel.COLUMN_VALUE));
        }
        cursor.close();
        return backgroundName;
    }

    @SuppressLint("Range")
    public String getBackgroundColor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format(
                "SELECT %s FROM %s WHERE %s == '%s'",
                SettingsModel.colorModel.COLUMN_CODE,
                SettingsModel.colorModel.TABLE_NAME,
                SettingsModel.colorModel.COLUMN_NAME,
                getBackgroundName()
        );

        Cursor cursor = db.rawQuery(query, null);
        String backgroundColor = null;
        if (cursor.moveToFirst()) {
            backgroundColor = cursor.getString(cursor.getColumnIndex(SettingsModel.colorModel.COLUMN_CODE));
        }
        cursor.close();
        return backgroundColor;
    }

    public void updateBackgroundName(String colorName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(SettingsModel.InnerSettingsModel.COLUMN_VALUE, colorName);

        db.update(SettingsModel.InnerSettingsModel.TABLE_NAME, newValues, SettingsModel.InnerSettingsModel.COLUMN_NAME+"='bg_color'", null);
    }

}
