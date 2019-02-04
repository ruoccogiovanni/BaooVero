package com.example.giovanni.baoovero;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AutoCompletePortrait extends ArrayAdapter<Portrait_Dog> {
    private List<Portrait_Dog> dogListFull;

    public AutoCompletePortrait(@NonNull Context context, @NonNull List<Portrait_Dog> dogList) {
        super(context, 0, dogList);
        dogListFull = new ArrayList<>(dogList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return dogFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.complete_image_breed, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.tv_breed);
        ImageView imageViewFlag = convertView.findViewById(R.id.image_breed);

        Portrait_Dog portrait_Dog = getItem(position);

        if (portrait_Dog != null) {
            textViewName.setText(portrait_Dog.getDogName());
            imageViewFlag.setImageResource(portrait_Dog.getDogImage());
        }

        return convertView;
    }

    private Filter dogFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Portrait_Dog> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(dogListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Portrait_Dog item : dogListFull) {
                    if (item.getDogName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Portrait_Dog) resultValue).getDogName();
        }
    };
}
