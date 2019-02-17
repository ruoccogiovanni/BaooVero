package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;


import static android.content.Context.VIBRATOR_SERVICE;

public class RecyclerViewProfile extends RecyclerView.Adapter<RecyclerViewProfile.MyViewHolder> {

    private Context mContext ;
    private List<Dog> mData ;
    private String uid;
    private Vibrator myVib;
    private DatabaseReference myRef;
    private String Name,Breed,Description,Gender,Age,Tel,Email,City,Thumbnail,Utente;
    private FirebaseAuth auth;

    public RecyclerViewProfile(Context mContext, List<Dog> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_profile,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        myRef= FirebaseDatabase.getInstance().getReference("Cani");
        Dog currentdog= mData.get(position);
        holder.tv_dog_name.setText(mData.get(position).getName());
        holder.tv_dog_breed.setText(mData.get(position).getBreed());
        String eig="anni";
        String eta="Età: ";
        uid=mData.get(position).getUid();
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            Utente = auth.getCurrentUser().getUid();
        }

        if (Integer.parseInt(mData.get(position).getAge())==1)
            eig="anno";
        holder.tv_dog_age.setText(eta+mData.get(position).getAge()+" " + eig);
        Picasso.get()
                .load(currentdog.getThumbnail())
                .placeholder(R.drawable.loading_prova)
                .fit()
                .centerCrop()
                .into(holder.img_dog_thumbnail);

        Name=mData.get(position).getName();
        Breed=mData.get(position).getBreed();
        Description=mData.get(position).getDescription();
        Gender=mData.get(position).getGender();
        City=mData.get(position).getCity();
        Age=mData.get(position).getAge();
        Tel=mData.get(position).getTel();
        Email=mData.get(position).getEmail();
        Thumbnail=mData.get(position).getThumbnail();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,ProfileDog.class);

                // passing data to the dog activity
                intent.putExtra("Name",mData.get(position).getName());
                intent.putExtra("Breed",mData.get(position).getBreed());
                intent.putExtra("Description",mData.get(position).getDescription());
                intent.putExtra("Gender",mData.get(position).getGender());
                intent.putExtra("City",mData.get(position).getCity());
                intent.putExtra("Age",mData.get(position).getAge());
                intent.putExtra("Tel",mData.get(position).getTel());
                intent.putExtra("Email",mData.get(position).getEmail());
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                intent.putExtra("Uid",mData.get(position).getUid());
                // start the activity
                mContext.startActivity(intent);


            }
        });


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                SelectEditProfile();
                myVib=(Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);


                return true;
            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dog_name;
        TextView tv_dog_breed;
        TextView tv_dog_age;
        ImageView img_dog_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_dog_name = (TextView) itemView.findViewById(R.id.dog_name_id_profile) ;
            tv_dog_breed=(TextView) itemView.findViewById(R.id.dog_breed_id_profile);
            tv_dog_age=(TextView)itemView.findViewById(R.id.dog_age_profile);
            img_dog_thumbnail = (ImageView) itemView.findViewById(R.id.dog_img_id_profile);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id_profile);


        }
    }


    private void SelectEditProfile(){
        final CharSequence[] items={"Modifica il tuo cane", "Elimina il tuo cane","Torna indietro"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Cosa vuoi fare?");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Modifica il tuo cane")) {
                    dialogInterface.dismiss();
                }else if (items[i].equals("Indietro")) {
                    dialogInterface.dismiss();
                }
                else if (items[i].equals("Elimina il tuo cane"))
                {
                    dialogInterface.dismiss();
                    SelectConfirm();
                }
            }
        });
        builder.show();
    }



    private void SelectConfirm(){
        final CharSequence[] items={"Confermo", "Annulla"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Sei sicuro di voler eliminare " + Name +"?");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Annulla")) {
                    dialogInterface.dismiss();
                }
                else if (items[i].equals("Confermo"))
                {
                    myVib.vibrate(25);
                    myRef.child(uid).setValue(null);
                    dialogInterface.dismiss();
                    mContext.startActivity(new Intent(mContext,ProfileActivity.class));
                }
            }
        });
        builder.show();
    }



}
