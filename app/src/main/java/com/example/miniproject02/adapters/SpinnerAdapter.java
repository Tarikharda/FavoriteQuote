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
    private ArrayList<String> colorsName;
    private ArrayList<String> colorsCode;

    public SpinnerAdapter(Context context, ArrayList<String> colorsName, ArrayList<String> colorsCode) {
        super(context, 0, colorsName);
        this.colorsName = colorsName;
        this.colorsCode = colorsCode;
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

        TextView tv_colorName = convertView.findViewById(R.id.tv_colorName);
        TextView tv_colorCode = convertView.findViewById(R.id.tv_colorCode);

        tv_colorName.setText(colorsName.get(position));
        tv_colorCode.setText(colorsCode.get(position));

        String hexCode = tv_colorCode.getText().toString();
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable backgroundDrawable = parent.getContext().getResources().getDrawable(R.drawable.hex_color_background);
        backgroundDrawable.setColorFilter(Color.parseColor(hexCode), PorterDuff.Mode.SRC_IN);
        tv_colorCode.setBackground(backgroundDrawable);


        return convertView;
    }
}
