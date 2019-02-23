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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EditActivity extends AppCompatActivity {
    ImageView immagineviewID;
    private Button addimmagine;
    private Button btnedit;
    private Button btnindietro;
    private SeekBar sbage;
    private TextView tvage;
    private AutoCompleteTextView actvbreed;
    private List<Portrait_Dog> dogList;
    private RadioGroup rgroup;
    private RadioButton rbutton, rmaschio,rfemmina;
    static final int REQUEST_CAMERA = 1;
    Integer SELECT_FILE=0;
    private EditText etname;
    private EditText etemail;
    private EditText etphone;
    private EditText etcity;
    private EditText etdescription;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private View v;
    private String urlimmagine;
    private Bitmap bmp;
    File photoFile = null;
    private String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY_NAME = "BAOO";
    static final int CAPTURE_IMAGE_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        v = findViewById(android.R.id.content);
        Snackbar.make(v,"Qui puoi modificare il tuo cane.",Snackbar.LENGTH_SHORT).show();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        //serve a prendere l'uid utente
        auth = FirebaseAuth.getInstance();
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        rgroup=(RadioGroup)findViewById(R.id.edit_group_gender);
        rmaschio=(RadioButton)findViewById(R.id.edit_button_male);
        rfemmina=(RadioButton)findViewById(R.id.edit_button_female);
        immagineviewID = (ImageView) findViewById(R.id.editimmagineviewID);
        addimmagine = findViewById(R.id.editimmaginepiuID);
        addimmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        btnedit = (Button) findViewById(R.id.edit_button);
        btnindietro = (Button) findViewById(R.id.edit_back);
        tvage = (TextView) findViewById(R.id.texteditAge);
        sbage = (SeekBar) findViewById(R.id.edit_Age);
        etname = (EditText) findViewById(R.id.edit_dog_name);
        etphone = (EditText) findViewById(R.id.edit_phone);
        etcity = (EditText) findViewById(R.id.edit_city);
        etdescription = (EditText) findViewById(R.id.edit_description);
        String annio=" anni";

        final Intent intent = getIntent();
        final String Name = intent.getExtras().getString("Name");
        final String Breed =  intent.getExtras().getString("Breed");
        final String Description = intent.getExtras().getString("Description");
        String descrizione="Descrizione di " + Name + ": \n" + Description;
        final String Gender = intent.getExtras().getString("Gender");
        final String City = intent.getExtras().getString("City");
        String citta= "Città: " + City;
        final String eig = intent.getExtras().getString("Age");
        if(Integer.parseInt(eig)==1)  annio=" anno";
        final String Age = "Età: " + eig + annio;
        final String Tel = intent.getExtras().getString("Tel");
        final String Email = intent.getExtras().getString("Email");
        final String image = intent.getExtras().getString("Image");
        final String Uid = intent.getExtras().getString("Uid");
        final String utente=auth.getCurrentUser().getUid();
        if(Gender.equals("Maschio"))
            rmaschio.setChecked(true);
        else
            rfemmina.setChecked(true);
        // Setting values
        etname.setText(Name);
        etdescription.setText(Description);
        etphone.setText(Tel);
        etcity.setText(City);
        tvage.setText(Age);
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.loading_prova)
                .fit()
                .centerCrop()
                .into(immagineviewID);
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
        final AutoCompleteTextView actvbreed = findViewById(R.id.editcmpltbreed);
        actvbreed.setText(Breed);
        AutoCompletePortrait adapter = new AutoCompletePortrait(this, dogList);
        actvbreed.setAdapter(adapter);
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] provincine = {"Agrigento","Alessandria","Ancona","Aosta","L'Aquila","Arezzo","Ascoli-Piceno","Asti","Avellino","Bari","Barletta-Andria-Trani","Belluno","Benevento","Bergamo","Biella","Bologna","Bolzano","Brescia","Brindisi","Cagliari","Caltanissetta","Campobasso","Carbonia Iglesias","Caserta","Catania","Catanzaro","Chieti","Como","Cosenza","Cremona","Crotone","Cuneo","Enna","Fermo","Ferrara","Firenze","Foggia","Forli-Cesena","Frosinone","Genova","Gorizia","Grosseto","Imperia","Isernia","La-Spezia","Latina","Lecce","Lecco","Livorno","Lodi","Lucca","Macerata","Mantova","Massa-Carrara","Matera","Medio Campidano","Messina","Milano","Modena","Monza-Brianza","Napoli","Novara","Nuoro","Ogliastra","Olbia Tempio","Oristano","Padova","Palermo","Parma","Pavia","Perugia","Pesaro-Urbino","Pescara","Piacenza","Pisa","Pistoia","Pordenone","Potenza","Prato","Ragusa","Ravenna","Reggio-Calabria","Reggio-Emilia","Rieti","Rimini","Roma","Rovigo","Salerno","Sassari","Savona","Siena","Siracusa","Sondrio","Taranto","Teramo","Terni","Torino","Trapani","Trento","Treviso","Trieste","Udine","Varese","Venezia","Verbania","Vercelli","Verona","Vibo-Valentia","Vicenza","Viterbo"};
                int grandezza = dogList.size();
                Portrait_Dog[] a = new Portrait_Dog[grandezza];
                dogList.toArray(a);
                List<String> razze = new ArrayList<>();
                for (Portrait_Dog s : a)
                {
                    razze.add(s.getDogName());
                }
                String addcity = etcity.getText().toString().trim();
                String addname = etname.getText().toString().trim();
                String addphone = etphone.getText().toString().trim();
                String adddescription = etdescription.getText().toString().trim();
                String addbreed = actvbreed.getText().toString().trim();
                int intage = sbage.getProgress();
                String addage = String.valueOf(intage);
                int idbutton=rgroup.getCheckedRadioButtonId();
                rbutton= findViewById(idbutton);
                String addgender="Maschio";
                try {
                    addgender= rbutton.getText().toString();
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(EditActivity.this, "Il tuo cane non ha sesso?", Toast.LENGTH_SHORT).show();
                }
                boolean provincia=false;
                boolean nome=true;
                boolean razza=false;
                boolean phone=true;
                for (String provinciona:provincine)
                {
                    if (addcity.equalsIgnoreCase(provinciona)) {
                        provincia = true;
                        break;
                    }
                }
                for (String s:razze)
                {
                    if (addbreed.equalsIgnoreCase(s)) {
                        razza = true;
                        break;
                    }
                }
                if (addname.isEmpty())
                {
                    nome=false;
                    etname.setHintTextColor(getResources().getColor(R.color.error_color));
                }
                if (addphone.isEmpty())
                {
                    phone=false;
                    etphone.setHintTextColor(getResources().getColor(R.color.error_color));
                }
                if (adddescription.isEmpty())
                {
                    adddescription="Il mio padroncino non vuole aggiungere la mia descrizione :(";
                }
                if (provincia&&nome&&phone&&razza)
                {
                    getUrlimmagine();
                    if (getUrlimmagine()==null)
                    {
                        urlimmagine=image;
                    }
                    Dog cane = new Dog(addname,addbreed,adddescription,addgender,addcity,addage,addphone,email,urlimmagine);
                    cane.setUtente(utente);
                    cane.setUid(Uid);
                    mDatabase.child("Cani").child(Uid).setValue(null);
                    mDatabase.child("Cani").child(Uid).setValue(cane);
                    Toast.makeText(EditActivity.this, "Complimenti, hai modificato "+addname+"!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditActivity.this,ProfileActivity.class));
                }
                else {
                    Toast.makeText(EditActivity.this, "C'è qualcosa che non va. Sicuro di aver inserito tutto?", Toast.LENGTH_LONG).show();
                    etcity.setHintTextColor(getResources().getColor(R.color.error_color));
                    actvbreed.setHintTextColor(getResources().getColor(R.color.error_color));
                    etcity.setText("");
                    actvbreed.setText("");
                }
            }
        });
        btnindietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this,"Modifica non effettuata",Toast.LENGTH_SHORT).show();
                if (getUrlimmagine()!=null)
                {
                    StorageReference cancella = storage.getReferenceFromUrl(getUrlimmagine());
                    cancella.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditActivity.this, "Il file è stato eliminato mmbro", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                startActivity(new Intent(EditActivity.this,ProfileActivity.class));
            }
        });
    }

    private void fillDogList() {
        dogList = new ArrayList<>();
        dogList.add(new Portrait_Dog("Akita Inu", R.drawable.image_akita));
        dogList.add(new Portrait_Dog("Alano", R.drawable.image_alano));
        dogList.add(new Portrait_Dog("Bassotto", R.drawable.image_bassotto));
        dogList.add(new Portrait_Dog("Beagle", R.drawable.image_beagle));
        dogList.add(new Portrait_Dog("Bearded Collie", R.drawable.beardedcollie));
        dogList.add(new Portrait_Dog("Border Collie", R.drawable.bordercollie));
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
        dogList.add(new Portrait_Dog("Jack Russell Terrier", R.drawable.image_russel));
        dogList.add(new Portrait_Dog("Schnauzer Nano", R.drawable.image_shnauzer));
        dogList.add(new Portrait_Dog("Shiba Inu", R.drawable.image_shiba));
        dogList.add(new Portrait_Dog("Volpino Italiano", R.drawable.image_volpino));
        dogList.add(new Portrait_Dog("West Highland White Terrier", R.drawable.image_westie));
        dogList.add(new Portrait_Dog("Yorkshire Terrier", R.drawable.image_yorkshire));
    }

    private void SelectImage(){
        final CharSequence[] items={"Fotocamera","Galleria", "Indietro"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
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
                immagineviewID.setImageBitmap(bmp);
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
                    Toast.makeText(EditActivity.this, "Il file è stato eliminato mmbro", Toast.LENGTH_SHORT).show();
                }
            });
        }
        startActivity(new Intent(EditActivity.this,ProfileActivity.class));
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


