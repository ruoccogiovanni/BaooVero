package com.example.giovanni.baoovero;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private ListView searchdog;
    private ArrayAdapter<String> adattatore;
    private ArrayList<String> nomicani;
    private ProgressBar caricamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listacani = new ArrayList<>();
        nomicani=new ArrayList<String>();

        searchdog=(ListView)findViewById(R.id.search_dog);
        caricamento = (ProgressBar) findViewById(R.id.caricamento);
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                    Dog cane = posSnapshot.getValue(Dog.class);
                    listacani.add(cane);
                    nomicani.add(posSnapshot.child("name").getValue().toString());
                    caricamento.setVisibility(View.INVISIBLE);
                }
                myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
                myrv.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                caricamento.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });
        adattatore=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,nomicani);
        searchdog.setAdapter(adattatore);

        dl = findViewById(R.id.dl);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        t.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.profilo:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    //case R.id.sviluppatori:
                    //    startActivity(...);
                    //    break;
                    case R.id.about:
                        startActivity(new Intent(MainActivity.this, SliderActivity.class));
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        MenuInflater inflauto = getMenuInflater();
        inflauto.inflate(R.menu.right_menu,menu);
        MenuItem cerca=menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView)cerca.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.findItem(R.id.app_bar_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for (Dog cane:listacani){
                    if (cane.getName().equalsIgnoreCase(newText)){
                    myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
                    myrv.setAdapter(myAdapter);
                    }
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        if (item.getItemId()==R.id.filtra)
        {
                startActivity(new Intent(MainActivity.this,FilterSearchActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }
}
