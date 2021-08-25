package com.example.menuwhiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;


//activity that saves the phone number from google signin users as phone numbers are mandatory for the Customer records and Google API does not provide the phone number of users
public class PhoneNumberActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private com.hbb20.CountryCodePicker countryCode;
    private Button btnSavePhoneNumber;
    private  static String URL_REGIST="http://192.168.1.87:8000/CustomerRegistration/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumber = findViewById(R.id.phoneNumber);
        countryCode= findViewById(R.id.ccp);
        btnSavePhoneNumber= findViewById(R.id.GetPhoneNumber);

        //button for saving phone number
        btnSavePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //collecting the phone number entered
                String phone = phoneNumber.getText().toString().trim();

                //collecting the selected country code
                String countrycode= countryCode.getSelectedCountryCode();

                //concatanating country code and phone number to form the full number
                String number= countrycode+phone;

                Intent intent = getIntent();

                //collecting the google user name and email from intent
                String name = intent.getStringExtra("name");
                String email = intent.getStringExtra("email");

                //function to save the google user details to our database
                StoreGoogleInfo(name, email, number);



            }
        });




    }

    //function that takes the google user name, email and phone number as parameter and stores the user record in our local database
    private void StoreGoogleInfo(String name, String email, String phone) {
        final String Cname= name;
        final String Cemail= email;
        final String Cphone= phone;




        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String user_id = jsonObject.getString("customer_id");
                    String user_name = jsonObject.getString("name");
                    String user_email = jsonObject.getString("email");
                    String user_phone = jsonObject.getString("phone");

                    Customer customer = new Customer(user_id, user_name, user_email, user_phone);

                    SessionManagement sessionManagement = new SessionManagement(PhoneNumberActivity.this);
                    sessionManagement.saveSessionCustomer(customer);

                    Intent intent = new Intent(PhoneNumberActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PhoneNumberActivity.this,"Error!" +e.toString(),Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PhoneNumberActivity.this,"Error!" +error.toString(),Toast.LENGTH_SHORT).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", Cname);
                params.put("email", Cemail);
                params.put("phone", Cphone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}