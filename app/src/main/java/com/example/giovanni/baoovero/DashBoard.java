package com.example.giovanni.baoovero;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    private TextView txtWelcome;
    private Button btnLogout;
    private RelativeLayout activity_dashboard;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //View
        txtWelcome = (TextView)findViewById(R.id.dashboard_welcome);
        btnLogout = (Button)findViewById(R.id.dashboard_btn_logout);
        activity_dashboard = (RelativeLayout)findViewById(R.id.activity_dash_board);

        btnLogout.setOnClickListener(this);
        //Init Firebase
        auth = FirebaseAuth.getInstance();
        //Session check
        if(auth.getCurrentUser() != null)
            txtWelcome.setText("Benvenuto, "+auth.getCurrentUser().getEmail());


    }

    @Override
    public void onClick(View view) {
            logoutUser();
    }

    private void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(DashBoard.this,MainActivity.class));
            finish();
        }
    }
}