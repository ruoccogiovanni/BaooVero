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

    // list of images
    public int[] lst_images = {
            R.drawable.ic_about,
            R.drawable.ic_icona_profilo,
            R.drawable.ic_icona_baoo,
            R.drawable.image_meticcio
    };
    // list of titles
    public String[] lst_title = {
            "INCONTRARCI",
            "COSE SCONCE", //QUI C'ERA SCOPARE
            "DEPRESSIONE",
            "POST PARTO"
    }   ;
    // list of descriptions
    public String[] lst_description = {
            "Lo scopo della nostra applicazione è trovare compagni di gioco ai nostri cuccioli.",
            "Questi cagnolini hanno l'aria da furfanti, un po' come noi.",
            "Qui arriva il momento di pensare che loro hanno trovato l'amore, e noi no.",
            "Finalmente un easter egg, e che diamine."
            //E magar mettr a mazz o cavr.",
            //"Sti sfaccimm e chen stann tutt nfuchet. Ma mocc e mamm lor vulimm chiava pur ij e te? Chiamm'm ciaccarè <3",
            //"Si lor chiavn e nuj no stam nguajet auer.",
            //"Ij m pigl o chiu bell... nun m cacà o cazz."
    };
    // list of background colors
    public int[]  lst_backgroundcolor = {
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(1,188,212)
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
