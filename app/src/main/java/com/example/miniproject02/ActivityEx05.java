package com.example.miniproject02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miniproject02.LocalDb.QuoteHelper;
import com.example.miniproject02.LocalDb.SettingsHelper;
import com.example.miniproject02.adapters.SpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ActivityEx05 extends AppCompatActivity {
    TextView tv_quoteId, tv_quote, tv_quoteAth;
    ToggleButton tbtn_pinned;
    Spinner sp_colors;
    SharedPreferences sharedPreferences, sharedPreferencesColors;
    String URL;
    ImageView iv_infav, iv_listFavorite;
    boolean isFav = false;
    Button btn_exit;

    QuoteHelper db_fav_quote;
    SettingsHelper db_settings;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex05);

        URL = String.format("https://dummyjson.com/quotes/%d", new Random().nextInt(80 - 25 + 1) + 25);

        tv_quoteId = findViewById(R.id.tv_quoteId);
        tv_quote = findViewById(R.id.tv_quote);
        tv_quoteAth = findViewById(R.id.tv_quoteAth);
        tbtn_pinned = findViewById(R.id.tbtn_pinned);
        sp_colors = findViewById(R.id.sp_colors);
        iv_infav = findViewById(R.id.infav);
        btn_exit = findViewById(R.id.btn_exit);
        db_fav_quote = new QuoteHelper(this);
        iv_listFavorite = findViewById(R.id.iv_listfavorite);
        db_settings = new SettingsHelper(this);

        //region spinner
        ArrayList<String> colorsName = new ArrayList<>();
        ArrayList<String> colorsCode = new ArrayList<>();

        for (int i = 0; i < db_settings.getColors().size(); i++) {
            colorsName.add(db_settings.getColors().get(i).getName());
            colorsCode.add(db_settings.getColors().get(i).getCode());
        }

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this,colorsName,colorsCode);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_colors.setAdapter(spinnerAdapter);

        String backgroundColorName = db_settings.getBackgroundName();
        Toast.makeText(this, ""+backgroundColorName, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < colorsName.size(); i++) {
            if (colorsName.get(i).equals(backgroundColorName)) {
                sp_colors.setSelection(i);
                break;
            }
        }

        sp_colors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColorName = parent.getItemAtPosition(position).toString();

                db_settings.updateBackgroundName(selectedColorName);

                String selectedColorCode = db_settings.getBackgroundColor(); // Retrieve the color code from the database

                // Set the background color dynamically
                getWindow().getDecorView().setBackgroundColor(Color.parseColor(selectedColorCode));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //endregion

        //region pin/Unpin
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
        isFav = isIt(tv_quoteId.getText().toString());
        tbtn_pinned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String id1 = null;
            String quote = null;
            String author = null;
            if (isChecked) {
                id1 = tv_quoteId.getText().toString();
                quote = tv_quote.getText().toString();
                author = tv_quoteAth.getText().toString();

                if (!isFav) {
                    db_fav_quote.add(id1, quote, author);
                    iv_infav.setImageResource(R.drawable.fav);
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    isFav = !isFav;
                }
            } else {
                getJsonQuote();
            }

            editor.putString("id", id1);
            editor.putString("quote", quote);
            editor.putString("author", author);
            editor.apply();

        });
        //endregion

        iv_infav.setOnClickListener(v -> {
            if (isFav) {
                db_fav_quote.remove(tv_quoteId.getText().toString());
                iv_infav.setImageResource(R.drawable.infav);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                tbtn_pinned.setChecked(false);
                getJsonQuote();
            } else {
                db_fav_quote.add(tv_quoteId.getText().toString(), tv_quote.getText().toString(), tv_quoteAth.getText().toString());
                iv_infav.setImageResource(R.drawable.fav);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            isFav = !isFav;
        });

        iv_listFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityEx05.this, ListFavoriteQuote.class);
            startActivity(intent);
        });

        btn_exit.setOnClickListener(v -> finish());
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
                            tv_quoteId.setText(response.getString("id"));
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

    // To check if Quote is favorite or not
    public boolean isIt(String id) {
        boolean flag = false;
        for (int i = 0; i < db_fav_quote.getQuotes().size(); i++) {
            if (Objects.equals(db_fav_quote.getQuotes().get(i).getId(), id)) {
                iv_infav.setImageResource(R.drawable.fav);
                flag = true;
                break;
            }
        }
        return flag;

    }
}