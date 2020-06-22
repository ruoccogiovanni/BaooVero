package com.example.giovanni.baoovero;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FloatingActionButton adddog;
    private FirebaseAuth auth;
    private TextView welcome;
    private Vibrator myVib;
    private RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewProfile myAdapter;
    private DatabaseReference myRef,myRef2;
    private String utente,email;
    private ImageView immagine;
    private DatabaseReference mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mDatabase=FirebaseDatabase.getInstance().getReference();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
        if (acct != null && mDatabase.child("Utenti").child(acct.getId()).child("email").toString() != acct.getEmail())  {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            mDatabase.child("Utenti").child(personId).child("email").setValue(personEmail);
            mDatabase.child("Utenti").child(personId).child("nome").setValue(personGivenName);
            mDatabase.child("Utenti").child(personId).child("cognome").setValue(personFamilyName);
        }













        welcome=(TextView)findViewById(R.id.profile_welcome);
        immagine=(ImageView)findViewById(R.id.profile_image);
        immagine.setVisibility(View.INVISIBLE);
        setTitle("My Dogs");
        auth = FirebaseAuth.getInstance();
        final Intent loginact = new Intent(ProfileActivity.this,LoginActivity.class);
        if(auth.getCurrentUser() != null) {
            utente = auth.getCurrentUser().getUid();
            email=auth.getCurrentUser().getEmail();
        }
        else startActivity(loginact);
        adddog=(FloatingActionButton)findViewById(R.id.profile_add);
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
                if (auth.getCurrentUser()!=null)
                    startActivity(new Intent(ProfileActivity.this, AddActivity.class));
                else {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    Toast.makeText(ProfileActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
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



    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    }
}