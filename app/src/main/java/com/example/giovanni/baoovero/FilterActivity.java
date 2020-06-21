package com.example.giovanni.baoovero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.reverse;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;
    private String ftbreed,ftage,ftcity,name;
    private boolean provenienza;
    private int numero,conta=0,conto=0;
    private TextView filtro,errore;
    private ImageView immagine;
    private boolean ordine=false;
    private View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        v = findViewById(android.R.id.content);

        filtro = (TextView)findViewById(R.id.filter_result);
        errore = (TextView)findViewById(R.id.filter_error);
        immagine=(ImageView)findViewById(R.id.filter_image);
        immagine.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        String ftgender=intent.getExtras().getString("Gender");
        ftbreed=intent.getExtras().getString("Breed");
        ftage=intent.getExtras().getString("Age");
        ftcity=intent.getExtras().getString("City");
        provenienza = intent.getExtras().getBoolean("Provenienza");
        listacani = new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myrv = (RecyclerView) findViewById(R.id.recyclerview_filter);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        if(provenienza){
            setTitle("Find your dog's partner");
            name =intent.getExtras().getString("Name");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listacani.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Dog cane = snapshot.getValue(Dog.class);
                            if (cane.getName().toLowerCase().contains(name.toLowerCase()))
                                listacani.add(cane);
                        }
                        if (listacani.isEmpty()) {
                            filtro.setVisibility(View.INVISIBLE);
                            errore.setText("The search did not return any results.");
                            immagine.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            filtro.setText("Results of the search of:\n" +name);
                            errore.setVisibility(View.INVISIBLE);
                            myAdapter=new RecyclerViewAdapter( FilterActivity.this,listacani);
                            myrv.setAdapter(myAdapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            setTitle("Search");
            Query query = myRef.orderByChild("gender").equalTo(ftgender);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            listacani.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Dog cane = snapshot.getValue(Dog.class);
                    listacani.add(cane);
                    if (!ftbreed.isEmpty()) {
                        for (Dog a : listacani) {
                            if (!(a.getBreed().equalsIgnoreCase(ftbreed))) {
                                listacani.remove(a);
                            }
                        }
                    }
                    if (!ftcity.isEmpty())
                    {
                        for (Dog a : listacani) {
                            if (!(a.getCity().equalsIgnoreCase(ftcity))) {
                                listacani.remove(a);
                            }
                        }
                    }
                    if (!ftage.isEmpty()) {
                        for (Dog a : listacani) {
                            if (Integer.parseInt(a.getAge())>Integer.parseInt(ftage) && Integer.parseInt((ftage)) != 0){
                                listacani.remove(a);
                            }
                        }
                    }
                }
                if (listacani.isEmpty()){
                    filtro.setVisibility(View.INVISIBLE);
                    errore.setText("The search did not return any results.");
                immagine.setVisibility(View.VISIBLE);
            }
            else {
                    filtro.setText("Results of the search");
                    errore.setVisibility(View.INVISIBLE);
                    myAdapter=new RecyclerViewAdapter( FilterActivity.this,listacani);
                    myrv.setAdapter(myAdapter);
            }
            }
        }



        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater inflauto = getMenuInflater();
        inflauto.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.ordina)
        {
            new AlertDialog.Builder(this)
                    .setMessage("Sort by")
                    .setCancelable(true)
                    .setPositiveButton("Age", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            conto++;
                            ordine=true;
                            Collections.sort(listacani, new Comparator<Dog>() {
                                @Override
                                public int compare(Dog o1, Dog o2) {
                                    return o1.getAge().compareTo(o2.getAge());
                                }
                            });
                            if (conto%2!=0){

                                Snackbar.make(v,"Age ascending order",Snackbar.LENGTH_SHORT).show();
                            }
                            else
                            {
                                reverse(listacani);

                                Snackbar.make(v,"Age descending order.",Snackbar.LENGTH_SHORT).show();
                            }
                            myAdapter=new RecyclerViewAdapter( FilterActivity.this,listacani);
                            myrv.setAdapter(myAdapter);
                        }
                    })
                    .setNegativeButton("Publication date", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            conta++;
                            if (ordine)
                                conta=1;
                            ordine=false;
                            reverse(listacani);
                            myAdapter=new RecyclerViewAdapter( FilterActivity.this,listacani);
                            myrv.setAdapter(myAdapter);
                            if(conta%2==0) {
                                //item.setIcon(R.drawable.ic_discendant_sort);
                                Snackbar.make(v,"From the most recent post",Snackbar.LENGTH_SHORT).show();
                            }
                            else {
                                //item.setIcon(R.drawable.ic_ascendant_sort);
                                Snackbar.make(v,"From the oldest post",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

}
