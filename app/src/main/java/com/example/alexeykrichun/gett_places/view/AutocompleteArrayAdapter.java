package com.example.alexeykrichun.gett_places.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexeykrichun.gett_places.R;
import com.example.alexeykrichun.gett_places.model.AutocompleteResult;
import com.example.alexeykrichun.gett_places.model.Place;

import java.util.List;

/**
 * Created by alexeykrichun on 27/12/2016.
 */

public class AutocompleteArrayAdapter extends ArrayAdapter<AutocompleteResult> {
    public AutocompleteArrayAdapter(Context context) {
        super(context, R.layout.autocomplete_item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AutocompleteResult place = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocomplete_item, parent, false);
        }

        ((TextView)convertView).setText(place.address);
        return convertView;
    }
}
