package com.example.giovanni.baoovero;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private AutoCompleteTextView etcity;
    private EditText etdescription;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private View v;
    private String urlimmagine;
    private  Bitmap bmp;
    File photoFile = null;
    private String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY_NAME = "BAOO";
    static final int CAPTURE_IMAGE_REQUEST = 1;
    public final static String[] provincine = {"Agrigento", "Alessandria", "Ancona", "Aosta", "L'Aquila", "Arezzo", "Ascoli-Piceno", "Asti", "Avellino", "Bari", "Barletta-Andria-Trani", "Belluno", "Benevento", "Bergamo", "Biella", "Bologna", "Bolzano", "Brescia", "Brindisi", "Cagliari", "Caltanissetta", "Campobasso", "Carbonia Iglesias", "Caserta", "Catania", "Catanzaro", "Chieti", "Como", "Cosenza", "Cremona", "Crotone", "Cuneo", "Enna", "Fermo", "Ferrara", "Firenze", "Foggia", "Forli-Cesena", "Frosinone", "Genova", "Gorizia", "Grosseto", "Imperia", "Isernia", "La-Spezia", "Latina", "Lecce", "Lecco", "Livorno", "Lodi", "Lucca", "Macerata", "Mantova", "Massa-Carrara", "Matera", "Medio Campidano", "Messina", "Milano", "Modena", "Monza-Brianza", "Napoli", "Novara", "Nuoro", "Ogliastra", "Olbia Tempio", "Oristano", "Padova", "Palermo", "Parma", "Pavia", "Perugia", "Pesaro-Urbino", "Pescara", "Piacenza", "Pisa", "Pistoia", "Pordenone", "Potenza", "Prato", "Ragusa", "Ravenna", "Reggio-Calabria", "Reggio-Emilia", "Rieti", "Rimini", "Roma", "Rovigo", "Salerno", "Sassari", "Savona", "Siena", "Siracusa", "Sondrio", "Taranto", "Teramo", "Terni", "Torino", "Trapani", "Trento", "Treviso", "Trieste", "Udine", "Varese", "Venezia", "Verbania", "Vercelli", "Verona", "Vibo-Valentia", "Vicenza", "Viterbo"};





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        v = findViewById(android.R.id.content);
        Snackbar.make(v,"Clicca sull'immagine per inserire una foto.",Snackbar.LENGTH_LONG).show();
        mDatabase= FirebaseDatabase.getInstance().getReference();
       //serve a prendere l'uid utente
        auth = FirebaseAuth.getInstance();
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String utente = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        rgroup=(RadioGroup)findViewById(R.id.group_gender);
        immagineviewID = (ImageView) findViewById(R.id.immagineviewID);
        addimmagine = findViewById(R.id.immaginepiuID);
        addimmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        btnavanti = (Button) findViewById(R.id.add_button);
        btnindietro = (Button) findViewById(R.id.add_back);
        tvage = (TextView) findViewById(R.id.textAge);
        sbage = (SeekBar) findViewById(R.id.seekAge);
        etname = (EditText) findViewById(R.id.add_dog_name);
        etphone = (EditText) findViewById(R.id.add_phone);
        etcity = (AutoCompleteTextView) findViewById(R.id.add_city);
        etdescription = (EditText) findViewById(R.id.add_description);
        ArrayAdapter<String> adattatore = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,provincine);
        etcity.setAdapter(adattatore);
        etcity.setDropDownVerticalOffset(-100);
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
        final AutoCompleteTextView actvbreed = findViewById(R.id.cmpltbreed);
        AutoCompletePortrait adapter = new AutoCompletePortrait(this, dogList);
        actvbreed.setAdapter(adapter);
        btnavanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grandezza = dogList.size();
                Portrait_Dog[] a = new Portrait_Dog[grandezza];
                dogList.toArray(a);
                List<String> razze = new ArrayList<>();
                for (Portrait_Dog s : a) {
                    razze.add(s.getDogName());
                }
                String addcity = etcity.getText().toString().trim();
                String addname = etname.getText().toString().trim();
                String addphone = etphone.getText().toString().trim();
                String adddescription = etdescription.getText().toString().trim();
                String addbreed = actvbreed.getText().toString().trim();
                int intage = sbage.getProgress();
                String addage = String.valueOf(intage);
                int idbutton = rgroup.getCheckedRadioButtonId();
                rbutton = findViewById(idbutton);
                String addgender = "Maschio";
                try {
                    addgender = rbutton.getText().toString();
                } catch (NullPointerException e) {

                }
                boolean provincia = false;
                boolean nome = true;
                boolean razza = false;
                boolean phone = true;
                for (String provinciona : provincine) {
                    if (addcity.equalsIgnoreCase(provinciona)) {
                        provincia = true;
                        break;
                    }
                }
                for (String s : razze) {
                    if (addbreed.equalsIgnoreCase(s)) {
                        razza = true;
                        break;
                    }
                }
                if (addname.isEmpty()) {
                    nome = false;
                    etname.setHintTextColor(getResources().getColor(R.color.error_color));
                    etname.setText("");
                }
                if (addphone.isEmpty()||(addphone.length() <9)) {
                    phone = false;
                    etphone.setHintTextColor(getResources().getColor(R.color.error_color));
                    etphone.setText("");
                }
                if (adddescription.isEmpty()) {
                    adddescription = "Il mio padroncino non vuole aggiungere la mia descrizione :(";
                }
                if (getUrlimmagine()==null)
                {
                    urlimmagine=null;
                }

                if (provincia && nome && phone && razza) {
                    getUrlimmagine();
                    Dog cane = new Dog(addname, addbreed, adddescription, addgender, addcity, addage, addphone, email, urlimmagine);
                    cane.setUtente(utente);
                    String uid = mDatabase.child("Cani").push().getKey();
                    cane.setUid(uid);
                    mDatabase.child("Cani").child(uid).setValue(cane);
                    Toast.makeText(Add_Activity.this, "Complimenti, hai aggiunto il tuo nuovo cane!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Add_Activity.this, ProfileActivity.class));
                } else
                    {
                    Toast.makeText(Add_Activity.this, "C'è qualcosa che non va. Sicuro di aver inserito bene le informazioni?", Toast.LENGTH_LONG).show();
                        if (!provincia)
                        {
                            etcity.setHintTextColor(getResources().getColor(R.color.error_color));
                            etcity.setText("");
                        }
                        if (!razza)
                        {
                            actvbreed.setHintTextColor(getResources().getColor(R.color.error_color));
                            actvbreed.setText("");
                        }
                    }
            }
        });
        btnindietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getUrlimmagine()!=null)
                {
                    StorageReference cancella = storage.getReferenceFromUrl(getUrlimmagine());
                    cancella.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Add_Activity.this, "Il file è stato eliminato mmbro", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                Toast.makeText(Add_Activity.this,"Aggiunta fallita",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add_Activity.this,ProfileActivity.class));
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

    private void SelectImage(){
        final CharSequence[] items={"Fotocamera","Galleria", "Indietro"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Activity.this);
        builder.setTitle("Seleziona Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Fotocamera")) {
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent, REQUEST_CAMERA);
                    captureimage();
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
    private void captureimage()
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        else
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile();
                    Toast.makeText(this, photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.giovanni.baoovero.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                }


            }else
            {
                Toast.makeText(this, "ERRORE", Toast.LENGTH_SHORT).show();
            }
        }



    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQUEST_CAMERA){
                //Bundle bundle = data.getExtras();
                //bmp = (Bitmap) bundle.get("data");
                bmp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                immagineviewID.setImageBitmap(bmp);
                uploadCamera();

            }else if(requestCode==SELECT_FILE){
                 selectedImageUri = data.getData();
                uploadImage();
                immagineviewID.setImageURI(selectedImageUri);
            }
            }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    public void rbclick(View v)
    {
        int idbutton=rgroup.getCheckedRadioButtonId();
        rbutton=(RadioButton)findViewById(idbutton);
    }
    public void onBackPressed() {
        super.onBackPressed();
        if (getUrlimmagine()!=null)
        {
            StorageReference cancella = storage.getReferenceFromUrl(getUrlimmagine());
            cancella.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Add_Activity.this, "Il file è stato eliminato mmbro", Toast.LENGTH_SHORT).show();
                }
            });
        }
        startActivity(new Intent(Add_Activity.this,ProfileActivity.class));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Caricamento in corso ");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        StorageReference ref = storageReference.child("Foto/" + UUID.randomUUID().toString());
        if (selectedImageUri != null) {
            ref.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Snackbar.make(v, "Caricamento completato", Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            setUrlimmagine(downloadUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(v, "Caricamento fallito, riprova.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Caricamento in corso: " + (int) progress + "%");
                        }
                    });
        }
    }
private void uploadCamera(){
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Caricamento in corso ");
    progressDialog.setCancelable(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.show();
    StorageReference ref = storageReference.child("Foto/" + UUID.randomUUID().toString());
                     if (bmp!=null){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        ref.putBytes(data)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Snackbar.make(v, "Caricamento completato", Toast.LENGTH_SHORT).show();
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful());
                                Uri downloadUrl = urlTask.getResult();
                                setUrlimmagine(downloadUrl);
                           }
                      })
                            .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Snackbar.make(v, "Caricamento fallito, riprova.", Toast.LENGTH_SHORT).show();
                           }
                      })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Caricamento in corso: "+(int)progress+"%");
                        }
                    });
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureimage();
            }
        }

    }
    public void setUrlimmagine(Uri prova)
    {
        urlimmagine=new String(prova.toString());
    }
    public String getUrlimmagine()
    {
        return urlimmagine;
    }

}


