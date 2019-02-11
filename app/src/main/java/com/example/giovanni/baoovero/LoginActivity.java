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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText input_email,input_password;
    TextView btnSignup,btnForgotPass;
    RelativeLayout activity_main;
    private FirebaseAuth auth;
    private Vibrator myVib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();


        if(auth.getCurrentUser() != null)
            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
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

