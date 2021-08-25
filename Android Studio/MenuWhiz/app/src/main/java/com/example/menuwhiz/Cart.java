package com.example.menuwhiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Activity representing the cart inteface
public class Cart extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private RecyclerView.Adapter cartAdapter;
    private TextView calculatedPrice;
    private Button placeOrder;
    private Button backToMenu;


    private List<ListItem> selectedListItems;

    private static String URL_RETRIEVE_ITEM ="http://192.168.1.87:8000/RetrieveTempItem/";
    private static String URL_PLACE_ORDER ="http://192.168.1.87:8000/PlaceOrder/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets the parent layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerCart= findViewById(R.id.recyclerCart);
        recyclerCart.setHasFixedSize(true);

        calculatedPrice= findViewById(R.id.calculatedPrice);
        placeOrder = findViewById(R.id.placeOrder);
        backToMenu = findViewById(R.id.backToMenu);


        recyclerCart.setLayoutManager(new LinearLayoutManager(this));


        selectedListItems = new ArrayList<>();

        //Function to retrieve currently placed items in cart and populating the cart view with the items on top of the parent layout
        retrieveSelectedItems();


        //button to go back to restaurant menu
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this, RestaurantMenu.class);
                startActivity(intent);
                finish();
            }
        });

        //button to place order
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeCompleteOrder();


            }
        });





    }

    //Function to place order. Takes customer and restaurant ids as parameters, fetches records stored in tempItems table and adds the records to tables where final orders are stored (orders, order_content and placed_order), Updates the quantity in item table and finally deletes the items from tempItems table
    private void placeCompleteOrder() {
        SessionManagement sessionManagement = new SessionManagement(Cart.this);
        final String restaurant = sessionManagement.getSessionRestaurant();
        final String customer = sessionManagement.getSessionCustomer();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PLACE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(Cart.this,"Order succesfully placed!",Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Cart.this,"Order was not placed!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cart.this,"Connectivity isssue!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant", restaurant);
                params.put("customer", customer);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    //function to retrieve the items currently added to cart and check total price
    private void retrieveSelectedItems() {

        SessionManagement sessionManagement = new SessionManagement(Cart.this);
        final String restaurant = sessionManagement.getSessionRestaurant();
        final String customer = sessionManagement.getSessionCustomer();
        final double[] price = {0};

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RETRIEVE_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("itemlist");


                    for(int i= 0; i<array.length(); i++) {
                        JSONObject o =array.getJSONObject(i);
                        ListItem item = new ListItem(o.getString("item"), o.getString("name"), o.getString("quantity"), o.getString("price"), o.getString("restaurant"));
                        double p = Double.parseDouble(o.getString("price"));
                        price[0] +=p;
                        selectedListItems.add(item);
                    }


                    String p=""+price[0];
                    calculatedPrice.setText(p);

                    cartAdapter= new CartAdapter(selectedListItems, getApplicationContext());
                    recyclerCart.setAdapter(cartAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Cart.this,"No items added!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cart.this,"Connectivity isssue!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant", restaurant);
                params.put("customer", customer);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}