package com.example.menuwhiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//activity that displays the restaurant menu when an NFC tag is detected
public class RestaurantMenu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView restaurantName;
    private Button exit;
    private Button cart;

    private List<ListItem> listItems;

    private static String URL_RETRIEVE ="http://192.168.1.87:8000/RetrieveMenu/";
    private static String URL_RETRIEVE_REST ="http://192.168.1.87:8000/RetrieveRestaurant/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets the parent layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        restaurantName = findViewById(R.id.restaurantName);
        exit = findViewById(R.id.exit);
        cart = findViewById(R.id.cartIcon);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listItems = new ArrayList<>();

        //function to retrieve restaurant whose NFC tag is detected
        retrieveRestaurant();

        //function to populate the interface with item objects
        retrieveRestaurantMenu();

        //function to exit a restaurant
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitRestaurant();
            }
        });

        //button to view current items in cart
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantMenu.this, Cart.class);
                startActivity(intent);
            }
        });

    }

    //function that exits a restaurant by removing the restaurant session
    private void exitRestaurant() {
        SessionManagement sessionManagement = new SessionManagement(RestaurantMenu.this);
        sessionManagement.removeSessionRestaurant();

        Intent intent= new Intent(RestaurantMenu.this, MainMenu.class);
        startActivity(intent);
        finish();
    }

    //function that retrieves the restaurant. Takes NFC id as the parameter, backend returns the restaurant record that matches. Restaurant name set on top of layout.
    private void retrieveRestaurant() {

        Intent intent = getIntent();
        final String nfcID = intent.getStringExtra("nfc");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RETRIEVE_REST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String id = jsonObject.getString("restaurant_id");
                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    String address = jsonObject.getString("address");
                    String phone = jsonObject.getString("phone");

                    Restaurant restaurant = new Restaurant(id,name,email,phone,address, nfcID);

                    SessionManagement sessionManagement = new SessionManagement(RestaurantMenu.this);
                    sessionManagement.saveSessionRestaurant(restaurant);

                    restaurantName.setText(name);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RestaurantMenu.this,"Restaurant not found!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantMenu.this,"Connectivity error!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nfc", nfcID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //function that retrieves the items of the restaurant detected, takes nfc tag as the parameter
    private void retrieveRestaurantMenu() {


        final ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(progressBar.VISIBLE);

        Intent intent = getIntent();
        final String nfcID = intent.getStringExtra("nfc");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RETRIEVE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(progressBar.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("itemlist");


                    for(int i= 0; i<array.length(); i++) {
                        JSONObject o =array.getJSONObject(i);
                        ListItem item = new ListItem(o.getString("item_id"), o.getString("name"), o.getString("quantity"), o.getString("price"), o.getString("restaurant"));
                        listItems.add(item);
                    }

                    //list of items sent to adapter so that the menu can be populated
                    adapter= new RecyclerAdapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RestaurantMenu.this,"Login Error!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestaurantMenu.this,"Login Error!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nfc", nfcID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}