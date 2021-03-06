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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button btnSignup;
    TextView btnLogin;
    EditText input_email,input_pass,nome,cognome, confirm_pass;
    RelativeLayout activity_sign_up;
    private Vibrator myVib;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign up");
        mDatabase=FirebaseDatabase.getInstance().getReference();
        btnSignup = (Button)findViewById(R.id.signup_btn_register);
        btnLogin = (TextView)findViewById(R.id.signup_btn_login);

        input_email = (EditText)findViewById(R.id.signup_email);
        input_pass = (EditText)findViewById(R.id.signup_password);
        confirm_pass = (EditText)findViewById(R.id.confirm_password);
        nome = (EditText)findViewById(R.id.signup_username);
        cognome = (EditText)findViewById(R.id.signup_surname);
        activity_sign_up = (RelativeLayout)findViewById(R.id.activity_sign_up);
        myVib=(Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp.this,MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signup_btn_login){
            myVib.vibrate(25);
            startActivity(new Intent(SignUp.this,LoginActivity.class));
            finish();
        }
        else if(view.getId() == R.id.signup_btn_register){
            myVib.vibrate(25);
            closeKeyboard();
            if (input_email.getText().toString().isEmpty()||!input_email.getText().toString().contains("@")||input_pass.getText().toString().isEmpty()||input_pass.getText().toString().length()<6 || !input_pass.getText().toString().equals(confirm_pass.getText().toString()) )
            {
                Snackbar snackBar = Snackbar.make(activity_sign_up, "Something went wrong. Are you sure you entered your email and password correctly?", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
            else {
                signUpUser(input_email.getText().toString(), input_pass.getText().toString());
            }
        }
    }

    private void signUpUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            snackbar = Snackbar.make(activity_sign_up,"Error creating your account, retry.",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else{
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                            mDatabase.child("Utenti").child(currentFirebaseUser.getUid()).child("email").setValue(input_email.getText().toString());
                            mDatabase.child("Utenti").child(currentFirebaseUser.getUid()).child("nome").setValue(nome.getText().toString());
                            mDatabase.child("Utenti").child(currentFirebaseUser.getUid()).child("cognome").setValue(cognome.getText().toString());
                            startActivity(new Intent(SignUp.this,ProfileActivity.class));
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