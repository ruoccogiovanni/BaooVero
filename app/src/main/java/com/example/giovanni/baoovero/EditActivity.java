package com.example.giovanni.baoovero;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static com.example.giovanni.baoovero.AddActivity.provincine;

public class EditActivity extends AppCompatActivity {
    ImageView immagineviewID;
    private Button addimmagine;
    private Button btnedit;

    private SeekBar sbage;
    private TextView tvage;
    private AutoCompleteTextView actvbreed,etcity;
    private List<PortraitDog> dogList;
    private RadioGroup rgroup;
    private RadioButton rbutton, rmaschio,rfemmina;
    static final int REQUEST_CAMERA = 1;
    Integer SELECT_FILE=0;
    private EditText etname;
    private EditText etemail;
    private EditText etphone;
    private EditText etdescription;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private View v;
    private String urlimmagine;
    private String imagetobedeleted;
    private Bitmap bmp;
    File photoFile = null;
    private String mCurrentPhotoPath, image;
    private static final String IMAGE_DIRECTORY_NAME = "BAOO";
    static final int CAPTURE_IMAGE_REQUEST = 1;
    private ConstraintLayout layout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        layout=(ConstraintLayout)findViewById(R.id.editlayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });
        setTitle("Edit your dog");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        v = findViewById(android.R.id.content);
        Snackbar.make(v,"Here you can edit your dog.",Snackbar.LENGTH_SHORT).show();
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

        tvage = (TextView) findViewById(R.id.texteditAge);
        sbage = (SeekBar) findViewById(R.id.edit_Age);
        etname = (EditText) findViewById(R.id.edit_dog_name);
        etphone = (EditText) findViewById(R.id.edit_phone);
        etcity = (AutoCompleteTextView) findViewById(R.id.edit_city);
        ArrayAdapter<String> adattatore = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,provincine);
        etcity.setAdapter(adattatore);
        etcity.setDropDownVerticalOffset(-100);
        etdescription = (EditText) findViewById(R.id.edit_description);
        String annio=" years";

        final Intent intent = getIntent();
        final String Name = intent.getExtras().getString("Name");
        final String Breed =  intent.getExtras().getString("Breed");
        final String Description = intent.getExtras().getString("Description");
        String descrizione= Name + "'s description:\n" + Description;
        final String Gender = intent.getExtras().getString("Gender");
        final String City = intent.getExtras().getString("City");
        String citta= "City: " + City;
        final String eig = intent.getExtras().getString("Age");
        if(Integer.parseInt(eig)==1)  annio=" year";
        final String Age = "Age: " + eig + annio;
        final String Tel = intent.getExtras().getString("Tel");
        final String Email = intent.getExtras().getString("Email");
        image = intent.getExtras().getString("Image");
        final String Uid = intent.getExtras().getString("Uid");
        final String utente=auth.getCurrentUser().getUid();
        if(Gender.equals("Male"))
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
                .placeholder(R.drawable.caricacuore)
                .fit()
                .centerCrop()
                .into(immagineviewID);
        final int agemax = 20;
        Date d=new Date();
        final int year=d.getYear() + 1900; //anno attuale
        sbage.setMax(agemax);
        sbage.setProgress(Integer.parseInt(eig));
        Toast.makeText(EditActivity.this, eig, Toast.LENGTH_LONG).show();
        tvage.setText("Year of birth: " + (year - sbage.getProgress()));
        sbage.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = year - progress;
                        tvage.setText("Year of birth: " + (year - progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        tvage.setText("Year of birth: " + progress_value);
                    }
                }
        );
        fillDogList();
        final AutoCompleteTextView actvbreed = findViewById(R.id.editcmpltbreed);
        actvbreed.setText(Breed);
        AutoCompleteBreed adapter = new AutoCompleteBreed(this, dogList);
        actvbreed.setAdapter(adapter);
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] provincine = {"Agrigento","Alessandria","Ancona","Aosta","L'Aquila","Arezzo","Ascoli-Piceno","Asti","Avellino","Bari","Barletta-Andria-Trani","Belluno","Benevento","Bergamo","Biella","Bologna","Bolzano","Brescia","Brindisi","Cagliari","Caltanissetta","Campobasso","Carbonia Iglesias","Caserta","Catania","Catanzaro","Chieti","Como","Cosenza","Cremona","Crotone","Cuneo","Enna","Fermo","Ferrara","Firenze","Foggia","Forli-Cesena","Frosinone","Genova","Gorizia","Grosseto","Imperia","Isernia","La-Spezia","Latina","Lecce","Lecco","Livorno","Lodi","Lucca","Macerata","Mantova","Massa-Carrara","Matera","Medio Campidano","Messina","Milano","Modena","Monza-Brianza","Napoli","Novara","Nuoro","Ogliastra","Olbia Tempio","Oristano","Padova","Palermo","Parma","Pavia","Perugia","Pesaro-Urbino","Pescara","Piacenza","Pisa","Pistoia","Pordenone","Potenza","Prato","Ragusa","Ravenna","Reggio-Calabria","Reggio-Emilia","Rieti","Rimini","Roma","Rovigo","Salerno","Sassari","Savona","Siena","Siracusa","Sondrio","Taranto","Teramo","Terni","Torino","Trapani","Trento","Treviso","Trieste","Udine","Varese","Venezia","Verbania","Vercelli","Verona","Vibo-Valentia","Vicenza","Viterbo"};
                int grandezza = dogList.size();
                PortraitDog[] a = new PortraitDog[grandezza];
                dogList.toArray(a);
                List<String> razze = new ArrayList<>();
                for (PortraitDog s : a)
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
                String addgender="Male";
                try {
                    addgender= rbutton.getText().toString();
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(EditActivity.this, "Your dog have no sex?", Toast.LENGTH_SHORT).show();
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
                    etname.setText("");
                }
                if (addphone.isEmpty()||(addphone.length() <9))
                {
                    phone=false;
                    etphone.setHintTextColor(getResources().getColor(R.color.error_color));
                    etphone.setText("");
                }
                if (adddescription.isEmpty())
                {
                    adddescription="No description for me :(";
                }
                if (provincia&&nome&&phone&&razza)
                {
                    getUrlimmagine();
                    if (getUrlimmagine()==null)
                    {
                        urlimmagine=image;
                    }
                    else
                    {
                        deleteImage(image);
                        /*StorageReference cancella = storage.getReferenceFromUrl(image);
                        cancella.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });*/
                    }
                    Dog cane = new Dog(addname,addbreed,adddescription,addgender,addcity,addage,addphone,email,urlimmagine);
                    cane.setUtente(utente);
                    cane.setUid(Uid);
                    mDatabase.child("Cani").child(Uid).setValue(null);
                    mDatabase.child("Cani").child(Uid).setValue(cane);
                    Toast.makeText(EditActivity.this, "Congratulations, you have modified "+addname+"!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditActivity.this,ProfileActivity.class));
                }
                else {
                    Toast.makeText(EditActivity.this, "There's something wrong. Are you sure to have inserted everything?", Toast.LENGTH_LONG).show();
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
    }

    private void fillDogList() {
        dogList = new ArrayList<>();
        dogList.add(new PortraitDog("Akita Inu", R.drawable.image_akita));
        dogList.add(new PortraitDog("Alano", R.drawable.image_alano));
        dogList.add(new PortraitDog("Corgi", R.drawable.image_corgi));
        dogList.add(new PortraitDog("Bassotto", R.drawable.image_bassotto));
        dogList.add(new PortraitDog("Cane da Pastore Maremmano Abruzzese", R.drawable.image_maremmano));
        dogList.add(new PortraitDog("American Staffordshire Terrier", R.drawable.image_american_terrier));
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
        dogList.add(new PortraitDog("Barboncino", R.drawable.image_barboncino));
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

    private void SelectImage(){
        final CharSequence[] items={"Camera","Album", "Back"};
        setImagetobedeleted(image);
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle("Select photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    captureimage();
                } else if (items[i].equals("Album")) {
                    if (ContextCompat.checkSelfPermission(EditActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                        selectimage();
                    else
                        requeststorage();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[i].equals("Back")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectimage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }
    private void requeststorage(){
        ActivityCompat.requestPermissions(EditActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
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

                try {

                    photoFile = createImageFile();

                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.giovanni.baoovero.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex) {

                }


            }else
            {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
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
            deleteImage(getUrlimmagine());
        /*if (getUrlimmagine()!=null)
        {
            StorageReference cancella = storage.getReferenceFromUrl(getUrlimmagine());
            cancella.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });
        }*/
        startActivity(new Intent(EditActivity.this,ProfileActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading ");
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
                            Snackbar.make(v, "Upload completed", Snackbar.LENGTH_SHORT).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            setUrlimmagine(downloadUrl);
                            //deleteImage(imagetobedeleted);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(v, "Upload failed, retry.", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Loading: " + (int) progress + "%");
                        }
                    });
        }
    }
    private void uploadCamera(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading ");
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
                            Snackbar.make(v, "Upload complete", Snackbar.LENGTH_SHORT).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            setUrlimmagine(downloadUrl);
                            //deleteImage(imagetobedeleted);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(v, "Upload failed, retry.", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Loading: "+(int)progress+"%");
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
        if (requestCode==1)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                selectimage();
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
    public String getImagetobedeleted()
    {
        return imagetobedeleted;
    }

    public void setImagetobedeleted(String imagetobedeleted)
    {
        this.imagetobedeleted = imagetobedeleted;
    }
    private void deleteImage(String immagine)
    {
        StorageReference cancella = storage.getReferenceFromUrl(immagine);
        cancella.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    private void closeKeyboard(){
        View vista = this.getCurrentFocus();
        if (vista!=null){
            InputMethodManager inputt = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputt.hideSoftInputFromWindow(vista.getWindowToken(),0);
        }
    }

}


