package com.example.giovanni.baoovero;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private  RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;
    private DatabaseReference myRef2;
    private FirebaseAuth auth;
    private String utente;
    private List<String> uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        auth = FirebaseAuth.getInstance();
        utente = auth.getCurrentUser().getUid();
        listacani=new ArrayList<>();
        uid = new ArrayList();

        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myRef2 = FirebaseDatabase.getInstance().getReference("Utenti");
        myrv = (RecyclerView) findViewById(R.id.favourite_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                            Dog cane = posSnapshot.getValue(Dog.class);
                            for (String s: uid) {
                                System.out.println(cane.getUid() + "ooooooooooooooooooooo " + s);
                                if (cane.getUid().equals(s)) {
                                    listacani.add(cane);
                                }
                            }
                            myAdapter=new RecyclerViewAdapter(FavouriteActivity.this,listacani);
                            myrv.setAdapter(myAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                uid.add(dataSnapshot.child(utente).child("Preferiti").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavouriteActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
