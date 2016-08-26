package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import Model.Product;
import Model.Work;

/**
 * Created by USER on 11.08.2016.
 */
public class ListAdapterWork extends ArrayAdapter<Work> {

    public List<Work> workList;
    private int resource;

    public ListAdapterWork(Context context, int resource, List<Work> objects) {
        super(context, resource, objects);
        this.workList=objects;
        this.resource=resource;
    }
    public Work getWork(int i){
        return workList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Work p = getItem(position);

        final View mView  = LayoutInflater.from(getContext()).inflate(resource, null);
        TextView productName= (TextView) mView.findViewById(R.id.product_name);
        TextView productCol= (TextView) mView.findViewById(R.id.product_color);

        productName.setText(p.name);
        productCol.setText(String.valueOf(p.calorieses));
        mView.setTag(p);
        return mView;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

}
