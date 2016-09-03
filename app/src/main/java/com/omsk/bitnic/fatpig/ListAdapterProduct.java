package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import Model.Product;
import Model.ProductBase;
import Transceiver.Transceiver;

public class ListAdapterProduct extends ArrayAdapter<ProductBase> {

    public List<ProductBase> productList;
    private int resource;
    public ListAdapterProduct(Context context, int resource, List<ProductBase> objects) {
        super(context, resource, objects);
        this.productList=objects;
        this.resource=resource;
    }
    public ProductBase getProduct(int i){
        return productList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ProductBase p = getItem(position);

        final View mView  = LayoutInflater.from(getContext()).inflate(resource, null);
        TextView productName= (TextView) mView.findViewById(R.id.product_name);
        TextView productCol= (TextView) mView.findViewById(R.id.product_color);

        productName.setText(p.name);
        productCol.setText(String.valueOf(p.calorieses));
       if(p.preferences){
           productName.setTextColor(Color.BLUE);
       }
        mView.setTag(p);
        return mView;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


}

