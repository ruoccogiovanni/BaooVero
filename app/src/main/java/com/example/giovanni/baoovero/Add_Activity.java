package com.example.giovanni.baoovero;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;

public class Add_Activity extends AppCompatActivity {
    ImageView immagineviewID;
    private Button addimmagine;
    private Button btnavanti;
    private Button btnindietro;
    private SeekBar sbage;
    private TextView tvage;
    private AutoCompleteTextView actvbreed;
    private List<Portrait_Dog> dogList;
    private RadioGroup rgroup;
    private RadioButton rbutton;
    static final int REQUEST_CAMERA = 1;
    Integer SELECT_FILE=0;
    private EditText etname;
    private EditText etemail;
    private EditText etphone;
    private EditText etcity;
    private EditText etdescription;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        rgroup=(RadioGroup)findViewById(R.id.group_gender);

        immagineviewID = (ImageView) findViewById(R.id.immagineviewID);
        addimmagine = findViewById(R.id.immaginepiuID);
        addimmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
                //  Toast.makeText(this,"biagio", Toast.LENGTH_LONG).show();
            }
        });

        btnavanti = (Button) findViewById(R.id.add_button);
        btnindietro = (Button) findViewById(R.id.add_back);
        tvage = (TextView) findViewById(R.id.textAge);
        sbage = (SeekBar) findViewById(R.id.seekAge);
        etname = (EditText) findViewById(R.id.add_dog_name);
        etemail =(EditText) findViewById(R.id.add_email);
        etphone = (EditText) findViewById(R.id.add_phone);
        etcity = (EditText) findViewById(R.id.add_city);
        etdescription = (EditText) findViewById(R.id.add_description);
        String addname = etname.getText().toString().trim();
        String addemail = etemail.getText().toString().trim();
        String addphone = etphone.getText().toString().trim();
        String adddescription = etdescription.getText().toString().trim();
        final int agemax = 20;
        sbage.setMax(agemax);
        tvage.setText("Età: " + sbage.getProgress());
        sbage.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        tvage.setText("Età: " + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        tvage.setText("Età: " + progress_value);
                    }
                }
        );
        fillDogList();
        AutoCompleteTextView actvbreed = findViewById(R.id.cmpltbreed);
        AutoCompletePortrait adapter = new AutoCompletePortrait(this, dogList);
        actvbreed.setAdapter(adapter);
        btnavanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] provincine = {"Agrigento","Alessandria","Ancona","Aosta","L'Aquila","Arezzo","Ascoli-Piceno","Asti","Avellino","Bari","Barletta-Andria-Trani","Belluno","Benevento","Bergamo","Biella","Bologna","Bolzano","Brescia","Brindisi","Cagliari","Caltanissetta","Campobasso","Carbonia Iglesias","Caserta","Catania","Catanzaro","Chieti","Como","Cosenza","Cremona","Crotone","Cuneo","Enna","Fermo","Ferrara","Firenze","Foggia","Forli-Cesena","Frosinone","Genova","Gorizia","Grosseto","Imperia","Isernia","La-Spezia","Latina","Lecce","Lecco","Livorno","Lodi","Lucca","Macerata","Mantova","Massa-Carrara","Matera","Medio Campidano","Messina","Milano","Modena","Monza-Brianza","Napoli","Novara","Nuoro","Ogliastra","Olbia Tempio","Oristano","Padova","Palermo","Parma","Pavia","Perugia","Pesaro-Urbino","Pescara","Piacenza","Pisa","Pistoia","Pordenone","Potenza","Prato","Ragusa","Ravenna","Reggio-Calabria","Reggio-Emilia","Rieti","Rimini","Roma","Rovigo","Salerno","Sassari","Savona","Siena","Siracusa","Sondrio","Taranto","Teramo","Terni","Torino","Trapani","Trento","Treviso","Trieste","Udine","Varese","Venezia","Verbania","Vercelli","Verona","Vibo-Valentia","Vicenza","Viterbo"};
                String addcity = etcity.getText().toString().trim();
                String addname = etname.getText().toString().trim();
                String addemail = etemail.getText().toString().trim();
                String addphone = etphone.getText().toString().trim();
                String adddescription = etdescription.getText().toString().trim();
                boolean provincia=false;
                String provinciast = "";
                String nomest = "";
                String emailst ="";
                boolean nome=true;
                boolean email=true;

                for (String provinciona:provincine)
                {
                    if (addcity.equalsIgnoreCase(provinciona)) {
                       Toast.makeText(Add_Activity.this, addcity, Toast.LENGTH_SHORT).show();
                        provincia = true;
                        provinciast ="";
                        break;
                    }
                    else
                        provinciast = "Provincia errata ";
                }
                if (addname.isEmpty())
                {
                    //Toast.makeText(Add_Activity.this, "Nome non inserito", Toast.LENGTH_SHORT).show();
                    nome=false;
                    nomest = "Nome errato ";
                }
                if (addemail.isEmpty() || !addemail.contains("@"))
                {
                    Toast.makeText(Add_Activity.this, "Email non valida", Toast.LENGTH_SHORT).show();
                    email=false;
                    emailst = "Email errata ";
                }

               Toast.makeText(Add_Activity.this, provinciast + nomest+ emailst, Toast.LENGTH_SHORT).show();
            }
        });
        final Intent intentmain = new Intent(this, MainActivity.class);
        btnindietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Add_Activity.this,"Aggiunta fallita",Toast.LENGTH_SHORT).show();
                startActivity(intentmain);
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

    private void SelectImage(){
        final CharSequence[] items={"Fotocamera","Galleria", "Indietro"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Activity.this);
        builder.setTitle("Seleziona Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Fotocamera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[i].equals("Galleria")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[i].equals("Indietro")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                immagineviewID.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                immagineviewID.setImageURI(selectedImageUri);
            }

        }
    }

    public void rbclick(View v)
    {
        int idbutton=rgroup.getCheckedRadioButtonId();
        rbutton=(RadioButton)findViewById(idbutton);
        Toast.makeText(getApplicationContext(),rbutton.getText(),Toast.LENGTH_SHORT).show();
    }
}

