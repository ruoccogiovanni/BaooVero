package com.example.giovanni.baoovero;

import android.content.Intent;
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

public class FilterActivity extends AppCompatActivity {

    private  RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent intent = getIntent();
        String ftgender=intent.getExtras().getString("Gender");
        String ftbreed=intent.getExtras().getString("Breed");
        String ftage=intent.getExtras().getString("Age");
        String ftcity=intent.getExtras().getString("City");
        listacani = new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myrv = (RecyclerView) findViewById(R.id.recyclerview_filter);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                    Dog cane = posSnapshot.getValue(Dog.class);
                    listacani.add(cane);
                }
                myAdapter=new RecyclerViewAdapter(FilterActivity.this,listacani);
                myrv.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FilterActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });









    }


}
