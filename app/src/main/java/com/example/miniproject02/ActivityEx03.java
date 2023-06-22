package com.example.miniproject02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class ActivityEx03 extends AppCompatActivity {
    TextView tv_quoteId, tv_quote, tv_quoteAth;
    ToggleButton tbtn_pinned;
    Spinner sp_colors;
    SharedPreferences sharedPreferences, sharedPreferencesColors;
    String URL;
    ImageView iv_infav, iv_listFavorite;
    boolean isFav = false;
    Button btn_exit;
    QuoteHelper db;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex03);

        URL = String.format("https://dummyjson.com/quotes/%d", new Random().nextInt(80 - 25 + 1) + 25);

        tv_quoteId = findViewById(R.id.tv_quoteId);
        tv_quote = findViewById(R.id.tv_quote);
        tv_quoteAth = findViewById(R.id.tv_quoteAth);
        tbtn_pinned = findViewById(R.id.tbtn_pinned);
        sp_colors = findViewById(R.id.sp_colors);
        iv_infav = findViewById(R.id.infav);
        btn_exit = findViewById(R.id.btn_exit);
        db = new QuoteHelper(this);
        iv_listFavorite = findViewById(R.id.iv_listfavorite);


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
                    db.add(id1, quote, author);
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
                db.remove(tv_quoteId.getText().toString());
                iv_infav.setImageResource(R.drawable.infav);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                tbtn_pinned.setChecked(false);
                getJsonQuote();
            } else {
                db.add(tv_quoteId.getText().toString(), tv_quote.getText().toString(), tv_quoteAth.getText().toString());
                iv_infav.setImageResource(R.drawable.fav);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            isFav = !isFav;
        });


        btn_exit.setOnClickListener(v -> finish());
        iv_listFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityEx03.this, ListFavoriteQuote.class);
            startActivity(intent);
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
        for (int i = 0; i < db.getQuotes().size(); i++) {
            if (Objects.equals(db.getQuotes().get(i).getId(), id)) {
                iv_infav.setImageResource(R.drawable.fav);
                flag = true;
                break;
            }
        }
        return flag;

    }
}