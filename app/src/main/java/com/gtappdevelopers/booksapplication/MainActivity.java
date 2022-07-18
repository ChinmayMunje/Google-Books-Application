package com.gtappdevelopers.booksapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<BookRVModal> bookInfoArrayList;
    private ProgressBar progressBar;
    private EditText searchEdt;
    private ImageView searchIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.idPBLoading);
        searchEdt = findViewById(R.id.idEdtSearch);
        searchIV = findViewById(R.id.idIVSearch);

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Please enter search query");
                    return;
                }
                getBooksInfo(searchEdt.getText().toString());
            }
        });
    }


    private void getBooksInfo(String query) {
        bookInfoArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.getCache().clear();
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        //below line is use to make json object request inside that we are passing url, get method and getting json object. .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                //inside on response method we are extracting all our json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");

                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String subtitle = volumeObj.optString("subtitle");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = imageLinks.optString("thumbnail");
                        String previewLink = volumeObj.optString("previewLink");
                        String infoLink = volumeObj.optString("infoLink");
                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                        String buyLink = saleInfoObj.optString("buyLink");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        Log.e("TAG", "AUthor arra" + authorsArray);
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        } else {
                            authorsArrayList.add("Author Not available");
                        }
                        BookRVModal bookRVModal = new BookRVModal(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                        bookInfoArrayList.add(bookRVModal);
                        BookRVAdapter bookRVAdapter = new BookRVAdapter(bookInfoArrayList, MainActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                        RecyclerView mRecyclerView = findViewById(R.id.idRVBooks);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(bookRVAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
}