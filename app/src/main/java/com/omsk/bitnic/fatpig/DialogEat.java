package com.omsk.bitnic.fatpig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import Model.OneEat;
import Model.Product;

/**
 * Created by USER on 10.08.2016.
 */
public class DialogEat extends DialogFragment {

    private static  String pref="Итого калорий: ";
    private IAction iAction;
    private Product product;
    OneEat oneEat=new OneEat();

    private IAction iActionDismiss;

    public DialogEat addIActionDismiss(IAction iAction){
        this.iActionDismiss=iAction;
        return  this;
    }
    public DialogEat addIAction(IAction iAction){
        this.iAction=iAction;
        return  this;
    }
    public DialogEat addProduct(Product product){
        this.product=product;
        return  this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(iActionDismiss!=null){
            iActionDismiss.Action(null);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_eat_product, null);
        builder.setView(v);

//        oneEat.product=product;
//        oneEat.product_id=product.id;
        oneEat.cal=product.calorieses;
        oneEat.isGramm=true;
        oneEat.date=Utils.dateToInt(new Date());


        final TextView textName= (TextView) v.findViewById(R.id.dialog_eat_name);
        textName.setText(product.name + " (" + String.valueOf(product.calorieses) + "ккал)");

        EditText editTextAmount= (EditText) v.findViewById(R.id.dialog_eat_amount);

        final TextView textTotal= (TextView) v.findViewById(R.id.dialog_eat_cal_total);
        textTotal.setText(pref + "0");

        final RadioButton buttonGramm= (RadioButton) v.findViewById(R.id.radio_button_gram);
        final RadioButton buttonOne= (RadioButton) v.findViewById(R.id.radio_button_one);

        buttonGramm.setChecked(true);

        buttonGramm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textTotal.setText(pref + String.valueOf((oneEat.amount / 100) * product.calorieses));
                    oneEat.isGramm=true;

                }
            }
        });
        buttonOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textTotal.setText(pref + String.valueOf((oneEat.amount ) * product.calorieses));
                    oneEat.isGramm=false;
                }
            }
        });







        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    oneEat.amount = 0;
                    textTotal.setText(pref + "0");
                } else {
                    oneEat.amount = Integer.parseInt(s.toString());
                    if (buttonGramm.isChecked()) {
                        textTotal.setText(pref + String.valueOf((oneEat.amount / 100) * product.calorieses));
                    } else {
                        textTotal.setText(pref + String.valueOf((oneEat.amount ) * product.calorieses));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        Button buttonCancel= (Button) v.findViewById(R.id.dialog_eat_cancal);
        Button buttonOk= (Button) v.findViewById(R.id.dialog_eat_ok);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iAction != null) {
                    iAction.Action(oneEat);
                }
                dismiss();
            }
        });

        return builder.create();
    }


}
