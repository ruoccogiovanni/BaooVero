package com.example.giovanni.baoovero;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class FilterRecyclerView extends  RecyclerView.Adapter<FilterRecyclerView.MyViewHolder>{

    private Context mContext ;
    private List<Dog> mData ;


    public FilterRecyclerView(Context mContext, List<Dog> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_dog,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_dog_name.setText(mData.get(position).getName());
        holder.img_dog_thumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,Dog_Activity.class);

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
                // start the activity
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dog_name;
        ImageView img_dog_thumbnail;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_dog_name = (TextView) itemView.findViewById(R.id.dog_name_id) ;
            img_dog_thumbnail = (ImageView) itemView.findViewById(R.id.dog_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);


        }
    }


}