package com.example.giovanni.baoovero;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FavouriteActivity extends AppCompatActivity {
    private  RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private FirebaseAuth auth;
    private String utente;
    private Map <String,String> uid;
    private String[] preferiti;
    private TextView errore;
    private ImageView immagine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        auth = FirebaseAuth.getInstance();
        utente = auth.getCurrentUser().getUid();
        listacani=new ArrayList<>();
        uid = new HashMap();
        errore = (TextView)findViewById(R.id.favourite_error);
        immagine=(ImageView)findViewById(R.id.favourite_image);
        immagine.setVisibility(View.INVISIBLE);
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myRef2 = FirebaseDatabase.getInstance().getReference("Utenti");
        myrv = (RecyclerView) findViewById(R.id.favourite_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Utente match = dataSnapshot.child(utente).getValue(Utente.class);
            uid=match.getPreferiti();
            try{
                int grandezza= uid.values().size();
                preferiti= new String[grandezza];
                uid.values().toArray(preferiti);
                }

                catch (NullPointerException e)
                {
                    /*View v = findViewById(android.R.id.content);
                    Snackbar.make(v,"Non hai ancora nessun preferito, che ne dici di pushare il quore?",Snackbar.LENGTH_LONG).show();
                   return;
                    */
                }


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listacani.clear();
                    for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                        Dog cane = posSnapshot.getValue(Dog.class);
                        for (String s: preferiti) {
                            if (cane.getUid().equals(s)) {
                                listacani.add(cane);
                            }
                        }
                        if (listacani.isEmpty())
                        {
                            errore.setVisibility(View.VISIBLE);
                            errore.setText("Non hai ancora nessun preferito.");
                            immagine.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            immagine.setVisibility(View.INVISIBLE);
                            errore.setVisibility(View.INVISIBLE);
                            myAdapter=new RecyclerViewAdapter(FavouriteActivity.this,listacani);
                            myrv.setAdapter(myAdapter);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        };
        myRef2.addValueEventListener(listener);
    }
protected void onResume(){
        super.onResume();
    setContentView(R.layout.activity_favourite);
        auth = FirebaseAuth.getInstance();
        utente = auth.getCurrentUser().getUid();
        listacani=new ArrayList<>();
        uid = new HashMap();
        errore = (TextView)findViewById(R.id.favourite_error);
        immagine=(ImageView)findViewById(R.id.favourite_image);
        immagine.setVisibility(View.INVISIBLE);
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myRef2 = FirebaseDatabase.getInstance().getReference("Utenti");
        myrv = (RecyclerView) findViewById(R.id.favourite_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utente match = dataSnapshot.child(utente).getValue(Utente.class);
                uid=match.getPreferiti();
                try{
                    int grandezza= uid.values().size();
                    preferiti= new String[grandezza];
                    uid.values().toArray(preferiti);
                }

                catch (NullPointerException e)
                {
                    /*View v = findViewById(android.R.id.content);
                    Snackbar.make(v,"Non hai ancora nessun preferito, che ne dici di pushare il quore?",Snackbar.LENGTH_LONG).show();
                   return;
                    */
                }


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listacani.clear();
                        for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                            Dog cane = posSnapshot.getValue(Dog.class);
                            for (String s: preferiti) {
                                if (cane.getUid().equals(s)) {
                                    listacani.add(cane);
                                }
                            }
                            if (listacani.isEmpty())
                            {
                                errore.setVisibility(View.VISIBLE);
                                errore.setText("Non hai ancora nessun preferito.");
                                immagine.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                immagine.setVisibility(View.INVISIBLE);
                                errore.setVisibility(View.INVISIBLE);
                                myAdapter=new RecyclerViewAdapter(FavouriteActivity.this,listacani);
                                myrv.setAdapter(myAdapter);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef2.addValueEventListener(listener);
    }


}