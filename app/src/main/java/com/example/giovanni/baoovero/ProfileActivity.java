package com.example.giovanni.baoovero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private Button logout;
    private Button adddog;
    private FirebaseAuth auth;
    private TextView welcome;
    private Vibrator myVib;
    private RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewProfile myAdapter;
    private DatabaseReference myRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        logout=(Button)findViewById(R.id.profile_logout);
        adddog=(Button)findViewById(R.id.profile_add);
        welcome=(TextView)findViewById(R.id.profile_welcome);
        myVib=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        listacani = new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myrv = (RecyclerView) findViewById(R.id.profile_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                    Dog cane = posSnapshot.getValue(Dog.class);
                    listacani.add(cane);
                }
                myAdapter=new RecyclerViewProfile( ProfileActivity.this,listacani);
                myrv.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });

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
            startActivity(new Intent(ProfileActivity.this,FavouriteActivity.class));
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