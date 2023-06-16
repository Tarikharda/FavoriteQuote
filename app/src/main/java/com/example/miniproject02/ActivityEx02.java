package com.example.miniproject02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ActivityEx02 extends AppCompatActivity {
    TextView tv_quoteId, tv_quote, tv_quoteAth;
    ToggleButton tbtn_pinned;
    SharedPreferences sharedPreferences;
    @SuppressLint("DefaultLocale")
    String URL = String.format("https://dummyjson.com/quotes/%d", new Random().nextInt(80 - 25 + 1) + 25);

    @SuppressLint({"DefaultLocale", "MissingInflatedId"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex02);
        tv_quoteId = findViewById(R.id.tv_quoteId);
        tv_quote = findViewById(R.id.tv_quote);
        tv_quoteAth = findViewById(R.id.tv_quoteAth);
        tbtn_pinned = findViewById(R.id.tbtn_pinned);
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
                }else{
                    getJsonQuote();
                }
                editor.putString("id", id);
                editor.putString("quote", quote);
                editor.putString("author", author);

                editor.commit();
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