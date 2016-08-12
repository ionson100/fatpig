package com.omsk.bitnic.fatpig;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import Model.OneEat;
import Model.User;
import orm.Configure;

/**
 * Created by USER on 10.08.2016.
 */
public class FillData {

    public static void fill(Activity activity){

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        int pecent=Settings.getSettings().getPercent();
        LinearLayout linearLayoutTop= (LinearLayout) activity.findViewById(R.id.panel_top);

        linearLayoutTop.setBackgroundColor(Settings.getSettings().colorTopPanel);

        LinearLayout panel1= (LinearLayout) activity.findViewById(R.id.panel1);
        LinearLayout panel2= (LinearLayout) activity.findViewById(R.id.panel2);
        LinearLayout panel3= (LinearLayout) activity.findViewById(R.id.panel3);
        LinearLayout panel4= (LinearLayout) activity.findViewById(R.id.panel4);

        panel1.setBackgroundColor(Settings.getSettings().colorPanel1);
        panel2.setBackgroundColor(Settings.getSettings().colorPanel2);
        panel3.setBackgroundColor(Settings.getSettings().colorPanel3);
        panel4.setBackgroundColor(Settings.getSettings().colorPanel4);





        TextView textView1= (TextView) activity.findViewById(R.id.panel_text1);
        TextView textView2= (TextView) activity.findViewById(R.id.panel_text2);
        TextView textView3= (TextView) activity.findViewById(R.id.panel_text3);
        TextView textView4= (TextView) activity.findViewById(R.id.panel_text4);

        List<User> users= Configure.getSession().getList(User.class,null);
        if(users.size()==0)return;
        User user=users.get(0);
        double total=Utils.getCalorises(user);
        textView1.setText(String.valueOf(total) + "    Общее количество  расчетных ккал для ващего тельца.".toUpperCase());
        {
            textView2.setText(String.valueOf(Utils.round(total/100*pecent,1))+"    Требуется ккал для похудания вашего тельца.".toUpperCase());
            int i2= (width/100)*pecent;
            ViewGroup.LayoutParams params = panel2.getLayoutParams();
            params.width = i2;
            panel2.setLayoutParams(params);
        }
        {

            Calendar calendar=Calendar.getInstance();
            int houer=calendar.get(Calendar.HOUR);
            int min=calendar.get(Calendar.MINUTE);
            int sec=calendar.get(Calendar.SECOND);
            int ds=Utils.dateToInt(new Date())-houer*60*60 -min*60-sec;


            List<OneEat> oneEatList=Configure.getSession().getList(OneEat.class," date > "+String.valueOf(ds));
            double accum=0;
            for (OneEat oneEat : oneEatList) {
                if(oneEat.isGramm){
                    accum=accum+(oneEat.amount/100)*oneEat.cal;
                }else{
                    accum=accum+(oneEat.amount*oneEat.cal);
                }
            }
            double d= total/100;
            double prcent=accum/d;
            ViewGroup.LayoutParams params = panel3.getLayoutParams();
            params.width = (int) ((width/100)*prcent);
            panel3.setLayoutParams(params);
            textView3.setText(String.valueOf(Utils.round(accum,1)));

        }


    }

}
