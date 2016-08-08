package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import Model.Product;

public class ListAdapterProduct extends ArrayAdapter<Product> {

    private int resource;
    public ListAdapterProduct(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        final Product p = getItem(position);
        if (p != null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            mView = vi.inflate(resource, null);
        }
        TextView productName= (TextView) mView.findViewById(R.id.product_name);
        TextView productCol= (TextView) mView.findViewById(R.id.product_color);
        CheckBox checkBox= (CheckBox) mView.findViewById(R.id.product_pref);
        productName.setText(p.name);
        productCol.setText(String.valueOf(p.calorieses));
        checkBox.setChecked(p.preferences);
        return mView;
    }
}
