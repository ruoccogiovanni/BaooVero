package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
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
    SignInButton btnGoogleLogin;
    EditText input_email,input_password;
    TextView btnSignup,btnForgotPass;
    RelativeLayout activity_main;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 0;
    private Vibrator myVib;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        btnGoogleLogin = findViewById(R.id.googlelogin_btn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intento, RC_SIGN_IN);
            }



        });
        mAuth = FirebaseAuth.getInstance();

























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
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
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

            firebaseAuthWithGoogle(account.getIdToken());
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }



    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
        }
        super.onStart();
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
                Snackbar snackBar = Snackbar.make(activity_main, "Something goes wrong. Are you sure you entered your email and password correctly?", Snackbar.LENGTH_LONG);
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
                                Snackbar snackBar = Snackbar.make(activity_main,"Email or password are wrong.",Snackbar.LENGTH_SHORT);
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

