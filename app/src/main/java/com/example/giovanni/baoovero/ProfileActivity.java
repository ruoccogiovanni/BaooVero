package com.example.giovanni.baoovero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private Button logout;
    private Button adddog;
    private FirebaseAuth auth;
    private TextView welcome;
    private Vibrator myVib;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        logout=(Button)findViewById(R.id.profile_logout);
        adddog=(Button)findViewById(R.id.profile_add);
        welcome=(TextView)findViewById(R.id.profile_welcome);
        myVib=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        final Intent loginact = new Intent(ProfileActivity.this,LoginActivity.class);
        if(auth.getCurrentUser() != null)
            welcome.setText("Benvenuto, "+auth.getCurrentUser().getEmail());
        else
            startActivity(loginact);
        final Intent intentmain = new Intent(this, MainActivity.class);


        adddog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(25);
                startActivity(new Intent(ProfileActivity.this,Add_Activity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(100);
                Toast.makeText(getApplicationContext(),"Torna presto!",Toast.LENGTH_SHORT).show();
                auth.signOut();
                if(auth.getCurrentUser() == null)
                {
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                    finish();
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflauto = getMenuInflater();
        inflauto.inflate(R.menu.profilefav_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.preferiti)
        {
            Toast.makeText(getApplicationContext(),"Questa è la pagina dei tuoi preferiti.",Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(ProfileActivity.this,FilterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    }
}