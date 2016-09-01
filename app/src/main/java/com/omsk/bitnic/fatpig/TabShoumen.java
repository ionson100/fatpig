package com.omsk.bitnic.fatpig;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import Model.OneEat;
import orm.Configure;

/**
 * Created by USER on 01.09.2016.
 */
public class TabShoumen {
    public static void showTab1(View mView, final FragmentActivity activity) {

       // Configure.getSession().execSQL("delete from one_eat");
        final LinearLayout linearLayout= (LinearLayout) mView.findViewById(R.id.tab1_panel);
        Button b1= (Button) mView.findViewById(R.id.t1bt1);
        Button b2= (Button) mView.findViewById(R.id.t1bt2);
        Button b3= (Button) mView.findViewById(R.id.t1bt3);
        Button b4= (Button) mView.findViewById(R.id.t1bt4);
        List<ButtonEat> eatList= Configure.getSession().getList(ButtonEat.class,null);
        if(eatList.size()!=4){
            Toast.makeText(activity, "не соответствие кнопок", Toast.LENGTH_SHORT).show();
            return;
        }
        b1.setTag(eatList.get(0));
        b2.setTag(eatList.get(1));
        b3.setTag(eatList.get(2));
        b4.setTag(eatList.get(3));
        b1.setText(eatList.get(0).name);
        b2.setText(eatList.get(1).name);
        b3.setText(eatList.get(2).name);
        b4.setText(eatList.get(3).name);
        if(eatList.get(0).ishow){
            b1.setVisibility(View.VISIBLE);
        }else{
            b1.setVisibility(View.GONE);
        }

        if(eatList.get(1).ishow){
            b2.setVisibility(View.VISIBLE);
        }else{
            b2.setVisibility(View.GONE);
        }
        if(eatList.get(2).ishow){
            b3.setVisibility(View.VISIBLE);
        }else{
            b3.setVisibility(View.GONE);
        }
        if(eatList.get(3).ishow){
            b4.setVisibility(View.VISIBLE);
        }else{
            b4.setVisibility(View.GONE);
        }
        View.OnClickListener click1=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogRequest d=new DialogRequest();
                d.setView(linearLayout);
                d.setOnAction(new DialogRequest.IAction() {
                    @Override
                    public void action() {
                        OneEat oneEat=new OneEat();
                        ButtonEat b= (ButtonEat) v.getTag();
                        oneEat.amount=1;
                        oneEat.date=Utils.dateToInt(new Date());
                        oneEat.isGramm=false;
                        oneEat.cal=b.calories;
                        Configure.getSession().insert(oneEat);
                        FillData.fill(activity);

                    }
                });
                linearLayout.setVisibility(View.INVISIBLE);
                d.show(activity.getSupportFragmentManager(),"sd");
            }
        };
        b1.setOnClickListener(click1);
        b2.setOnClickListener(click1);
        b3.setOnClickListener(click1);
        b4.setOnClickListener(click1);




    }

    public static void showTab2(View mView, final FragmentActivity activity) {

        final LinearLayout linearLayout= (LinearLayout) mView.findViewById(R.id.tab1_panel1);
        Button b1= (Button) mView.findViewById(R.id.t1bt1);
        Button b2= (Button) mView.findViewById(R.id.t1bt2);
        Button b3= (Button) mView.findViewById(R.id.t1bt3);
        Button b4= (Button) mView.findViewById(R.id.t1bt4);
        List<ButtonEat> eatList= Configure.getSession().getList(ButtonEat.class,null);
        if(eatList.size()!=4){
            Toast.makeText(activity, "не соответствие кнопок", Toast.LENGTH_SHORT).show();
            return;
        }
        b1.setTag(eatList.get(0));
        b2.setTag(eatList.get(1));
        b3.setTag(eatList.get(2));
        b4.setTag(eatList.get(3));
        b1.setText(eatList.get(0).name);
        b2.setText(eatList.get(1).name);
        b3.setText(eatList.get(2).name);
        b4.setText(eatList.get(3).name);
        if(eatList.get(0).ishow){
            b1.setVisibility(View.VISIBLE);
        }else{
            b1.setVisibility(View.GONE);
        }

        if(eatList.get(1).ishow){
            b2.setVisibility(View.VISIBLE);
        }else{
            b2.setVisibility(View.GONE);
        }
        if(eatList.get(2).ishow){
            b3.setVisibility(View.VISIBLE);
        }else{
            b3.setVisibility(View.GONE);
        }
        if(eatList.get(3).ishow){
            b4.setVisibility(View.VISIBLE);
        }else{
            b4.setVisibility(View.GONE);
        }
        View.OnClickListener click1=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DialogRequest d=new DialogRequest();
                d.setView(linearLayout);
                d.setOnAction(new DialogRequest.IAction() {
                    @Override
                    public void action() {


                    }
                });
                linearLayout.setVisibility(View.INVISIBLE);
                d.show(activity.getSupportFragmentManager(),"sd");
            }
        };
        b1.setOnClickListener(click1);
        b2.setOnClickListener(click1);
        b3.setOnClickListener(click1);
        b4.setOnClickListener(click1);



    }

}
