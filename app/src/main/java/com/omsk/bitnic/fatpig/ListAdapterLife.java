package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import Model.Life;


public class ListAdapterLife extends ArrayAdapter<Life> {

    private List<Life> lifes;

    private int resource;

    public ListAdapterLife(Context context, int resource, List<Life> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.lifes = objects;
    }

    public Life getLife(int pos) {
        return lifes.get(pos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        float d = 16f;
        String f = "sans-serif";
        final Life p = getItem(position);
        final View mView = LayoutInflater.from(getContext()).inflate(resource, null);
        TextView date = (TextView) mView.findViewById(R.id.life_date);
        TextView mass = (TextView) mView.findViewById(R.id.life_mass);
        TextView pressure = (TextView) mView.findViewById(R.id.life_press);
        TextView calories = (TextView) mView.findViewById(R.id.life_calories);
        TextView comment = (TextView) mView.findViewById(R.id.life_comment);

        date.setText(Utils.simpleDateFormatE(new Date(p.date).getTime()));
        mass.setText(String.valueOf(p.mass));
        pressure.setText(p.pressure);
        calories.setText(String.valueOf(p.calories));
        comment.setText(p.commentary);

        mView.setTag(p);
        return mView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
