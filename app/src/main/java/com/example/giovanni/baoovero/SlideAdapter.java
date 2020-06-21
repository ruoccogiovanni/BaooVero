package com.example.giovanni.baoovero;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    public int[] lst_images = {
            R.drawable.ic_about,
            R.drawable.ic_icona_profilo,
            R.drawable.guide_search,
            R.drawable.guide_lookfor,
            R.drawable.guide_contact
    };
    public String[] lst_title = {
            "MEETING",
            "LOVE IS IN THE AIR",
            "LOOK FOR DOGS",
            "ADD YOUR PREFERENCES",
            "CONTACT THE OWNER"
    };
    public String[] lst_description = {
            "The goal of our application is to make our puppies fall in love.",
            "Baoo is the perfect way to find a partner for our dogs",
            "With a simple tap you can go to search function",
            "You can choose gender, breed, age and city to be sure to find the perfect match for your dog",
            "After choosing the dog you can contact the owner via email or telephone"
    };
    public int[]  lst_backgroundcolor = {
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(52,89,149),
            Color.rgb(75,150,75),
            Color.rgb(29,45,73)
    };

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.sliderlinearlayout);
        ImageView imgslide = (ImageView)  view.findViewById(R.id.sliderimg);
        TextView txttitle= (TextView) view.findViewById(R.id.txttitle);
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        layoutslide.setBackgroundColor(lst_backgroundcolor[position]);
        imgslide.setImageResource(lst_images[position]);
        txttitle.setText(lst_title[position]);
        description.setText(lst_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}