package com.example.menuwhiz;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//main screen after user signs in. prompts user to place phone near nfc tag. Upon nfc tag detection moves on to the restaurant's menu if it exists
public class MainMenu extends AppCompatActivity {
    private TextView text;
    private Button signOut;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        text = findViewById(R.id.textView);
        signOut = findViewById(R.id.signOut);

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

        //checks if phone has NFC reader hardware installed
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            text.setText("Device does not have NFC capabilities");
        } else {
            text.setText("Place phone near NFC Tag");
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        //sign out button
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });

    }

    // function that signs a user out by removing session information for both email login and google sign in
    private void SignOut() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!= null) {
            SessionManagement sessionManagement = new SessionManagement(MainMenu.this);
            sessionManagement.removeSessionCustomer();

            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainMenu.this, "Sign out succesfull", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });


        }

        else {

            SessionManagement sessionManagement = new SessionManagement(MainMenu.this);
            sessionManagement.removeSessionCustomer();

            Intent intent= new Intent(MainMenu.this, LogInActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(MainMenu.this, "Sign out succesfull", Toast.LENGTH_SHORT).show();
        }
    }

    //function that continuously checks if NFC tag is detected. Moves to new activity when detected
    @Override
    protected void onResume() {
        super.onResume();

        if(nfcAdapter != null) {
            //checks if nfc is disabled in device
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent,null,null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(nfcAdapter!=null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    //function that specifies what happens when NFC tag is found
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    //upon finding an nfc tag, the id is passed on to the restaurant menu activity as context, where it is used to retrieve the restaurant menu
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        String hexID = null;

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] UID = tag.getId();


                hexID = bytesToHexString(UID);

                Intent menuIntent = new Intent(MainMenu.this, RestaurantMenu.class);
                menuIntent.putExtra("nfc",hexID);
                startActivity(menuIntent);
                finish();
                

            }


        }


    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i< bytes.length; i++) {
            int b = bytes[i] & 0xff;

            if (b < 0x10)
                sb.append('0');

            sb.append(Integer.toHexString(b).toUpperCase());
            if (i<=bytes.length-2)
                sb.append(":");
        }
        return sb.toString();
        }


    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(Settings.ACTION_WIRELESS_SETTINGS);

        startActivity(intent);
    }


}


