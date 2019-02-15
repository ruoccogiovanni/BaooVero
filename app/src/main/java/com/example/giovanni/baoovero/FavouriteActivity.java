package com.example.giovanni.baoovero;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        listacani=new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference();
        myrv = (RecyclerView) findViewById(R.id.favourite_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                  String uid = posSnapshot.child("Utenti").child("Preferiti").getValue().toString();
                  Dog cane = posSnapshot.child("Cani").getValue(Dog.class);
                  if(cane.getUid()==uid) listacani.add(cane);

              }
                myAdapter=new RecyclerViewAdapter(FavouriteActivity.this,listacani);
                myrv.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavouriteActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
