package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class ProfileDog extends AppCompatActivity {
    float x1,x2,y1,y2;
    private TextView tvname, tvdescription, tvbreed, tvgender, tvcity, tvage;
    private ImageView img, imag;
    private boolean isOpen = false;
    private ConstraintSet layout1, layout2, layout3;
    private ConstraintLayout constraintLayout;
    private Vibrator myVib;
    private FirebaseAuth auth;
    private Button btmodifica;
    private Button btelimina;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide(); //<< this
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_dog);
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        tvname = (TextView) findViewById(R.id.profile_name);
        tvname.setSelected(true);
        tvdescription = (TextView) findViewById(R.id.profile_desc);
        tvbreed = (TextView) findViewById(R.id.profile_breed);
        tvbreed.setSelected(true);
        tvgender = (TextView) findViewById(R.id.profile_gender);
        tvcity = (TextView) findViewById(R.id.profile_city);
        tvage = (TextView) findViewById(R.id.profile_age);
        img = (ImageView) findViewById(R.id.profiledogimg);
        imag = (ImageView) findViewById(R.id.profiledogimg2);
        myVib=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        String annio=" anni";
        // Recieve data
        final Intent intent = getIntent();
        final String Name = intent.getExtras().getString("Name");
        final String Breed =  intent.getExtras().getString("Breed");
        final String Description = intent.getExtras().getString("Description");
        String descrizione="Descrizione di " + Name + ": \n" + Description;
        final String Gender = intent.getExtras().getString("Gender");
        final String City = intent.getExtras().getString("City");
        String citta= "Città: " + City;
        final String eig = intent.getExtras().getString("Age");
        if(Integer.parseInt(eig)==1)  annio=" anno";
        final String Age = "Età: " + eig + annio;
        final String Tel = intent.getExtras().getString("Tel");
        final String Email = intent.getExtras().getString("Email");
        final String image = intent.getExtras().getString("Thumbnail");
        final String Uid = intent.getExtras().getString("Uid");
        final String utente=auth.getCurrentUser().getUid();
        // Setting values
        tvname.setText(Name);
        tvbreed.setText(Breed);
        tvdescription.setText(descrizione);
        tvgender.setText(Gender);
        tvcity.setText(citta);
        tvage.setText(Age);

        switch (Breed){
            case "Akita Inu":
                imag.setImageResource(R.drawable.image_akita);
                break;
            case "Alano":
                imag.setImageResource(R.drawable.image_alano);
                break;
            case "Bassotto":
                imag.setImageResource(R.drawable.image_bassotto);
                break;
            case "Bearded Collie":
                imag.setImageResource(R.drawable.image_beardedcollie);
                break;
            case "Cane Corso":
                imag.setImageResource(R.drawable.canecorso);
                break;
            case "Lupo Cecoslovacco":
                imag.setImageResource(R.drawable.lupocecoslovacco);
                break;
            case "Cane da montagna dei Pirenei":
                imag.setImageResource(R.drawable.montagnapirenei);
                break;
            case "Cane dei Faraoni":
                imag.setImageResource(R.drawable.canedeifaraoni);
                break;
            case "Cocker Americano":
                imag.setImageResource(R.drawable.cockeramericano);
                break;
            case "Beagle":
                imag.setImageResource(R.drawable.image_beagle);
                break;
            case "Levriero Afgano":
                imag.setImageResource(R.drawable.levrieroafgano);
                break;
            case "Levriero Spagnolo":
                imag.setImageResource(R.drawable.levrierospagnolo);
                break;
            case "Mastino Napoletano":
                imag.setImageResource(R.drawable.mastinonapoletano);
                break;
            case "Pastore Bergamasco":
                imag.setImageResource(R.drawable.pastorebergamasco);
                break;
            case "Pastore Olandese":
                imag.setImageResource(R.drawable.pastoreolandese);
                break;
            case "San Bernardo":
                imag.setImageResource(R.drawable.sanbernardo);
                break;
            case "Segugio Spagnolo":
                imag.setImageResource(R.drawable.segugiospagnolo);
                break;
            case "Shar Pei":
                imag.setImageResource(R.drawable.sharpei);
                break;
            case "Siberian Husky":
                imag.setImageResource(R.drawable.siberianhusky);
                break;
            case "Bulldog Francese":
                imag.setImageResource(R.drawable.bulldogfrancese);
                break;
            case "Carlino":
                imag.setImageResource(R.drawable.image_carlino);
                break;
            case "Chihuahua":
                imag.setImageResource(R.drawable.image_chiuaua);
                break;
            case "Dalmata":
                imag.setImageResource(R.drawable.image_dalmata);
                break;
            case "Labrador Retriever":
                imag.setImageResource(R.drawable.image_labrador);
                break;
            case "Maltese":
                imag.setImageResource(R.drawable.image_maltese);
                break;
            case "Pastore Tedesco":
                imag.setImageResource(R.drawable.image_pastoretedesco);
                break;
            case "American Pit Bull Terrier":
                imag.setImageResource(R.drawable.image_pitbull);
                break;
            case "Jack Russell Terrier":
                imag.setImageResource(R.drawable.image_russell);
                break;
            case "Schnauzer Nano":
                imag.setImageResource(R.drawable.image_shnauzer);
                break;
            case "Shiba Inu":
                imag.setImageResource(R.drawable.image_shiba);
                break;
            case "Volpino Italiano":
                imag.setImageResource(R.drawable.image_volpino);
                break;
            case "West Highland White Terrier":
                imag.setImageResource(R.drawable.image_olivi);
                break;
            case "Pastore della Brie":
                imag.setImageResource(R.drawable.image_gisy);
                break;
            case "Chow chow":
                imag.setImageResource(R.drawable.image_chowchow);
                break;
            case "Border Collie":
                imag.setImageResource(R.drawable.image_bordercollie);
                break;
            case "Boxer":
                imag.setImageResource(R.drawable.image_boxer);
                break;
            case "Greyhound":
                imag.setImageResource(R.drawable.greyhound);
                break;
            case "Bulldog":
                imag.setImageResource(R.drawable.image_bulldog);
                break;
            case "Segugio Serbo":
                imag.setImageResource(R.drawable.segugioserbo);
                break;
            case "Setter Inglese":
                imag.setImageResource(R.drawable.setteringlese);
                break;
            case "Meticcio":
                imag.setImageResource(R.drawable.image_meticcio);
                break;
            case "Rottweiler":
                imag.setImageResource(R.drawable.image_rottweiler);
                break;
            case "Yorkshire Terrier":
                imag.setImageResource(R.drawable.image_yorkshire);
                break;
            case "Golden Retriever":
                imag.setImageResource(R.drawable.image_retriever);
                break;
            case "Pechinese":
                imag.setImageResource(R.drawable.pechinese);
                break;
            default:
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.roundloading)
                        .fit()
                        .centerCrop()
                        .into(imag);
        }
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.loading_prova)
                .fit()
                .centerCrop()
                .into(img);
        btmodifica = (Button) findViewById(R.id.modifica);
        btmodifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                myVib.vibrate(25);
               Intent intento = new Intent(ProfileDog.this,EditActivity.class);
               intento.putExtra("Name",Name);
               intento.putExtra("Breed",Breed);
               intento.putExtra("Description",Description);
               intento.putExtra("Gender",Gender);
               intento.putExtra("City",City);
               intento.putExtra("Age",eig);
               intento.putExtra("Tel",Tel);
               intento.putExtra("Image",image);
               intento.putExtra("Uid",Uid);
               intento.putExtra("Utente",utente);
               startActivity(intento);
            }

        });

        btelimina = (Button) findViewById(R.id.elimina);
        btelimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVib.vibrate(25);
                new AlertDialog.Builder(ProfileDog.this)
                        .setMessage("Sei sicuro di voler eliminare " + Name +"?")
                        .setCancelable(false)
                        .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myRef.child(Uid).setValue(null);
                                startActivity(new Intent(ProfileDog.this, ProfileActivity.class) );
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

        });

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        layout3 = new ConstraintSet();
        constraintLayout = findViewById(R.id.constraint_profile);
        layout2.clone(this, R.layout.activity_reduced_dog);
        layout1.clone(constraintLayout);
        layout3.clone(this, R.layout.activity_profile_dog);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(y1>y2){
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }else if(y1 < y2){
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout3.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }
                break;
        }
        return false;
    }
    
}

