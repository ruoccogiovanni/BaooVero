package com.example.giovanni.baoovero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {
    private SeekBar ftage;
    private AutoCompleteTextView ftbreed;
    private List<Portrait_Dog> dogList;
    private RadioGroup groupgender;
    private RadioButton genderbutton;
    private TextView fttextage;
    private EditText ftcity;
    private Button ftbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        groupgender=(RadioGroup)findViewById(R.id.filter_gender);
        ftage = (SeekBar) findViewById(R.id.ft_seek_age);
        fttextage = (TextView) findViewById(R.id.ft_age);
        ftbutton = (Button)findViewById(R.id.ft_button);
        ftcity = (EditText) findViewById(R.id.ft_citta);
        final int agemax = 20;
        ftage.setMax(agemax);
        fttextage.setText("Età: " + ftage.getProgress());
        ftage.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        fttextage.setText("Età: " + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        fttextage.setText("Età: " + progress_value);
                    }
                }
        );
        fillDogList();
        final AutoCompleteTextView ftcmpltbreed = findViewById(R.id.ft_cmplt_breed);
        AutoCompletePortrait adapter = new AutoCompletePortrait(this, dogList);
        ftcmpltbreed.setAdapter(adapter);

        ftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] provincine = {"Agrigento","Alessandria","Ancona","Aosta","L'Aquila","Arezzo","Ascoli-Piceno","Asti","Avellino","Bari","Barletta-Andria-Trani","Belluno","Benevento","Bergamo","Biella","Bologna","Bolzano","Brescia","Brindisi","Cagliari","Caltanissetta","Campobasso","Carbonia Iglesias","Caserta","Catania","Catanzaro","Chieti","Como","Cosenza","Cremona","Crotone","Cuneo","Enna","Fermo","Ferrara","Firenze","Foggia","Forli-Cesena","Frosinone","Genova","Gorizia","Grosseto","Imperia","Isernia","La-Spezia","Latina","Lecce","Lecco","Livorno","Lodi","Lucca","Macerata","Mantova","Massa-Carrara","Matera","Medio Campidano","Messina","Milano","Modena","Monza-Brianza","Napoli","Novara","Nuoro","Ogliastra","Olbia Tempio","Oristano","Padova","Palermo","Parma","Pavia","Perugia","Pesaro-Urbino","Pescara","Piacenza","Pisa","Pistoia","Pordenone","Potenza","Prato","Ragusa","Ravenna","Reggio-Calabria","Reggio-Emilia","Rieti","Rimini","Roma","Rovigo","Salerno","Sassari","Savona","Siena","Siracusa","Sondrio","Taranto","Teramo","Terni","Torino","Trapani","Trento","Treviso","Trieste","Udine","Varese","Venezia","Verbania","Vercelli","Verona","Vibo-Valentia","Vicenza","Viterbo"};
                String provincia = ftcity.getText().toString().trim();
                boolean boolprovincia=false;
                for (String provinciona:provincine)
                {
                    if (provincia.equalsIgnoreCase(provinciona)) {
                        boolprovincia = true;
                        break;
                    }
                }
                if (boolprovincia)
                {
                    Toast.makeText(FilterSearchActivity.this, "Ecco i cani cercati.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FilterSearchActivity.this, FilterActivity.class));
                }
                else
                    Toast.makeText(FilterSearchActivity.this, "C'è qualcosa che non va. Sicuro di aver inserito tutto?", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fillDogList() {
        dogList = new ArrayList<>();
        dogList.add(new Portrait_Dog("Akita Inu", R.drawable.image_akita));
        dogList.add(new Portrait_Dog("Alano", R.drawable.image_alano));
        dogList.add(new Portrait_Dog("Bassotto", R.drawable.image_bassotto));
        dogList.add(new Portrait_Dog("Beagle", R.drawable.image_beagle));
        dogList.add(new Portrait_Dog("Boxer", R.drawable.image_boxer));
        dogList.add(new Portrait_Dog("Briard", R.drawable.image_gisy));
        dogList.add(new Portrait_Dog("Bulldog", R.drawable.image_bulldog));
        dogList.add(new Portrait_Dog("Carlino", R.drawable.image_carlino));
        dogList.add(new Portrait_Dog("Chihuahua", R.drawable.image_chiuaua));
        dogList.add(new Portrait_Dog("Chow chow", R.drawable.image_chowchow));
        dogList.add(new Portrait_Dog("Dalmata", R.drawable.image_dalmata));
        dogList.add(new Portrait_Dog("Labrador Retriever", R.drawable.image_labrador));
        dogList.add(new Portrait_Dog("Maltese", R.drawable.image_maltese));
        dogList.add(new Portrait_Dog("Meticcio", R.drawable.image_meticcio));
        dogList.add(new Portrait_Dog("Pastore Tedesco", R.drawable.image_pastoretedesco));
        dogList.add(new Portrait_Dog("American Pit Bull Terrier", R.drawable.image_pitbull));
        dogList.add(new Portrait_Dog("Golden Retriever", R.drawable.image_retriever));
        dogList.add(new Portrait_Dog("Rottweiler", R.drawable.image_rottweiler));
        dogList.add(new Portrait_Dog("Jack Russell Terrier", R.drawable.image_russel));
        dogList.add(new Portrait_Dog("Schnauzer Nano", R.drawable.image_shnauzer));
        dogList.add(new Portrait_Dog("Shiba Inu", R.drawable.image_shiba));
        dogList.add(new Portrait_Dog("Volpino", R.drawable.image_volpino));
        dogList.add(new Portrait_Dog("West Highland White Terrier", R.drawable.image_westie));
        dogList.add(new Portrait_Dog("Yorkshire", R.drawable.image_yorkshire));
    }

    public void ftclick(View v)
    {
        int idbutton=groupgender.getCheckedRadioButtonId();
        genderbutton=(RadioButton)findViewById(idbutton);

    }
}
