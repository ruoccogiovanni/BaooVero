package com.example.giovanni.baoovero;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private String ftbreed,ftage,ftcity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent intent = getIntent();
        String ftgender=intent.getExtras().getString("Gender");
        ftbreed=intent.getExtras().getString("Breed");
        ftage=intent.getExtras().getString("Age");
        ftcity=intent.getExtras().getString("City");
        listacani = new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        Query query = myRef.orderByChild("gender").equalTo(ftgender);
        query.addListenerForSingleValueEvent(valueEventListener);
        myrv = (RecyclerView) findViewById(R.id.recyclerview_filter);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
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
                    if (!ftage.isEmpty()){
                        listacani.removeIf(n -> (Integer.parseInt(n.getAge())<= Integer.parseInt(ftage)));
                    }
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
