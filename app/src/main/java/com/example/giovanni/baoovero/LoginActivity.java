package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email,input_password;
    TextView btnSignup,btnForgotPass;
    RelativeLayout activity_main;
    private FirebaseAuth auth;
    private Vibrator myVib;
    private final static int RC_SIGN_IN=3456;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("740390149429-qr2u2oldluul8d246d47rcrbr7k4d655.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //   .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            //View
        btnLogin = (Button)findViewById(R.id.login_btn_login);
        input_email = (EditText)findViewById(R.id.login_email);
        input_password = (EditText)findViewById(R.id.login_password);
        btnSignup = (TextView)findViewById(R.id.login_btn_signup);
        btnForgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        myVib=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        btnSignup.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnGoogle = (SignInButton)findViewById(R.id.login_btn_google);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();


        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
    }


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
                Toast.makeText(this, "Accesso con Google fallito", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        //updateUI(currentUser);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Autenticazione fallita", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }






















    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_forgot_password)
        {
            myVib.vibrate(25);
            startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_signup)
        {
            myVib.vibrate(25);
            startActivity(new Intent(LoginActivity.this,SignUp.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_login)
        {
            myVib.vibrate(25);
            closeKeyboard();
            if (input_email.getText().toString().isEmpty()||!input_email.getText().toString().contains("@")||input_password.getText().toString().isEmpty())
            {
                Snackbar snackBar = Snackbar.make(activity_main, "C'è qualcosa che non va. Sicuro di aver inserito correttamente email e password?", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
            else
            loginUser(input_email.getText().toString(),input_password.getText().toString());
        }
    }

    private void loginUser(String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                                Snackbar snackBar = Snackbar.make(activity_main,"L'email o la password sono errati.",Snackbar.LENGTH_SHORT);
                                snackBar.show();
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                        }
                    }
                });
    }
    private void closeKeyboard(){
        View vista = this.getCurrentFocus();
        if (vista!=null){
            InputMethodManager inputt = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputt.hideSoftInputFromWindow(vista.getWindowToken(),0);
        }
    }
}

