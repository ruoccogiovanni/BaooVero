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
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Dog> listacani;


    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listacani = new ArrayList<>();
        listacani.add(new Dog("Oliver","West Highland White Terrier","Oliviiiiiiiero","Maschio","Napoli", "4","3463919107", "gionni0797@gmail.com", R.drawable.image_olivi));
        listacani.add(new Dog("Cico","Chihuahua","Sono un cane piccolino ma mi piace tanto giocare","Maschio","Salerno", "4","3347626298", "pepperaul@gmail.com", R.drawable.image_cico));
        listacani.add(new Dog("Giselle","Briard","Patàààà","Femmina", "Caserta", "8", "3932791138", "ivanorefix3@yahoo.com", R.drawable.image_gisy));
        listacani.add(new Dog("Enya","Meticcio","Non sanno ancora che sono un gatto","Femmina","Caserta", "7","3665048238", "manfr_96@gmail.com", R.drawable.image_enya));
        listacani.add(new Dog("Piccola","Meticcio","Sono vecchia e non cammino","Femmina", "Caserta", "14", "3292127611", "ottaviafalco@gmail.com", R.drawable.image_piccola));
        listacani.add(new Dog("Nina","Meticcio","Come fanno a non capirlo","Femmina","Caserta", "9","3292127611", "ottaviafalco@gmail.com", R.drawable.image_nina));
        listacani.add(new Dog("Giselle","Briard","Patàààà","Femmina", "Caserta", "8", "3932791138", "ivanorefix3@yahoo.com", R.drawable.image_gisy));
        listacani.add(new Dog("Oliver","West Highland White Terrier","Oliviiiiiiiero","Maschio","Napoli", "4","3463919107", "gionni0797@gmail.com", R.drawable.image_westie));
        listacani.add(new Dog("Giselle","Briard","Patàààà","Femmina", "Caserta", "8", "3932791138", "ivanorefix3@yahoo.com", R.drawable.image_gisy));
        listacani.add(new Dog("Oliver","West Highland White Terrier","Oliviiiiiiiero","Maschio","Napoli", "4","3463919107", "gionni0797@gmail.com", R.drawable.image_westie));
        listacani.add(new Dog("Giselle","Briard","Patàààà","Femmina", "Caserta", "8", "3932791138", "ivanorefix3@yahoo.com", R.drawable.image_gisy));

        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,listacani);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflauto = getMenuInflater();
        inflauto.inflate(R.menu.right_menu,menu);
        return true;
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
