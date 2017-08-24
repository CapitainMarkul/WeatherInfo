package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.tensor.dapavlov1.tensorfirststep.provider.repository.places.PlacesDataRepository;

import java.util.List;

/**
 * Created by Dmitry on 07.06.2017.
 */

public class PlacesAutoComplete extends ArrayAdapter<String> implements Filterable {

    private List<String> resultList;

    //    private Context mContext;
//    private int mResource;
//
    public PlacesAutoComplete(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = PlacesDataRepository.getInstance().getPlaces(constraint.toString());

                    if (resultList != null) {
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}
