package com.omsk.bitnic.fatpig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.Product;
import Transceiver.Transceiver;

/**
 * Created by USER on 10.08.2016.
 */
public class DialogAddOdEditProduct extends DialogFragment {


    private IAction iAction;
    private Product product;

    public DialogAddOdEditProduct addIAction(IAction iAction){
        this.iAction=iAction;
        return  this;
    }
    public DialogAddOdEditProduct addProduct(Product product){
        this.product=product;
        return  this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_add_product, null);
        builder.setView(v);
        final EditText editTextName= (EditText) v.findViewById(R.id.dialog_add_name);
        final EditText editTextCal= (EditText) v.findViewById(R.id.dialog_add_calor);
        Button buttonCancel= (Button) v.findViewById(R.id.bt_dialog_add_canell);
        Button buttonOk= (Button) v.findViewById(R.id.bt_dialog_add_ok);
        final CheckBox checkBox= (CheckBox) v.findViewById(R.id.checkBox_pref);

        if(product!=null){
            editTextCal.setText(String.valueOf(product.calorieses));
            editTextName.setText(product.name);
            checkBox.setChecked(product.preferences);
        }


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextCal.getText().toString().trim().length()==0||editTextName.getText().toString().trim().length()==0){
                    Toast.makeText(getActivity(), getString(R.string.error1), Toast.LENGTH_SHORT).show();

                }else{
                    if(product==null){
                        Product p=new Product();
                        p.preferences=checkBox.isChecked();
                        p.name=editTextName.getText().toString();
                        p.calorieses=Double.parseDouble(editTextCal.getText().toString());
                        if(iAction!=null){
                            iAction.Action(p);
                        }
                        dismiss();
                    }else{

                        product.preferences=checkBox.isChecked();
                        product.name=editTextName.getText().toString();
                        product.calorieses=Double.parseDouble(editTextCal.getText().toString());
                        if(iAction!=null){
                            iAction.Action(product);
                        }
                        dismiss();
                    }

                }

            }
        });

        return builder.create();
    }
}
