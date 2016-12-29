package com.example.alexeykrichun.gett_places.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.alexeykrichun.gett_places.PlacesViewContract;
import com.example.alexeykrichun.gett_places.R;
import com.example.alexeykrichun.gett_places.model.AutocompleteResult;

import java.util.List;

/**
 * Created by alexeykrichun on 27/12/2016.
 */

public class AutocompleteArrayAdapter extends ArrayAdapter<AutocompleteResult> implements Filterable {
    private final PlacesViewContract.Presenter presenter;

    public AutocompleteArrayAdapter(Context context, PlacesViewContract.Presenter presenter) {
        super(context, R.layout.autocomplete_item);
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AutocompleteResult place = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        if (place != null) {
            ((TextView) convertView).setText(place.address);
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    List<AutocompleteResult> results = presenter.getAutocompleteSuggesionsSync(charSequence.toString());
                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults != null && filterResults.count > 0) {
                    clear();
                    List<AutocompleteResult> results = (List<AutocompleteResult>) filterResults.values;
                    addAll(results);
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}

