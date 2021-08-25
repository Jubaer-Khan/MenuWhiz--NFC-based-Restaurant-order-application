package com.example.menuwhiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//Activity that deals with logging into the application. Contains both email login and google sign in
public class LogInActivity extends AppCompatActivity {
    private EditText eEmailAddress, ePassword;
    private TextView forgotPassword;
    private Button btnSignup,btnSignin;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton gSignin;
    private static String URL_LOGIN ="http://192.168.1.87:8000/CustomerLogin/";
    private  static String URL_CUSTOMER_RETRIEVE="http://192.168.1.87:8000/RetrieveCustomer/";
    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        eEmailAddress = (EditText) findViewById(R.id.eEmailAddress);
        ePassword = (EditText) findViewById(R.id.ePassword);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        btnSignup = (Button)  findViewById(R.id.btnSignup);
        btnSignin = (Button)  findViewById(R.id.btnSignin);
        gSignin = findViewById(R.id.google_sign_in);

        String serverClientId = getString(R.string.server_client_id);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestServerAuthCode(serverClientId)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //button to move to sign up instead
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(reg);
                finish();

            }
        });

        //Log in button for email log in
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = eEmailAddress.getText().toString().trim();
                String mPass = ePassword.getText().toString().trim();

                if(!mEmail.isEmpty() || !mPass.isEmpty()) {
                    LogIn(mEmail,mPass);

                } else {
                    eEmailAddress.setError("Please insert email");
                    ePassword.setError("Please insert password");
                }


            }
        });

        //Forgot Password button (has not been configured yet)
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        //Google sign in button
        gSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.google_sign_in:
                        signIn();
                        break;
                }
            }
        });



    }


    //function for email login. Takes email and password as parameter, backend checks if the record exists and if the password is correct
    private void LogIn(final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String user_id = jsonObject.getString("customer_id");
                    String user_name = jsonObject.getString("name");
                    String user_email = jsonObject.getString("email");
                    String user_phone = jsonObject.getString("phone");

                    Customer customer = new Customer(user_id, user_name, user_email, user_phone);

                    SessionManagement sessionManagement = new SessionManagement(LogInActivity.this);
                    sessionManagement.saveSessionCustomer(customer);

                    Intent intent = new Intent(LogInActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LogInActivity.this,"Login Error!" +e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogInActivity.this,"Login Error!" +error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);





    }

    //google sign in function
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            checkNewUser();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    //checks whether the user using google sign in is signing in for the first time (hence need to save his phone number) or resigning in (phone number exists, move straight to main menu)
    private void checkNewUser() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        final String  email;
        if (acct != null) {
            email = acct.getEmail();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CUSTOMER_RETRIEVE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        //phone number doesn't exist hence move to phone number activity
                        Intent intent = new Intent(LogInActivity.this, PhoneNumberActivity.class);
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LogInActivity.this);

                        String  name, email;
                        if (acct != null) {

                            name = acct.getDisplayName();
                            email = acct.getEmail();

                            intent.putExtra("name",name);
                            intent.putExtra("email",email);
                        }


                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //phone number exists hence set customer session and move to main menu

                            String user_id = jsonObject.getString("customer_id");
                            String user_name = jsonObject.getString("name");
                            String user_email = jsonObject.getString("email");
                            String user_phone = jsonObject.getString("phone");

                            Customer customer = new Customer(user_id, user_name, user_email, user_phone);

                            SessionManagement sessionManagement = new SessionManagement(LogInActivity.this);
                            sessionManagement.saveSessionCustomer(customer);

                            Intent intent = new Intent(LogInActivity.this, MainMenu.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            Toast.makeText(LogInActivity.this,"Login Error!" +e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LogInActivity.this,"Error!" +error.toString(),Toast.LENGTH_SHORT).show();

                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        }
    }

    //function that specifies what happens when this activity starts, in this case, checks for currently signed in user.
    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    //function to check if a user had signed in before and didn't sign out
    private void checkSession() {
        //check if user logged in through google account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account!= null) {
            Intent intent = new Intent(LogInActivity.this, MainMenu.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            String email;
            if (acct != null) {

                email = acct.getEmail();
                intent.putExtra("email",email);
            }


            startActivity(intent);
            finish();
        }

        //check if user logged in user logged in through email

        SessionManagement sessionManagement = new SessionManagement(LogInActivity.this);
        String customer_id = sessionManagement.getSessionCustomer();

        if (customer_id!=null) {
            Intent intent = new Intent(LogInActivity.this, MainMenu.class);
            startActivity(intent);
            finish();

        }
    }


}