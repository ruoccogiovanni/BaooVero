package com.example.giovanni.baoovero;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.luolc.emojirain.EmojiRainLayout;

public class SviluppatoriActivity extends AppCompatActivity {
    private int numero;
    private ImageView immagine;
    private EmojiRainLayout contenitore;
    private Vibrator myVib;
    private TextView dev1,dev2,dev3;
    MediaPlayer song;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sviluppatori);
        setTitle("Sviluppatori");
        song = MediaPlayer.create(SviluppatoriActivity.this, R.raw.shooting_stars);
        dev1=(TextView)findViewById(R.id.testodeveloper);
        dev2=(TextView)findViewById(R.id.testodeveloper2);
        dev3=(TextView)findViewById(R.id.testodev3);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constraint_svil);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.uait));
        numero = 0;
        myVib=(Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        immagine = (ImageView) findViewById(R.id.developerimage);
        contenitore = (EmojiRainLayout) findViewById(R.id.emoji);
    }

    public void developerclick(View v) {
        numero++;
        if (numero % 3 == 0 )
        {
            song.start();
            Snackbar.make(v,"COSA STA SUCCEDENDO",Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.error_color)).setAction("Fermati ti prego", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contenitore.stopDropping();
                    dev1.setVisibility(View.VISIBLE);
                    dev2.setVisibility(View.VISIBLE);
                    dev3.setVisibility(View.VISIBLE);
                    immagine.setVisibility(View.VISIBLE);
                    if (song.isPlaying())
                    {
                        song.pause();
                        song.seekTo(0);
                    }
                }
            }).show();
            myVib.vibrate(100);
            contenitore.addEmoji(R.drawable.image_beardedcollie);
            contenitore.addEmoji(R.drawable.image_bordercollie);
            contenitore.addEmoji(R.drawable.bulldog);
            contenitore.addEmoji(R.drawable.canecorso);
            contenitore.addEmoji(R.drawable.canedeifaraoni);
            contenitore.addEmoji(R.drawable.cockeramericano);
            contenitore.addEmoji(R.drawable.image_akita);
            contenitore.addEmoji(R.drawable.image_alano);
            contenitore.addEmoji(R.drawable.image_maremmano);
            contenitore.addEmoji(R.drawable.image_american_terrier);
            contenitore.addEmoji(R.drawable.image_beagle);
            contenitore.addEmoji(R.drawable.image_boxer);
            contenitore.addEmoji(R.drawable.image_bulldog);
            contenitore.addEmoji(R.drawable.image_carlino);
            contenitore.addEmoji(R.drawable.image_chiuaua);
            contenitore.addEmoji(R.drawable.image_chowchow);
            contenitore.addEmoji(R.drawable.image_dalmata);
            contenitore.addEmoji(R.drawable.image_labrador);
            contenitore.addEmoji(R.drawable.image_barboncino);
            contenitore.addEmoji(R.drawable.image_maltese);
            contenitore.addEmoji(R.drawable.image_chowchow);
            contenitore.addEmoji( R.drawable.image_akita);
            contenitore.addEmoji( R.drawable.image_alano);
            contenitore.addEmoji( R.drawable.image_bassotto);
            contenitore.addEmoji( R.drawable.image_american_terrier);
            contenitore.addEmoji( R.drawable.image_beagle);
            contenitore.addEmoji( R.drawable.image_beardedcollie);
            contenitore.addEmoji( R.drawable.image_bordercollie);
            contenitore.addEmoji(R.drawable.image_boxer);
            contenitore.addEmoji( R.drawable.image_gisy);
            contenitore.addEmoji( R.drawable.bulldog);
            contenitore.addEmoji(R.drawable.canecorso);
            contenitore.addEmoji(R.drawable.lupocecoslovacco);
            contenitore.addEmoji( R.drawable.montagnapirenei);
            contenitore.addEmoji(R.drawable.canedeifaraoni );
            contenitore.addEmoji(R.drawable.cockeramericano);
            contenitore.addEmoji(R.drawable.greyhound);
            contenitore.addEmoji(R.drawable.levrieroafgano);
            contenitore.addEmoji(R.drawable.levrierospagnolo );
            contenitore.addEmoji(R.drawable.mastinonapoletano );
            contenitore.addEmoji(R.drawable.pastorebergamasco);
            contenitore.addEmoji(R.drawable.pastoreolandese);
            contenitore.addEmoji( R.drawable.pechinese);
            contenitore.addEmoji(R.drawable.sanbernardo);
            contenitore.addEmoji(R.drawable.segugioserbo );
            contenitore.addEmoji( R.drawable.segugiospagnolo);
            contenitore.addEmoji(R.drawable.setteringlese);
            contenitore.addEmoji(R.drawable.sharpei);
            contenitore.addEmoji(R.drawable.siberianhusky);
            contenitore.addEmoji(R.drawable.image_bulldog);
            contenitore.addEmoji(R.drawable.image_carlino);
            contenitore.addEmoji(R.drawable.image_pastoretedesco);
            contenitore.addEmoji( R.drawable.image_pitbull);
            contenitore.addEmoji( R.drawable.image_retriever);
            contenitore.addEmoji( R.drawable.image_rottweiler);
            contenitore.addEmoji( R.drawable.image_russell);
            contenitore.addEmoji( R.drawable.image_shnauzer);
            contenitore.addEmoji(R.drawable.image_shiba);
            contenitore.addEmoji( R.drawable.image_volpino);
            contenitore.addEmoji( R.drawable.image_olivi);
            contenitore.addEmoji( R.drawable.image_yorkshire);
            contenitore.addEmoji(R.drawable.image_meticcio);

            contenitore.stopDropping();
            dev1.setVisibility(View.INVISIBLE);
            dev2.setVisibility(View.INVISIBLE);
            dev3.setVisibility(View.INVISIBLE);
            immagine.setVisibility(View.INVISIBLE);
            contenitore.setPer(10);
            contenitore.setDuration(13000);
            contenitore.setDropDuration(2400);
            contenitore.setDropFrequency(500);
            contenitore.startDropping();



            song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    dev1.setVisibility(View.VISIBLE);
                    dev2.setVisibility(View.VISIBLE);
                    dev3.setVisibility(View.VISIBLE);
                    immagine.setVisibility(View.VISIBLE);
                }
            });


        }
    }




    public void onBackPressed(){
        if (song.isPlaying())
        {
            song.pause();
            song.seekTo(0);
        }
        startActivity(new Intent(SviluppatoriActivity.this, MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}