package com.lelangapa.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.FilterSearchResources;

import java.util.ArrayList;

/**
 * Created by andre on 05/05/17.
 */

public class FilterSearchAdapter extends RecyclerView.Adapter<FilterSearchAdapter.ViewHolder>{
    private ArrayList<FilterSearchResources> filter;
    public FilterSearchAdapter(ArrayList<FilterSearchResources> filter) {
        this.filter = filter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_filter_layout_items, parent, false);
        return new FilterSearchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterSearchResources resources = filter.get(position);
        holder.textView_key.setText(resources.getJudulFilter());
        holder.textView_value.setText(resources.getValueFilter());
    }

    @Override
    public int getItemCount() {
        return filter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_key, textView_value;
        public ViewHolder(View view) {
            super(view);
            textView_key = (TextView) view.findViewById(R.id.fragment_filter_layout_judul);
            textView_value = (TextView) view.findViewById(R.id.fragment_filter_layout_value);
        }
    }

}
