package com.omsk.bitnic.fatpig;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import Model.Work;


public class ListAdapterButton  extends ArrayAdapter<ButtonBase> {

    private List<ButtonBase> mButtonList;
      public ListAdapterButton(FragmentActivity activity, int item_list_button, List<ButtonBase> mButtonList) {
        super(activity,item_list_button,mButtonList);
        this.mButtonList=mButtonList;

    }



    public ButtonBase getButtonBase(int i){
       return mButtonList.get(i);
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        final ButtonBase p = getItem(position);
        View mView  = LayoutInflater.from(getContext()).inflate(R.layout.item_list_button, null);
        TextView textView= (TextView) mView.findViewById(R.id.button_name);
        String s="скрыта";
        if(p.ishow){
            s="открыта";
        }
        textView.setText(p.name+"  cal-"+String.valueOf(p.calories)+" "+s);

        mView.setTag(p);
        return mView;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
