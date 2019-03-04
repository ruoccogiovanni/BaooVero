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
import static com.example.giovanni.baoovero.AddActivity.provincine;

public class FilterSearchActivity extends AppCompatActivity {
    private SeekBar ftage;
    private AutoCompleteTextView ftbreed,ftcity;
    private List<PortraitDog> dogList;
    private RadioGroup groupgender;
    private RadioButton genderbutton;
    private TextView fttextage;
    private Button ftbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        setTitle("Filtra");
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
        fttextage.setText("Età: qualsiasi");
        ftage.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        if(progress==0)
                            fttextage.setText("Età: qualsiasi");
                        else if (progress==1)
                            fttextage.setText("Età: da " + progress + " anno in poi");
                        else
                            fttextage.setText("Età: da " + progress + " anni in poi");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (progress_value==0)
                            fttextage.setText("Età: qualsiasi");
                        else if (progress_value==1)
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
                PortraitDog[] a = new PortraitDog[grandezza];
                dogList.toArray(a);
                List<String> razze = new ArrayList<>();
                for (PortraitDog s : a)
                {
                    razze.add(s.getDogName());
                }
                boolean razza = false;
                int intage = ftage.getProgress();
                String filterage = String.valueOf(intage);
                int idbutton=groupgender.getCheckedRadioButtonId();
                genderbutton= findViewById(idbutton);
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
                String ftgender= genderbutton.getText().toString();
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
                if (boolprovincia&&razza)
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
                    Toast.makeText(FilterSearchActivity.this, "C'è qualcosa che non va.", Toast.LENGTH_SHORT).show();
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
        dogList.add(new PortraitDog("Akita Inu", R.drawable.image_akita));
        dogList.add(new PortraitDog("Alano", R.drawable.image_alano));
        dogList.add(new PortraitDog("Bassotto", R.drawable.image_bassotto));
        dogList.add(new PortraitDog("Beagle", R.drawable.image_beagle));
        dogList.add(new PortraitDog("Bearded Collie", R.drawable.image_beardedcollie));
        dogList.add(new PortraitDog("Border Collie", R.drawable.image_bordercollie));
        dogList.add(new PortraitDog("Boxer", R.drawable.image_boxer));
        dogList.add(new PortraitDog("Pastore della Brie", R.drawable.image_gisy));
        dogList.add(new PortraitDog("Bulldog", R.drawable.bulldog));
        dogList.add(new PortraitDog("Cane Corso",R.drawable.canecorso));
        dogList.add(new PortraitDog("Lupo Cecoslovacco",R.drawable.lupocecoslovacco));
        dogList.add(new PortraitDog("Cane da montagna dei Pirenei", R.drawable.montagnapirenei));
        dogList.add(new PortraitDog("Cane dei Faraoni",R.drawable.canedeifaraoni ));
        dogList.add(new PortraitDog("Cocker Americano", R.drawable.cockeramericano));
        dogList.add(new PortraitDog("Greyhound", R.drawable.greyhound));
        dogList.add(new PortraitDog("Levriero Afgano", R.drawable.levrieroafgano));
        dogList.add(new PortraitDog("Levriero Spagnolo",R.drawable.levrierospagnolo ));
        dogList.add(new PortraitDog("Mastino Napoletano",R.drawable.mastinonapoletano ));
        dogList.add(new PortraitDog("Pastore Bergamasco", R.drawable.pastorebergamasco));
        dogList.add(new PortraitDog("Pastore Olandese", R.drawable.pastoreolandese));
        dogList.add(new PortraitDog("Pechinese", R.drawable.pechinese));
        dogList.add(new PortraitDog("San Bernardo", R.drawable.sanbernardo));
        dogList.add(new PortraitDog("Segugio Serbo",R.drawable.segugioserbo ));
        dogList.add(new PortraitDog("Segugio Spagnolo", R.drawable.segugiospagnolo));
        dogList.add(new PortraitDog("Setter Inglese", R.drawable.setteringlese));
        dogList.add(new PortraitDog("Shar Pei", R.drawable.sharpei));
        dogList.add(new PortraitDog("Siberian Husky", R.drawable.siberianhusky));
        dogList.add(new PortraitDog("Bulldog Francese", R.drawable.image_bulldog));
        dogList.add(new PortraitDog("Carlino", R.drawable.image_carlino));
        dogList.add(new PortraitDog("Chihuahua", R.drawable.image_chiuaua));
        dogList.add(new PortraitDog("Chow chow", R.drawable.image_chowchow));
        dogList.add(new PortraitDog("Dalmata", R.drawable.image_dalmata));
        dogList.add(new PortraitDog("Labrador Retriever", R.drawable.image_labrador));
        dogList.add(new PortraitDog("Maltese", R.drawable.image_maltese));
        dogList.add(new PortraitDog("Meticcio", R.drawable.image_meticcio));
        dogList.add(new PortraitDog("Pastore Tedesco", R.drawable.image_pastoretedesco));
        dogList.add(new PortraitDog("American Pit Bull Terrier", R.drawable.image_pitbull));
        dogList.add(new PortraitDog("Golden Retriever", R.drawable.image_retriever));
        dogList.add(new PortraitDog("Rottweiler", R.drawable.image_rottweiler));
        dogList.add(new PortraitDog("Jack Russell Terrier", R.drawable.image_russell));
        dogList.add(new PortraitDog("Schnauzer Nano", R.drawable.image_shnauzer));
        dogList.add(new PortraitDog("Shiba Inu", R.drawable.image_shiba));
        dogList.add(new PortraitDog("Volpino Italiano", R.drawable.image_volpino));
        dogList.add(new PortraitDog("West Highland White Terrier", R.drawable.image_olivi));
        dogList.add(new PortraitDog("Yorkshire Terrier", R.drawable.image_yorkshire));
    }

    public void ftclick(View v)
    {
        int idbutton=groupgender.getCheckedRadioButtonId();
        genderbutton=(RadioButton)findViewById(idbutton);

    }
}
