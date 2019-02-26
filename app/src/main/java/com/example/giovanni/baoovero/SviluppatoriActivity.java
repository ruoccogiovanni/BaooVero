package com.example.giovanni.baoovero;

import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.luolc.emojirain.EmojiRainLayout;

public class SviluppatoriActivity extends AppCompatActivity {
    private int numero;
    private ImageView immagine;
    private EmojiRainLayout contenitore;
    private Vibrator myVib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sviluppatori);
        numero = 0;
        myVib=(Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        immagine = (ImageView) findViewById(R.id.developerimage);
        contenitore = (EmojiRainLayout) findViewById(R.id.emoji);


    }

    public void developerclick(View v) {
        numero++;
        if (numero % 3 == 0 )
        {
            Snackbar.make(v,"WAAOOOOOOOOOOOOOOO",Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.error_color)).setAction("Fermati ti prego", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contenitore.stopDropping();
                }
            }).show();
            myVib.vibrate(100);
            contenitore.addEmoji(R.drawable.beardedcollie);
            contenitore.addEmoji(R.drawable.bordercollie);
            contenitore.addEmoji(R.drawable.bulldog);
            contenitore.addEmoji(R.drawable.canecorso);
            contenitore.addEmoji(R.drawable.canedeifaraoni);
            contenitore.addEmoji(R.drawable.cockeramericano);
            contenitore.addEmoji(R.drawable.image_akita);
            contenitore.addEmoji(R.drawable.image_alano);
            contenitore.addEmoji(R.drawable.image_bassotto);
            contenitore.addEmoji(R.drawable.image_beagle);
            contenitore.addEmoji(R.drawable.image_boxer);
            contenitore.addEmoji(R.drawable.image_bulldog);
            contenitore.addEmoji(R.drawable.image_carlino);
            contenitore.addEmoji(R.drawable.image_chiuaua);
            contenitore.stopDropping();
            contenitore.setPer(10);
            contenitore.setDuration(7200);
            contenitore.setDropDuration(2400);
            contenitore.setDropFrequency(500);
            contenitore.startDropping();

        }


    }
}