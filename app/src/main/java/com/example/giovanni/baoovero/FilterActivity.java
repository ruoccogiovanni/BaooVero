package com.example.giovanni.baoovero;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef;
    private String ftbreed,ftage,ftcity,name;
    private boolean provenienza;
    private TextView filtro,errore;
    private ImageView immagine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
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
                            errore.setText("Non ho trovato quello che cercavi.");
                            immagine.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            filtro.setText("Risultati della ricerca per il nome:\n" +name);
                            errore.setVisibility(View.INVISIBLE);
                        }

                        myAdapter=new RecyclerViewAdapter( FilterActivity.this,listacani);
                        myrv.setAdapter(myAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
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
                    if (!ftbreed.isEmpty())
                        listacani.removeIf(n -> !(n.getBreed().equalsIgnoreCase(ftbreed)));
                    if (!ftcity.isEmpty())
                        listacani.removeIf(n -> !(n.getCity().equalsIgnoreCase(ftcity)));
                    if (!ftage.isEmpty()) {
                        listacani.removeIf(n -> (Integer.parseInt(n.getAge()) < Integer.parseInt(ftage)));
                    }
                }
                if (listacani.isEmpty()){
                    filtro.setVisibility(View.INVISIBLE);
                    errore.setText("Non ho trovato quello che cercavi.");
                immagine.setVisibility(View.VISIBLE);
            }
            else {
                    filtro.setText("Questi sono i risultati.");
                    errore.setVisibility(View.INVISIBLE);
                }
                myAdapter=new RecyclerViewAdapter( FilterActivity.this,listacani);
                myrv.setAdapter(myAdapter);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
