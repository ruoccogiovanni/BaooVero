package com.example.giovanni.baoovero;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        auth = FirebaseAuth.getInstance();
        utente = auth.getCurrentUser().getUid();
        listacani=new ArrayList<>();
        uid = new HashMap();
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myRef2 = FirebaseDatabase.getInstance().getReference("Utenti");
        myrv = (RecyclerView) findViewById(R.id.favourite_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Utente match = dataSnapshot.child(utente).getValue(Utente.class);
            uid=match.getPreferiti();
            int grandezza= uid.values().size();
            final String[]preferiti= new String[grandezza];
            uid.values().toArray(preferiti);
            System.out.println(preferiti[grandezza-1]);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                        Dog cane = posSnapshot.getValue(Dog.class);
                        for (String s: preferiti) {
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
        }

        @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        };
        myRef2.addValueEventListener(listener);
/*




 */


    }


}