package com.example.giovanni.baoovero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ProfileActivity extends AppCompatActivity {
    private Button logout;
    private Button adddog;
    private FirebaseAuth auth;
    private TextView welcome;
    private Vibrator myVib;
    private RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewProfile myAdapter;
    private DatabaseReference myRef,myRef2;
    private String utente,email;
    private ImageView immagine;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        welcome=(TextView)findViewById(R.id.profile_welcome);
        immagine=(ImageView)findViewById(R.id.profile_image);
        immagine.setVisibility(View.INVISIBLE);
        setTitle("Profile");
        auth = FirebaseAuth.getInstance();
        final Intent loginact = new Intent(ProfileActivity.this,LoginActivity.class);
        if(auth.getCurrentUser() != null) {
            utente = auth.getCurrentUser().getUid();
            email=auth.getCurrentUser().getEmail();
        }
        else startActivity(loginact);
        logout=(Button)findViewById(R.id.profile_logout);
        adddog=(Button)findViewById(R.id.profile_add);
        myVib=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        listacani = new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myRef2=FirebaseDatabase.getInstance().getReference("Utenti");
        final Query query=myRef.orderByChild("utente").equalTo(utente);
        query.addListenerForSingleValueEvent(valueEventListener);
        Query profilo=myRef2.orderByChild("email").equalTo(email);
        profilo.addListenerForSingleValueEvent(evento);
        myrv = (RecyclerView) findViewById(R.id.profile_recyclerview);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        final Intent intentmain = new Intent(this, MainActivity.class);
        adddog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(25);
                startActivity(new Intent(ProfileActivity.this, AddActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(250);
                Toast.makeText(getApplicationContext(),"Torna presto!",Toast.LENGTH_SHORT).show();
                auth.signOut();
                if(auth.getCurrentUser() == null)
                {
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeprofile);
        swipeRefreshLayout.setColorSchemeResources(R.color.prova,R.color.Rosso,R.color.verde);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                query.addListenerForSingleValueEvent(valueEventListener);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            listacani.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Dog cane = snapshot.getValue(Dog.class);
                    listacani.add(cane);
                }
                reverse(listacani);
                myAdapter=new RecyclerViewProfile( ProfileActivity.this,listacani);
                myrv.setAdapter(myAdapter);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener evento = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snap: dataSnapshot.getChildren())
            {
                Utente user=snap.getValue(Utente.class);
                if (listacani.isEmpty())
                {
                    welcome.setText("Welcome, " + user.getNome() + " " + user.getCognome() +"\nAdd some puppies.");
                    immagine.setVisibility(View.VISIBLE);
                }
                else
                welcome.setText("Welcome, " + user.getNome() + " " + user.getCognome() +"\nThese are your dogs.");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflauto = getMenuInflater();
        inflauto.inflate(R.menu.profilefav_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.preferiti)
        {
            startActivity(new Intent(ProfileActivity.this,FavouriteActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    }
}