package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.Snackbar;
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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;


public class Dog_Activity extends AppCompatActivity implements OnLikeListener {
    float x1,x2,y1,y2;
    private TextView tvname, tvdescription, tvbreed, tvgender, tvcity, tvage;
    private ImageView img, imag;
    private boolean isOpen = false;
    private ConstraintSet layout1, layout2, layout3;
    private ConstraintLayout constraintLayout;
    private LikeButton likeButton;
    private Vibrator myVib;
    private FirebaseAuth auth;
    private String utentecorrente;
    private DatabaseReference myRef;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            utentecorrente = auth.getCurrentUser().getUid();
        }
        getSupportActionBar().hide(); //<< this
        myRef= FirebaseDatabase.getInstance().getReference();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_dog_);
        tvname = (TextView) findViewById(R.id.textName);
        tvdescription = (TextView) findViewById(R.id.txtDesc);
        tvbreed = (TextView) findViewById(R.id.textBreed);
        tvgender = (TextView) findViewById(R.id.textGend);
        tvcity = (TextView) findViewById(R.id.textCity);
        tvage = (TextView) findViewById(R.id.txtAge);
        img = (ImageView) findViewById(R.id.dogthumbnail);
        imag = (ImageView) findViewById(R.id.dogthumbnail2);
        likeButton = findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);
        myVib=(Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        String annio=" anni";
        // Recieve data
        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        String Breed =  intent.getExtras().getString("Breed");
        String Description = "Descrizione di " + Name + ": \n" + intent.getExtras().getString("Description");
        String Gender = intent.getExtras().getString("Gender");
        String City = "Città: " + intent.getExtras().getString("City");
        String eig = intent.getExtras().getString("Age");
        uid = intent.getExtras().getString("Uid");
        if(Integer.parseInt(eig)==1)  annio=" anno";
        String Age = "Età: " + eig + annio;
        final String Tel = intent.getExtras().getString("Tel");
        final String Email = intent.getExtras().getString("Email");
        final String image = intent.getExtras().getString("Thumbnail");
        // Setting values
        tvname.setText(Name);
        tvbreed.setText(Breed);
        tvdescription.setText(Description);
        tvgender.setText(Gender);
        tvcity.setText(City);
        tvage.setText(Age);
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.loading_prova)
                .fit()
                .centerCrop()
                .into(img);
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.loading_prova)
                .fit()
                .centerCrop()
                .into(imag);

        Button btcall = (Button) findViewById(R.id.chiama);
        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                myVib.vibrate(25);
                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + Tel));
                    startActivity(intent);

                } else
                    Snackbar.make(v,"Devi prima aver effettuato il login",Snackbar.LENGTH_SHORT);
                                       // startActivity(new Intent(Dog_Activity.this, LoginActivity.class));
            }

        });

        Button btcontact = (Button) findViewById(R.id.contatta);

        btcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                myVib.vibrate(25);

                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto: "));
                    String[] contatto = {Email};
                    intent.putExtra(Intent.EXTRA_EMAIL, contatto);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "BAOO:Incontriamoci!");
                    intent.putExtra(Intent.EXTRA_TEXT, "Il mio cane è interessato a farsela con il tuo. Vediamoci e facciamoli divertire. Bacini :**");
                    intent.setType("message/rfc822");
                    Intent chooser = Intent.createChooser(intent, "Send email");
                    startActivity(chooser);
                } else
                    Toast.makeText(context, "Devi aver effettuato il login.", Toast.LENGTH_SHORT).show();
                   // startActivity(new Intent(Dog_Activity.this, LoginActivity.class));
            }

        });

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        layout3 = new ConstraintSet();
        constraintLayout = findViewById(R.id.constraint_layout);
        layout2.clone(this, R.layout.activity_reducedprofile);
        layout1.clone(constraintLayout);
        layout3.clone(this, R.layout.activity_dog_);
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

    @Override
    public void liked(LikeButton likeButton) {

        myRef.child("Utenti").child(utentecorrente).child("Preferiti").setValue(uid);
        Toast.makeText(this, "Aggiunto nei preferiti!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        Toast.makeText(this, "Tolto dai preferiti!", Toast.LENGTH_SHORT).show();
    }
}

