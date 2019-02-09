package com.example.giovanni.baoovero;

import android.content.Intent;
import android.os.Bundle;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        logout=(Button)findViewById(R.id.profile_logout);
        adddog=(Button)findViewById(R.id.profile_add);
        welcome=(TextView)findViewById(R.id.profile_welcome);
        final Intent loginact = new Intent(ProfileActivity.this,LoginActivity.class);
        if(auth.getCurrentUser() != null)
            welcome.setText("Benvenuto, "+auth.getCurrentUser().getEmail());
        else
            startActivity(loginact);
        final Intent intentadd = new Intent(this, Add_Activity.class);
        final Intent intentmain = new Intent(this, MainActivity.class);


        adddog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentadd);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Cess non logouttare o frat",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Fevorit",Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(ProfileActivity.this,FilterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}