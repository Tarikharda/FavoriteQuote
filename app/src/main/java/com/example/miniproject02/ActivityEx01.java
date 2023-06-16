package com.example.miniproject02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ActivityEx01 extends AppCompatActivity {
    TextView tv_quoteId, tv_quote, tv_quoteAth;
    @SuppressLint("DefaultLocale")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex01);
        tv_quoteId = findViewById(R.id.tv_quoteId);
        tv_quote = findViewById(R.id.tv_quote);
        tv_quoteAth = findViewById(R.id.tv_quoteAth);
        String URL = String.format("https://dummyjson.com/quotes/%d",new Random().nextInt(80-25+1) + 25);
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