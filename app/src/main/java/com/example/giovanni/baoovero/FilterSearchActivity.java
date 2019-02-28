package com.example.giovanni.baoovero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import static com.example.giovanni.baoovero.Add_Activity.provincine;

public class FilterSearchActivity extends AppCompatActivity {
    private SeekBar ftage;
    private AutoCompleteTextView ftbreed,ftcity;
    private List<Portrait_Dog> dogList;
    private RadioGroup groupgender;
    private RadioButton genderbutton;
    private TextView fttextage;
    private Button ftbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        Toast.makeText(this, "Il campo sesso è obbligatorio.", Toast.LENGTH_SHORT).show();
        groupgender=(RadioGroup)findViewById(R.id.filter_gender);
        ftage = (SeekBar) findViewById(R.id.ft_seek_age);
        fttextage = (TextView) findViewById(R.id.ft_age);
        ftbutton = (Button)findViewById(R.id.ft_button);
        ftcity = (AutoCompleteTextView) findViewById(R.id.ft_citta);
        ArrayAdapter<String> adattatore = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,provincine);
        ftcity.setAdapter(adattatore);
        ftcity.setDropDownVerticalOffset(-100);
        final int agemax = 20;
        ftage.setMax(agemax);
        fttextage.setText("Età: da " + ftage.getProgress() + " anni in poi");
        ftage.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        if (progress==1)
                            fttextage.setText("Età: da " + progress + " anno in poi");
                        else
                            fttextage.setText("Età: da " + progress + " anni in poi");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (progress_value==1)
                            fttextage.setText("Età: da " + progress_value + " anno in poi");
                        else
                            fttextage.setText("Età: da " + progress_value + " anni in poi");
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
                int grandezza = dogList.size();
                Portrait_Dog[] a = new Portrait_Dog[grandezza];
                dogList.toArray(a);
                List<String> razze = new ArrayList<>();
                for (Portrait_Dog s : a)
                {
                    razze.add(s.getDogName());
                }
                boolean razza = false;
                int intage = ftage.getProgress();
                String filterage = String.valueOf(intage);
                int idbutton=groupgender.getCheckedRadioButtonId();
                genderbutton= findViewById(idbutton);
                boolean sesso=false;
                boolean boolprovincia=false;
                String filterbreed = ftcmpltbreed.getText().toString().trim();
                if (!filterbreed.isEmpty()) {
                    for (String s : razze) {
                        if (filterbreed.equalsIgnoreCase(s)) {
                            razza = true;
                            break;
                        }
                    }
                }
                else
                    razza = true;
                String ftgender="Maschio";
                try {
                    ftgender= genderbutton.getText().toString();
                    sesso=true;
                }
                catch (NullPointerException e)
                {
                }

                if (!provincia.isEmpty()) {
                    for (String provinciona : provincine) {
                        if (provincia.equalsIgnoreCase(provinciona)) {
                            boolprovincia = true;
                            break;
                        }
                    }
                }
                else
                    boolprovincia=true;
                if (boolprovincia&&sesso&&razza)
                {
                    Intent intent = new Intent(FilterSearchActivity.this,FilterActivity.class);
                    intent.putExtra("Gender",ftgender);
                    intent.putExtra("Breed",filterbreed);
                    intent.putExtra("Age",filterage);
                    intent.putExtra("City",provincia);
                    intent.putExtra("Provenienza",false);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(FilterSearchActivity.this, "C'è qualcosa che non va. Sei sicuro di aver inserito almeno il sesso?", Toast.LENGTH_SHORT).show();
                    if (!razza)
                    {
                        ftcmpltbreed.setHintTextColor(getResources().getColor(R.color.error_color));
                        ftcmpltbreed.setText("");
                    }
                    if (!boolprovincia)
                    {
                        ftcity.setHintTextColor(getResources().getColor(R.color.error_color));
                        ftcity.setText("");
                    }
                }
            }
        });
    }

    private void fillDogList() {
        dogList = new ArrayList<>();
        dogList.add(new Portrait_Dog("Akita Inu", R.drawable.image_akita));
        dogList.add(new Portrait_Dog("Alano", R.drawable.image_alano));
        dogList.add(new Portrait_Dog("Bassotto", R.drawable.image_bassotto));
        dogList.add(new Portrait_Dog("Beagle", R.drawable.image_beagle));
        dogList.add(new Portrait_Dog("Bearded Collie", R.drawable.image_beardedcollie));
        dogList.add(new Portrait_Dog("Border Collie", R.drawable.image_bordercollie));
        dogList.add(new Portrait_Dog("Boxer", R.drawable.image_boxer));
        dogList.add(new Portrait_Dog("Pastore della Brie", R.drawable.image_gisy));
        dogList.add(new Portrait_Dog("Bulldog", R.drawable.bulldog));
        dogList.add(new Portrait_Dog("Cane Corso",R.drawable.canecorso));
        dogList.add(new Portrait_Dog("Lupo Cecoslovacco",R.drawable.lupocecoslovacco));
        dogList.add(new Portrait_Dog("Cane da montagna dei Pirenei", R.drawable.montagnapirenei));
        dogList.add(new Portrait_Dog("Cane dei Faraoni",R.drawable.canedeifaraoni ));
        dogList.add(new Portrait_Dog("Cocker Americano", R.drawable.cockeramericano));
        dogList.add(new Portrait_Dog("Greyhound", R.drawable.greyhound));
        dogList.add(new Portrait_Dog("Levriero Afgano", R.drawable.levrieroafgano));
        dogList.add(new Portrait_Dog("Levriero Spagnolo",R.drawable.levrierospagnolo ));
        dogList.add(new Portrait_Dog("Mastino Napoletano",R.drawable.mastinonapoletano ));
        dogList.add(new Portrait_Dog("Pastore Bergamasco", R.drawable.pastorebergamasco));
        dogList.add(new Portrait_Dog("Pastore Olandese", R.drawable.pastoreolandese));
        dogList.add(new Portrait_Dog("Pechinese", R.drawable.pechinese));
        dogList.add(new Portrait_Dog("San Bernardo", R.drawable.sanbernardo));
        dogList.add(new Portrait_Dog("Segugio Serbo",R.drawable.segugioserbo ));
        dogList.add(new Portrait_Dog("Segugio Spagnolo", R.drawable.segugiospagnolo));
        dogList.add(new Portrait_Dog("Setter Inglese", R.drawable.setteringlese));
        dogList.add(new Portrait_Dog("Shar Pei", R.drawable.sharpei));
        dogList.add(new Portrait_Dog("Siberian Husky", R.drawable.siberianhusky));
        dogList.add(new Portrait_Dog("Bulldog Francese", R.drawable.image_bulldog));
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
        dogList.add(new Portrait_Dog("Jack Russell Terrier", R.drawable.image_russell));
        dogList.add(new Portrait_Dog("Schnauzer Nano", R.drawable.image_shnauzer));
        dogList.add(new Portrait_Dog("Shiba Inu", R.drawable.image_shiba));
        dogList.add(new Portrait_Dog("Volpino Italiano", R.drawable.image_volpino));
        dogList.add(new Portrait_Dog("West Highland White Terrier", R.drawable.image_olivi));
        dogList.add(new Portrait_Dog("Yorkshire Terrier", R.drawable.image_yorkshire));
    }

    public void ftclick(View v)
    {
        int idbutton=groupgender.getCheckedRadioButtonId();
        genderbutton=(RadioButton)findViewById(idbutton);

    }
}
