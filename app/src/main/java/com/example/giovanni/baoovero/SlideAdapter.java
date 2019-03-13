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
            R.drawable.ic_icona_baoo,
            R.drawable.image_meticcio
    };
    public String[] lst_title = {
            "INCONTRARCI",
            "COSE SCONCE",
            "DEPRESSIONE",
            "ARRIVANO I CUCCIOLI"
    };
    public String[] lst_description = {
            "Lo scopo della nostra applicazione è far trovare l'amore ai nostri cuccioli.",
            "Questi cagnolini hanno l'aria da furfanti, un po' come noi.",
            "Infine arriva il momento di pensare che loro hanno trovato l'amore, e noi no.",
            "Restate aggiornati. In pentola bollono molte altre funzionalità, e non cani, mi raccomando."
    };
    public int[]  lst_backgroundcolor = {
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(75,150,75)
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