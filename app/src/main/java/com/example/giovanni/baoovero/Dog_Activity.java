package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Dog_Activity extends AppCompatActivity {

    private TextView tvname, tvdescription, tvbreed, tvgender, tvcity, tvage;
    private ImageView img, imag;
    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_);

        tvname = (TextView) findViewById(R.id.textName);
        tvdescription = (TextView) findViewById(R.id.txtDesc);
        tvbreed = (TextView) findViewById(R.id.textBreed);
        tvgender = (TextView) findViewById(R.id.textGend);
        tvcity = (TextView) findViewById(R.id.textCity);
        tvage = (TextView) findViewById(R.id.txtAge);
        img = (ImageView) findViewById(R.id.dogthumbnail);
        imag = (ImageView) findViewById(R.id.dogthumbnail2);

        // Recieve data
        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        String Breed = "Razza: " + intent.getExtras().getString("Breed");
        String Description = intent.getExtras().getString("Description");
        String Gender = "Sesso: " + intent.getExtras().getString("Gender");
        String City = intent.getExtras().getString("City");
        String Age = intent.getExtras().getString("Age");
        final String Tel = intent.getExtras().getString("Tel");
        final String Email = intent.getExtras().getString("Email");
        int image = intent.getExtras().getInt("Thumbnail");
       // int imagge = intent.getExtras().getInt("Thumbnail");

        // Setting values

        tvname.setText(Name);
        tvbreed.setText(Breed);
        tvdescription.setText(Description);
        tvgender.setText(Gender);
        tvcity.setText(City);
        tvage.setText(Age);
        img.setImageResource(image);
        imag.setImageResource(image);

        Button btcall = (Button) findViewById(R.id.chiama);

        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                Boolean PROVA = true;
                if (PROVA) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + Tel));
                    startActivity(intent);
                } else

                    Toast.makeText(context, "STRUNZZ", Toast.LENGTH_SHORT).show();
            }

        });

        Button btcontact = (Button) findViewById(R.id.contatta);

        btcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                Boolean PROVA = true;
                if (PROVA) {
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

                    Toast.makeText(context, "STRUNZZ", Toast.LENGTH_SHORT).show();
            }

        });

        //Window w = getWindow();
        //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        constraintLayout = findViewById(R.id.constraint_layout);
        layout2.clone(this, R.layout.activity_expandedprofile);
        layout1.clone(constraintLayout);

        imag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(Dog_Activity.this, "Cliccato",Toast.LENGTH_SHORT).show();

                if (!isOpen) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen;
                } else {

                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = !isOpen;

                }


            }
        });
    }
}

