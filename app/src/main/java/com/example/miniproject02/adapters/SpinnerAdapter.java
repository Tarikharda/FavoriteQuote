package com.example.miniproject02.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.miniproject02.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private ArrayList<String> color_liste;
    private ArrayList<String> hex_liste;

    public SpinnerAdapter(Context context, ArrayList<String> color_liste, ArrayList<String> hex_liste) {
        super(context, 0, color_liste);
        this.color_liste = color_liste;
        this.hex_liste = hex_liste;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_spinner, parent, false);
        }

        TextView color_name_tv = convertView.findViewById(R.id.color_name_tv);
        TextView color_hex_tv = convertView.findViewById(R.id.color_hex_tv);

        color_name_tv.setText(color_liste.get(position));
        color_hex_tv.setText(hex_liste.get(position));

        String colorHex = color_hex_tv.getText().toString();

        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable backgroundDrawable = parent.getContext().getResources().getDrawable(R.drawable.card_widget);
        backgroundDrawable.setColorFilter(Color.parseColor(colorHex), PorterDuff.Mode.SRC_IN);

        color_hex_tv.setBackground(backgroundDrawable);
        color_hex_tv.setPadding(10,5,10,5);

        return convertView;
    }
}
