package com.example.miniproject02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ActivityEx02 extends AppCompatActivity {
    TextView tv_quoteId, tv_quote, tv_quoteAth;
    ToggleButton tbtn_pinned;
    Spinner sp_colors;
    SharedPreferences sharedPreferences, sharedPreferencesColors;
    @SuppressLint("DefaultLocale")
    String URL;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex02);

        URL = String.format("https://dummyjson.com/quotes/%d", new Random().nextInt(80 - 25 + 1) + 25);

        tv_quoteId = findViewById(R.id.tv_quoteId);
        tv_quote = findViewById(R.id.tv_quote);
        tv_quoteAth = findViewById(R.id.tv_quoteAth);
        tbtn_pinned = findViewById(R.id.tbtn_pinned);
        sp_colors = findViewById(R.id.sp_colors);

        //region spinner
        sharedPreferencesColors = getSharedPreferences("saved-color", MODE_PRIVATE);

        ArrayList<String> colorList = new ArrayList<>(Arrays.asList(
                "Default",
                "LightSalmon",
                "Plum",
                "PaleGreen",
                "CornflowerBlue"
        ));

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                colorList
        );

        sp_colors.setAdapter(colorAdapter);

        int defaultColor = R.color.white;
        String defaultColorName = "Default";

        int savedColor = sharedPreferencesColors.getInt(defaultColorName, 0);
        if (savedColor != 0) {
            defaultColor = savedColor;
            defaultColorName = sharedPreferencesColors.getString("name", null);
        }
        getWindow().getDecorView().setBackgroundColor(getColor(defaultColor));

        for (int i = 0; i < colorList.size(); i++) {
           int colorName = sharedPreferencesColors.getInt(colorList.get(i), 0);
            if (colorName != 0 && colorList.get(i).equals(defaultColorName)) {
                sp_colors.setSelection(i);
                break;
            }
        }

        //endregion

        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if (id == null) {
            getJsonQuote();
        } else {
            String quote = sharedPreferences.getString("quote", null);
            String author = sharedPreferences.getString("author", null);
            tv_quoteId.setText(id);
            tv_quote.setText(quote);
            tv_quoteAth.setText(author);
            tbtn_pinned.setChecked(true);
        }

        sp_colors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferencesColors.edit();
                int colorValue;
                String colorName;
                switch (position) {
                    case 0:
                        colorValue = R.color.white;
                        colorName = "Default";
                        break;
                    case 1:
                        colorValue = R.color.LightSalmon;
                        colorName = "LightSalmon";
                        break;
                    case 2:
                        colorValue = R.color.Plum;
                        colorName = "Plum";
                        break;
                    case 3:
                        colorValue = R.color.PaleGreen;
                        colorName = "PaleGreen";
                        break;
                    case 4:
                        colorValue = R.color.CornflowerBlue;
                        colorName = "CornflowerBlue";
                        break;
                    default:
                        return;
                }

                editor.putInt(colorName, colorValue);
                editor.putString("name", colorName);
                editor.apply();

                getWindow().getDecorView().setBackgroundColor(getColor(colorValue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tbtn_pinned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String id = null;
                String quote = null;
                String author = null;

                if (isChecked) {
                    id = tv_quoteId.getText().toString();
                    quote = tv_quote.getText().toString();
                    author = tv_quoteAth.getText().toString();
                } else {
                    getJsonQuote();
                }

                editor.putString("id", id);
                editor.putString("quote", quote);
                editor.putString("author", author);
                editor.apply();
            }
        });

    }

    public void getJsonQuote() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                URL,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tv_quoteId.setText("#" + response.getString("id"));
                            tv_quote.setText(response.getString("quote"));
                            tv_quoteAth.setText(response.getString("author"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        queue.add(jsonRequest);

    }

}