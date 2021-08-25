package com.example.menuwhiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Adapter that deals with populating the cart items on top of the parent layout in cart activity
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<ListItem> lisItems;
    private Context context;

    private static String URL_TEMP_DELETE="http://192.168.1.87:8000//DeletetempItem/";

    public CartAdapter(List<ListItem> lisItems, Context context) {
        this.lisItems = lisItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //specifies which layout is used on top of the parent layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_view, parent, false);

        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //takes the current item from the selected list of items and updates the fields accordingly
        final ListItem listItem = lisItems.get(position);

        holder.name.setText(listItem.getName());
        holder.orderqty.setText(listItem.getQuantity());
        holder.totalprice.setText(listItem.getPrice());

        //button to delete any item from the currently selected items
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(listItem);
            }
        });

    }

    //function to remove an item from cart. Takes customer, restaurant and item ids as parameters
    private void deleteItem(ListItem listItem) {
        SessionManagement sessionManagement = new SessionManagement(context);
        final String customer= sessionManagement.getSessionCustomer();

        final String restaurant = listItem.getRestaurant();
        final String item_id = listItem.getItem_id();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TEMP_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Could not delete item!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Could not delete item!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant", restaurant);
                params.put("customer", customer);
                params.put("item", item_id);
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
        public TextView orderqty;
        public TextView totalprice;
        public Button delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            totalprice = itemView.findViewById(R.id.totalprice);
            orderqty = itemView.findViewById(R.id.orderqty);
            delete=itemView.findViewById(R.id.delete);

        }
    }
}
