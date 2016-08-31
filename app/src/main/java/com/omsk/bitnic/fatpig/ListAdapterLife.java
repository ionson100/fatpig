package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Date;
import java.util.List;
import Model.Life;




public class ListAdapterLife extends ArrayAdapter<Life> {

    private List<Life> lifes;

    private int resource ;

    public ListAdapterLife(Context context, int resource, List<Life> objects) {
        super(context, resource, objects);
        this.resource=resource;
        this.lifes=objects;
    }

    public Life getLife(int pos){
        return lifes.get(pos);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        float d=16f;
        String f="sans-serif";
        final Life p = getItem(position);
        final View mView  = LayoutInflater.from(getContext()).inflate(resource, null);
        Pupper date= (Pupper) mView.findViewById(R.id.life_date);
        Pupper mass= (Pupper) mView.findViewById(R.id.life_mass);
        Pupper pressure= (Pupper) mView.findViewById(R.id.life_press);
        Pupper calories= (Pupper) mView.findViewById(R.id.life_calories);
        Pupper comment= (Pupper) mView.findViewById(R.id.life_comment);

        date.setPairString("Дата:",Utils.simpleDateFormatE(Utils.dateToInt(new Date(p.date))));
        date.getTitul().setTextColor(Color.BLACK);
        date.getValue().setTextColor(Color.BLACK);
        date.getTitul().setTextSize(d);
        date.getValue().setTextSize(d);
        date.getTitul().setTypeface(Typeface.create(f, Typeface.NORMAL));
        date.getValue().setTypeface(Typeface.create(f, Typeface.NORMAL));
        date.getTitul().setWidth(100);



        mass.setPairString("Вес:",String.valueOf(p.mass));
        mass.getTitul().setTextColor(Color.BLACK);
        mass.getValue().setTextColor(Color.BLACK);
        mass.getTitul().setTextSize(d);
        mass.getValue().setTextSize(d);
        mass.getTitul().setTypeface(Typeface.create(f, Typeface.NORMAL));
        mass.getValue().setTypeface(Typeface.create(f, Typeface.NORMAL));


        pressure.setPairString("Давление:",p.pressure);
        pressure.getTitul().setTextColor(Color.BLACK);
        pressure.getValue().setTextColor(Color.BLACK);
        pressure.getTitul().setTextSize(d);
        pressure.getValue().setTextSize(d);
        pressure.getTitul().setTypeface(Typeface.create(f, Typeface.NORMAL));
        pressure.getValue().setTypeface(Typeface.create(f, Typeface.NORMAL));


        calories.setPairString("Калории:",String.valueOf(p.calories));
        calories.getTitul().setTextColor(Color.BLACK);
        calories.getValue().setTextColor(Color.BLACK);
        calories.getTitul().setTextSize(d);
        calories.getValue().setTextSize(d);
        calories.getTitul().setTypeface(Typeface.create(f, Typeface.NORMAL));
        calories.getValue().setTypeface(Typeface.create(f, Typeface.NORMAL));


        comment.setPairString("Комментарии:",p.commentary);
        comment.getTitul().setTextColor(Color.BLACK);
        comment.getValue().setTextColor(Color.BLACK);
        comment.getTitul().setTextSize(d);
        comment.getValue().setTextSize(d);
        comment.getTitul().setTypeface(Typeface.create(f, Typeface.NORMAL));
        comment.getValue().setTypeface(Typeface.create(f, Typeface.NORMAL));


        mView.setTag(p);
        return mView;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
