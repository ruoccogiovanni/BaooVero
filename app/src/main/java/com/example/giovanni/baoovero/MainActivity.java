package com.example.giovanni.baoovero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class MainActivity extends AppCompatActivity {

    private  RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private ArrayAdapter<String> adattatore;
    private List<Dog> nomicani;
    private ProgressBar caricamento;
    private FirebaseAuth auth;
    private FloatingActionButton aggiungi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        listacani = new ArrayList<>();
        caricamento = (ProgressBar) findViewById(R.id.caricamento);
        aggiungi=(FloatingActionButton)findViewById(R.id.main_add);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser()!=null)
                    startActivity(new Intent(MainActivity.this,Add_Activity.class));
                else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(MainActivity.this, "Devi prima aver effettuato il login.", Toast.LENGTH_SHORT).show();
                }
                }
        });
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listacani.clear();
                for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                    Dog cane = posSnapshot.getValue(Dog.class);
                    listacani.add(cane);
                    caricamento.setVisibility(View.INVISIBLE);
                }
                reverse(listacani);
                myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
                myrv.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                caricamento.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.prova,R.color.Rosso,R.color.verde);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listacani.clear();
                        for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                            Dog cane = posSnapshot.getValue(Dog.class);
                            listacani.add(cane);
                            caricamento.setVisibility(View.INVISIBLE);
                        }
                        reverse(listacani);
                        myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
                        myrv.setAdapter(myAdapter);
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        caricamento.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
                    case R.id.navpreferiti:
                        if(auth.getCurrentUser() != null)
                            startActivity(new Intent(MainActivity.this,FavouriteActivity.class));
                        else
                            Toast.makeText(MainActivity.this, "Devi aver effettuato il login.", Toast.LENGTH_SHORT).show();
                        break;
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
                Intent intento = new Intent(MainActivity.this,FilterActivity.class);
                intento.putExtra("Name",query);
                intento.putExtra("Provenienza",true);
                startActivity(intento);
                searchView.onActionViewCollapsed();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //nomicani.removeIf(n ->!(n.getName().toLowerCase().contains(newText.toLowerCase())));
                //myAdapter=new RecyclerViewAdapter( MainActivity.this,nomicani);
                //myrv.setAdapter(myAdapter);
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
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Sei sicuro di voler chiudere Baoo?")
                .setCancelable(false)
                .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }



}
