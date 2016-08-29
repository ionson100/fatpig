package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Pupper extends LinearLayout {
    private TextView textViewTitul;
    private TextView textViewValue;

    public Pupper(Context context) {
        super(context);
        init();
    }

    public Pupper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Pupper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TextView getTitul() {
        return textViewTitul;
    }

    public TextView getValue() {
        return textViewValue;
    }

    public void setPairString(int titul, int value) {
        textViewTitul.setText(titul);
        textViewValue.setText(value);
    }

    public void setPairString(String titul, int value) {
        textViewTitul.setText(titul);
        textViewValue.setText(value);
    }

    public void setPairString(int titul, String value) {
        textViewTitul.setText(titul);
        textViewValue.setText(value);
    }

    public void setPairString(String titul, String value) {
        textViewTitul.setText(titul);
        textViewValue.setText(value);
    }

    private void init() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.pupper, null, false);
        textViewTitul = (TextView) view.findViewById(R.id.pupper_titul);
        textViewValue = (TextView) view.findViewById(R.id.pupper_value);
        this.addView(view);
    }

}