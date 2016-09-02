package com.omsk.bitnic.fatpig;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import Model.GeoData;
import Model.OneEat;
import Model.OneWork;
import Model.User;
import linq.Function;
import linq.Linq;
import orm.Configure;


public class FillData {

    public static void fill(Activity activity){

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        int pecent=Settings.getSettings().getPercent();
        LinearLayout linearLayoutTop= (LinearLayout) activity.findViewById(R.id.panel_top);

        //linearLayoutTop.setBackgroundColor(Settings.getSettings().colorTopPanel);

        LinearLayout panel1= (LinearLayout) activity.findViewById(R.id.panel1);
        LinearLayout panel2= (LinearLayout) activity.findViewById(R.id.panel2);
        LinearLayout panel3= (LinearLayout) activity.findViewById(R.id.panel3);
        LinearLayout panel4= (LinearLayout) activity.findViewById(R.id.panel4);

//        panel1.setBackgroundColor(Settings.getSettings().colorPanel1);
//        panel2.setBackgroundColor(Settings.getSettings().colorPanel2);
//        panel3.setBackgroundColor(Settings.getSettings().colorPanel3);
//        panel4.setBackgroundColor(Settings.getSettings().colorPanel4);



        final int  del=280;


        TextView textView1= (TextView) activity.findViewById(R.id.panel_text1);
        TextView textView2= (TextView) activity.findViewById(R.id.panel_text2);
        TextView textView3= (TextView) activity.findViewById(R.id.panel_text3);
        TextView textView4= (TextView) activity.findViewById(R.id.panel_text4);

        List<User> users= Configure.getSession().getList(User.class,null);
        if(users.size()==0)return;
        User user=users.get(0);
        double total=Utils.getCalorisesCore(user);
        textView1.setText(String.valueOf(total) + " ккал.");
        {
            textView2.setText(String.valueOf(Utils.round(total/100*pecent,1))+" ккал.");
            int i2= (width/100)*pecent;
            ViewGroup.LayoutParams params = panel2.getLayoutParams();
            if(i2<del){
                i2=del;
            }
            params.width = i2;
            panel2.setLayoutParams(params);
        }
        double d= total/100;
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar today = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        long logdel=today.getTime().getTime();
        int idd=Utils.dateToInt(today.getTime());

        {




            List<OneEat> oneEatList=Configure.getSession().getList(OneEat.class," date > "+String.valueOf(idd));
            double accum=0;
            for (OneEat oneEat : oneEatList) {
                if(oneEat.isGramm){
                    accum=accum+(oneEat.amount/100)*oneEat.cal;
                }else{
                    accum=accum+(oneEat.amount*oneEat.cal);
                }
            }

            double prcent=accum/d;
            ViewGroup.LayoutParams params = panel3.getLayoutParams();
            params.width = (int) ((width/100)*prcent);
            if(params.width<del){
                params.width=del;
            }
            panel3.setLayoutParams(params);
            textView3.setText(String.valueOf(Utils.round(accum,1))+" ккал.");

        }
        {





            List<GeoData> geoDatas=Configure.getSession().getList(GeoData.class," track_name > "+String.valueOf(idd));//
            Map<Integer, List<GeoData>> dataMap = Linq.toStream(geoDatas).groupBy(new Function<GeoData, Integer>() {
                @Override
                public Integer foo(GeoData t) {
                    return t.trackName;
                }
            });

            double cal=0;
            for (Integer ss : dataMap.keySet()) {
                cal=cal+ Calculation.getCalories(dataMap.get(ss),user);
            }

            List<OneWork> oneWorks= Configure.getSession().getList(OneWork.class, "date_start >= "+String.valueOf(logdel));

            for (OneWork oneWork : oneWorks) {
                cal=cal+oneWork.getCalories(user);
            }





            width=width-(width/100)*10;// уменьшение из за того что вставил кнопку
            double prcent=cal/d;
            ViewGroup.LayoutParams params = panel4.getLayoutParams();
            params.width = (int) ((width/100)*prcent);
            if(params.width<del){
                params.width=del;
            }
            panel4.setLayoutParams(params);
            textView4.setText(String.valueOf(Utils.round(cal,1))+" ккал.");
        }


    }



}
