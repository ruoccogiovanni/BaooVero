package com.example.giovanni.baoovero;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.rotate;

public class MainActivity extends AppCompatActivity {

    private  RecyclerView myrv;
    private List<Dog> listacani;
    private RecyclerViewAdapter myAdapter;
    private DatabaseReference myRef,myRef2;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private ProgressBar caricamento;
    private FirebaseAuth auth;
    private SearchView searchView;
    private FloatingActionButton aggiungi;
    private int numero,conta=0;
    private TextView tvnamelogin, tvemaillogin;
    private ImageView immaginelogin;
    private String utente,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        listacani = new ArrayList<>();
        NavigationView navigescionView = (NavigationView) findViewById(R.id.nv);
        View headerView = navigescionView.getHeaderView(0);
        immaginelogin= (ImageView) headerView.findViewById(R.id.immaginelogin);
        immaginelogin.setImageResource(R.drawable.ic_icona_profilo);
        tvnamelogin = (TextView) headerView.findViewById(R.id.navigation_name);
        tvemaillogin = (TextView) headerView.findViewById(R.id.navigation_email);
        tvemaillogin.setPaintFlags(tvemaillogin.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        numero=0;
        caricamento = (ProgressBar) findViewById(R.id.caricamento);
        aggiungi=(FloatingActionButton)findViewById(R.id.main_add);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser()!=null)
                    startActivity(new Intent(MainActivity.this, AddActivity.class));
                else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    Toast.makeText(MainActivity.this, "Devi prima aver effettuato il login.", Toast.LENGTH_SHORT).show();
                }
                }
        });
       aggiungi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                numero++;
                Toast.makeText(MainActivity.this, "Ce ne hai messo di tempo per trovarmi.\nConsiglio: attiva la rotazione automatica.", Toast.LENGTH_SHORT).show();
                if (numero%2==1)
                    MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                else
                    MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            }
        });
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        myRef2=FirebaseDatabase.getInstance().getReference("Utenti");
        myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myrv.setLayoutManager(new GridLayoutManager(this, 1));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listacani.clear();
                for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                    Dog cane = posSnapshot.getValue(Dog.class);
                    listacani.add(cane);
                    caricamento.setVisibility(View.INVISIBLE);
                }
                reverse(listacani);
                myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
                myrv.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                caricamento.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.prova,R.color.Rosso,R.color.verde);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listacani.clear();
                        for(DataSnapshot posSnapshot: dataSnapshot.getChildren())  {
                            Dog cane = posSnapshot.getValue(Dog.class);
                            listacani.add(cane);
                            caricamento.setVisibility(View.INVISIBLE);
                        }
                        reverse(listacani);
                        myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
                        myrv.setAdapter(myAdapter);
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        caricamento.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "ERRORE DATABASE", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        if (auth.getCurrentUser()!=null){
            utente=auth.getCurrentUser().getUid();
            tvemaillogin.setText(auth.getCurrentUser().getEmail());
            Query query = myRef.orderByChild("utente").equalTo(utente).limitToFirst(1);
            query.addListenerForSingleValueEvent(valueEventListener);
            Query nome = myRef2.orderByChild("email").equalTo(auth.getCurrentUser().getEmail());
            nome.addListenerForSingleValueEvent(evento);
        }
        dl = findViewById(R.id.dl);
        t = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        t.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch(id)
                {
                    case R.id.profilo:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case R.id.navpreferiti:
                        if(auth.getCurrentUser() != null)
                            startActivity(new Intent(MainActivity.this,FavouriteActivity.class));
                        else
                            Toast.makeText(MainActivity.this, "Devi aver effettuato il login.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        startActivity(new Intent(MainActivity.this, SliderActivity.class));
                        break;
                    case R.id.info:
                        startActivity(new Intent(MainActivity.this,SviluppatoriActivity.class));
                        break;
                    case R.id.feedback:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("mailto: "));
                        String[] contatto = {"baoo@engineer.com"};
                        intent.putExtra(Intent.EXTRA_EMAIL, contatto);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "BAOO:Feedback");
                        intent.putExtra(Intent.EXTRA_TEXT, "Vorrei segnalarvi il seguente disservizio: ");
                        intent.setType("message/rfc822");
                        Intent chooser = Intent.createChooser(intent, "Invia Email");
                        startActivity(chooser);
                        break;
                    case R.id.cheers:
                        String urlString = "http://paypal.me/baooincontri";
                        Intent intento = new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
                        intento.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intento.setPackage("com.android.chrome");
                        try {
                            startActivity(intento);
                        } catch (ActivityNotFoundException ex) {
                            // Chrome browser presumably not installed so allow user to choose instead
                            intento.setPackage(null);
                            startActivity(intento);
                        }
                        break;
                }
                return true;
            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren())
                {Dog cane = snap.getValue(Dog.class);
                 url=new String(cane.getThumbnail());
                 break;
                }
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(immaginelogin);
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
                tvnamelogin.setText(user.getNome()+" " + user.getCognome());
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        MenuInflater inflauto = getMenuInflater();
        inflauto.inflate(R.menu.right_menu,menu);
        MenuItem cerca=menu.findItem(R.id.app_bar_search);
        cerca.setIcon(R.drawable.ic_search);
        searchView = (SearchView)cerca.getActionView();
        searchView.setQueryHint("Cerca per nome...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intento = new Intent(MainActivity.this,FilterActivity.class);
                intento.putExtra("Name",query);
                intento.putExtra("Provenienza",true);
                startActivity(intento);
                searchView.onActionViewCollapsed();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchViewMenuItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) searchViewMenuItem.getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_search);


        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        if (item.getItemId()==R.id.filtra)
        {
                startActivity(new Intent(MainActivity.this,FilterSearchActivity.class));
                return true;
        }
        if (item.getItemId()==R.id.ordina)
        {
            conta++;
            reverse(listacani);
            myAdapter=new RecyclerViewAdapter( MainActivity.this,listacani);
            myrv.setAdapter(myAdapter);
            if(conta%2!=0) {
                Toast.makeText(this, "Ordine invertito: dal primo inserito all'ultimo", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_ascendant_sort);
            }
            else {
                Toast.makeText(this, "Ordine invertito: dall'ultimo inserito al primo", Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_discendant_sort);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        if (!searchView.isIconified())
            searchView.onActionViewCollapsed();
        else if (dl.isDrawerOpen(GravityCompat.START)) {
        dl.closeDrawer(GravityCompat.START);
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("Sei sicuro di voler chiudere Baoo?")
                    .setCancelable(false)
                    .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }
    public void Clicckino(View v){
        if (auth.getCurrentUser()==null)
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        else
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));

    }

}
