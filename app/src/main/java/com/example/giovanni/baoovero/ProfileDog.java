package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide(); //<< this
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_profile_dog);

        tvname = (TextView) findViewById(R.id.profile_name);
        tvdescription = (TextView) findViewById(R.id.profile_desc);
        tvbreed = (TextView) findViewById(R.id.profile_breed);
        tvgender = (TextView) findViewById(R.id.profile_gender);
        tvcity = (TextView) findViewById(R.id.profile_city);
        tvage = (TextView) findViewById(R.id.profile_age);
        img = (ImageView) findViewById(R.id.profiledogimg);
        imag = (ImageView) findViewById(R.id.profiledogimg2);
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
                .placeholder(R.drawable.loading_dog)
                .fit()
                .centerCrop()
                .into(img);
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.loading_dog)
                .fit()
                .centerCrop()
                .into(imag);

        btmodifica = (Button) findViewById(R.id.modifica);
        btmodifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                myVib.vibrate(25);
               //

                Toast.makeText(ProfileDog.this, "Modificato", Toast.LENGTH_SHORT).show();
                
                //
            }

        });

        btelimina = (Button) findViewById(R.id.elimina);

        btelimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                myVib.vibrate(25);

                //
                Toast.makeText(ProfileDog.this, "Eliminato", Toast.LENGTH_SHORT).show();
                //
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

