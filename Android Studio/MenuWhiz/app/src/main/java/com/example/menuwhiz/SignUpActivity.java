package com.example.menuwhiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;

//Activity that deals with signup activity using email
public class SignUpActivity extends AppCompatActivity {
private TextView existingAccount;
private EditText eName, eEmailAddress, ePhone,  ePassword, eConfirmPassword;
private Button btn_signup;
private  static String URL_REGIST="http://192.168.1.87:8000/CustomerRegistration/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        existingAccount = (TextView) findViewById(R.id.existingAccount);

        eName = (EditText) findViewById(R.id.eName);
        eEmailAddress = (EditText) findViewById(R.id.eEmailAddress);
        ePhone = (EditText) findViewById(R.id.ePhone);
        ePassword = (EditText) findViewById(R.id.ePassword);
        eConfirmPassword = (EditText) findViewById(R.id.eConfirmPassword);
        btn_signup = (Button)  findViewById(R.id.btn_signup);


        //button to move to sign in activity
        existingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ret_dsp = new Intent(SignUpActivity.this,LogInActivity.class);
                startActivity(ret_dsp);
                finish();
            }
        });

        //button for sign up
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checks if all inputs are provided and if passwords match
                if(IsValid()) {
                    SignUp();
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Invalid input",Toast.LENGTH_SHORT).show();

                }




            }
        });

    }

    //function to check if all the inputs are given by user and if the passwords match.
    private boolean IsValid() {
        String cfmpw, pw, n, ph;
        n=this.eName.getText().toString().trim();
        ph= this.ePhone.getText().toString().trim();
        cfmpw = this.eConfirmPassword.getText().toString().trim();
        pw=this.ePassword.getText().toString().trim();

        if(n.isEmpty() || ph.isEmpty() || cfmpw.isEmpty() || pw.isEmpty() || !pw.equals(cfmpw)) {
            return false;
        }
        else {
            return  true;
        }
    }

    //sign up function that takes name, email, phone and password of customer as parameter and saves it to the database
    private void SignUp(){
        btn_signup.setVisibility(View.GONE);

        final String name= this.eName.getText().toString().trim();
        final String email= this.eEmailAddress.getText().toString().trim();
        final String phone= this.ePhone.getText().toString().trim();
        final String password= this.ePassword.getText().toString().trim();




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

                    SessionManagement sessionManagement = new SessionManagement(SignUpActivity.this);
                    sessionManagement.saveSessionCustomer(customer);

                    Intent intent=new Intent(SignUpActivity.this, MainMenu.class);
                    startActivity(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this,"Register Error!" +e.toString(),Toast.LENGTH_SHORT).show();
                    btn_signup.setVisibility(View.VISIBLE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this,"Register Error!" +error.toString(),Toast.LENGTH_SHORT).show();
                btn_signup.setVisibility(View.VISIBLE);
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}