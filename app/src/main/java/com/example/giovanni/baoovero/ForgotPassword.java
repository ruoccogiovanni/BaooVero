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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText input_email;
    private Button btnResetPass;
    private TextView btnBack;
    private RelativeLayout activity_forgot;
    private Vibrator myVib;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Password dimenticata");
        //View
        input_email = (EditText)findViewById(R.id.forgot_email);
        btnResetPass = (Button)findViewById(R.id.forgot_btn_reset);
        btnBack = (TextView)findViewById(R.id.forgot_btn_back);
        activity_forgot = (RelativeLayout)findViewById(R.id.activity_forgot_password);
        myVib=(Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        btnResetPass.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassword.this,MainActivity.class));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.forgot_btn_back)
        {
            myVib.vibrate(25);
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else  if(view.getId() == R.id.forgot_btn_reset)
        {
            myVib.vibrate(25);
            closeKeyboard();
            if (input_email.getText().toString().isEmpty()||!input_email.getText().toString().contains("@"))
            {
                Snackbar snackBar = Snackbar.make(activity_forgot,"C'è qualcosa che non va. Sicuro di aver inserito correttamente la tua email?",Snackbar.LENGTH_LONG);
                snackBar.show();
                input_email.setHintTextColor(getResources().getColor(R.color.error_color));
                input_email.setText("");
            }
            else
            resetPassword(input_email.getText().toString());

        }
    }

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Snackbar snackBar = Snackbar.make(activity_forgot,"Abbiamo mandato la password all'email: "+email,Snackbar.LENGTH_LONG);
                            snackBar.show();
                        }
                        else{
                            Snackbar snackBar = Snackbar.make(activity_forgot,"Riprova, la password non è stata resettata.",Snackbar.LENGTH_SHORT);
                            snackBar.show();
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
