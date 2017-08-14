package com.tensor.dapavlov1.tensorfirststep;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 07.06.2017.
 */

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    List<String> resultList;

    Context mContext;
    int mResource;

    public PlacesAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;
        mResource = resource;
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
                    try {
                        resultList =
                                GsonFactory.getInstance().getPlacesName(
                                        ConnectToApi.getInstance().getJsonFromGooglePlaceApi(
                                                ConnectToApi.getInstance().createMapForGoogleApi(
                                                        BuildConfig.GOOGLE_API_KEY,
                                                        BuildConfig.GOOGLE_API_TYPES,
                                                        constraint.toString()
                                                )
//                                        )
                                        )
                                );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
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
