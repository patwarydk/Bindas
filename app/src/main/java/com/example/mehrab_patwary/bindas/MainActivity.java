package com.example.mehrab_patwary.bindas;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private EditText emamilEt;
    private EditText passEt;
    private TextView statusTv;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
// Splash screen with login start *******************************
    LinearLayout layout1, activity_main;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            layout1.setVisibility(View.VISIBLE);
            activity_main.setVisibility(View.VISIBLE);
        }
    };
    // Splash screen with login End *****************************

    // Gmail login Start  *************************************

    SignInButton googleSighnin;
   // FirebaseAuth mAuth;
    private final static int RC_SIGN_IN=2;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListner);
    }
    // Gmail login End *****************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emamilEt = findViewById(R.id.editTextEmail);
        passEt = findViewById(R.id.editTextPass);
        statusTv = findViewById(R.id.textViewStatus);
        // Splash with login start
        layout1 = findViewById(R.id.layout1);
        activity_main = findViewById(R.id.activity_main);
        handler.postDelayed(runnable, 1000);
        // Splash with login end
        // Gmail lgoin start ************************************************

      auth = FirebaseAuth.getInstance();
      googleSighnin = findViewById(R.id.signGoogle);
      googleSighnin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              signIn();
          }
      });
      auth = FirebaseAuth.getInstance();
      mAuthListner = new FirebaseAuth.AuthStateListener() {
          @Override
          public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              if(firebaseAuth.getCurrentUser()!=null){
                  startActivity(new Intent(MainActivity.this,DrawerActivity.class));
              }
          }
      };
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestIdToken(getString(R.string.default_web_client_id))
              .requestEmail()
              .build();
     mGoogleApiClient = new GoogleApiClient.Builder(this)
             .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                 @Override
                 public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                     Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                 }
             })
             .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
             .build();

    }
/*    GoogleSignInOptions gso = new
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();*/

    // Gmil Signin Start *************************************
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag","SignInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG","signInWithCredential:failure",task.getException());

                            Snackbar.make(findViewById(R.id.activity_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }
                    }
                });

    }
    // Gmail login End *********************************************

    public void singnInButton(View view) {
        String email = emamilEt.getText().toString();
        String pass = passEt.getText().toString();
        Task<AuthResult> task = auth.signInWithEmailAndPassword(email,pass); // siweap method return a Task, so we store in task
        // now we set diffrent types of listner to return a message
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // if registration successfully compleated, onCompleate mdthod will be call and return task.
                if (task.isSuccessful()){
                    currentUser = auth.getCurrentUser();
                    statusTv.setText("Logd in as"+currentUser.getEmail());
                    statusTv.setVisibility(View.VISIBLE);
                    if (currentUser !=null){
                        startActivity(new Intent(MainActivity.this,DrawerActivity.class));
                    }
                }
            }

        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                statusTv.setText(e.getMessage()); // why use e ??
                statusTv.setVisibility(View.VISIBLE);

            }
        });

    }
// Forgot password Start ********************************************************************
    public void forgotPass(View view) {
        startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));

    }
    // Forgot password End ********************************************************************

    public void sigunupUser(View view) {
        startActivity(new Intent(MainActivity.this,SignupActivity.class));
    }
}
