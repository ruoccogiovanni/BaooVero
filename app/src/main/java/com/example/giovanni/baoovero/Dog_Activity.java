package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Dog_Activity extends AppCompatActivity {

    private TextView tvname, tvdescription, tvbreed, tvgender, tvcity, tvage;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_);

        tvname = (TextView) findViewById(R.id.txtname);
        tvdescription = (TextView) findViewById(R.id.txtDesc);
        tvbreed = (TextView) findViewById(R.id.txtBreed);
        tvgender = (TextView) findViewById(R.id.txtGend);
        tvcity = (TextView) findViewById(R.id.txtCity);
        tvage = (TextView) findViewById(R.id.txtAge);
        img = (ImageView) findViewById(R.id.dogthumbnail);

        // Recieve data
        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        String Breed = "Razza: " + intent.getExtras().getString("Breed");
        String Description = intent.getExtras().getString("Description");
        String Gender = "Sesso: " + intent.getExtras().getString("Gender");
        String City = intent.getExtras().getString("City");
        String Age =  intent.getExtras().getString("Age");
        final String Tel =  intent.getExtras().getString("Tel");
        final String Email = intent.getExtras().getString("Email");
        int image = intent.getExtras().getInt("Thumbnail") ;


        // Setting values

        tvname.setText(Name);
        tvbreed.setText(Breed);
        tvdescription.setText(Description);
        tvgender.setText(Gender);
        tvcity.setText(City);
        tvage.setText(Age);
        img.setImageResource(image);


        Button btcall = (Button) findViewById(R.id.chiama);

        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                Boolean PROVA = true;
                if(PROVA) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+Tel));
                    startActivity(intent);
                }
                else

                    Toast.makeText(context,"STRUNZZ",Toast.LENGTH_SHORT).show();
            }

        });

        Button btcontact = (Button) findViewById(R.id.contatta);

        btcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                Boolean PROVA = true;
                if(PROVA) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto: "));
                    String [] contatto = {Email};
                    intent.putExtra(Intent.EXTRA_EMAIL,contatto);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"BAOO:Incontriamoci!");
                    intent.putExtra(Intent.EXTRA_TEXT,"Il mio cane è interessato a farsela con il tuo. Vediamoci e facciamoli divertire. Bacini :**");
                    intent.setType("message/rfc822");
                    Intent chooser = Intent.createChooser(intent, "Send email");
                    startActivity(chooser);
                }
                else

                    Toast.makeText(context,"STRUNZZ",Toast.LENGTH_SHORT).show();
            }

        });







    }
}

