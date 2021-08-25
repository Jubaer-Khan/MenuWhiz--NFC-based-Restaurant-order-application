package com.example.menuwhiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//function responsible for populating the restaurant menu interface
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ListItem> lisItems;
    private Context context;

    private static String URL_TEMP_ADD ="http://192.168.1.87:8000/AddtempItem/";

    public RecyclerAdapter(List<ListItem> lisItems, Context context) {
        this.lisItems = lisItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //specifies which layout is used on top of parent layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row, parent, false);

        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //takes the current item from the list of items and updates the fields accordingly
        final ListItem listItem = lisItems.get(position);
        final int[] qt = {0};
        final int quantity = Integer.parseInt(listItem.getQuantity());


        holder.name.setText(listItem.getName());
        holder.availableqty.setText(listItem.getQuantity());
        holder.price.setText(listItem.getPrice());
        holder.orderqty.setText(""+ qt[0]);


        //button to decrease selected quantity if quantity greater than 0
        holder.lowerQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qt[0]>0) qt[0]--;
                holder.orderqty.setText(""+ qt[0]);
            }
        });

        //button to increase selected quantity if selected quanity is still lower than available quantity
        holder.addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qt[0]<quantity) qt[0]++;
                holder.orderqty.setText(""+ qt[0]);
            }
        });

        //button to add the item to cart
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItem newItem = new ListItem(listItem.getItem_id(),listItem.getName(),""+qt[0],listItem.getPrice(),listItem.getRestaurant());
                AddItemtoCart(newItem);

            }
        });
    }

    //function to add the item to cart. Takes item details, restaunrant and customer ids as parameters and saves them to the tempItem table
    private void AddItemtoCart(ListItem newItem) {

        SessionManagement sessionManagement = new SessionManagement(context);
        final String customer= sessionManagement.getSessionCustomer();

        final String restaurant = newItem.getRestaurant();
        final String item_id = newItem.getItem_id();
        final String name = newItem.getName();
        final String quantity = newItem.getQuantity();
        String p2=  newItem.getPrice();
        float p= Float.parseFloat(p2);

        float price = p * Integer.parseInt(quantity);
        final String totalPrice = ""+price;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TEMP_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Could not add item!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Could not add item!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant", restaurant);
                params.put("customer", customer);
                params.put("item", item_id);
                params.put("name", name);
                params.put("quantity", quantity);
                params.put("price", totalPrice);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return lisItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView availableqty;
        public TextView price;
        public TextView orderqty;

        public Button addQty;
        public Button lowerQty;
        public Button addToCart;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            availableqty = itemView.findViewById(R.id.availableqty);
            price = itemView.findViewById(R.id.price);
            orderqty = itemView.findViewById(R.id.orderqty);
            addQty= itemView.findViewById(R.id.addQty);
            lowerQty=itemView.findViewById(R.id.lowerQty);
            addToCart=itemView.findViewById(R.id.addItem);



        }
    }
}
