package com.omsk.bitnic.fatpig;

import android.location.Location;

import java.util.Date;
import java.util.List;

import Model.GeoData;
import Model.User;

/**
 * Created by USER on 29.08.2016.
 */
public class Calculation {


    //Е=0,007 x V2 + 21,   где V -скорость ходьбы  в м/мин , Е—расход энергии (кал/кг/мин).
   // Е =18,0 x V - 20,   где V—скорость бега (км/час), Е—расход энергии (кал/кг/мин).

  public static   double getDistance(List<GeoData> geoDatas){
        double res=0;
        if(geoDatas.size()<=1)return 0d;
        for (int i = 0; i < geoDatas.size(); i++) {
            int is=i+1;
            if(is>=geoDatas.size()) break;
            Location location1=new Location("locationA");
            location1.setLatitude(geoDatas.get(i).latitude);
            location1.setLongitude(geoDatas.get(i).longitude);
            Location location11=new Location("locationA1");
            location11.setLatitude(geoDatas.get(is).latitude);
            location11.setLongitude(geoDatas.get(is).longitude);
            res=res+location1.distanceTo(location11);
        }
        return  res/1000;
    }

    public static double getSpeed(List<GeoData> geoDatas){

        if(geoDatas.size()<=1) return 0;

        return speed(geoDatas.get(geoDatas.size()-2),geoDatas.get(geoDatas.size()-1));


    }





     private static double speed(GeoData d1,GeoData d2){

         Location location1=new Location("locationA");
         location1.setLatitude(d1.latitude);
         location1.setLongitude(d1.longitude);
         Location location11=new Location("locationA1");
         location11.setLatitude(d2.latitude);
         location11.setLongitude(d2.longitude);
         float ff=location1.distanceTo(location11);
         if(ff==0) return 0;
         long time=d2.date-d1.date;

         double dd=(((double)time)/1000)/60/60;// час;
         double dis=ff/1000; // км

         return dis/dd;
        // return d2.speed/1000f/60f;
     }

    public double speedCole(double distancion,long startTime){
        long time=new Date().getTime()-startTime;
        double dd=(((double)time)/1000)/60/60;// час;
        double dis=distancion/1000; // км

        return dis/dd;
    }

    public static double getCalories(List<GeoData> geoDatas, User user){

        if(geoDatas.size()<=1) return 0;
        if(user==null) return 0;

        double calories=0;

        for (int i = 0; i < geoDatas.size(); i++) {
            int is=i+1;
            if(is>=geoDatas.size()) break;

            double speed=speed(geoDatas.get(i),geoDatas.get(is));
            if(speed>6){
                // Е =18,0 x V - 20,   где V—скорость бега (км/час), Е—расход энергии (кал/кг/мин).
                double dd=(18d*speed-20)*(double)user.weight;
                long sd=geoDatas.get(is).date-geoDatas.get(i).date;
                double ddd=dd*(((double)sd)/1000/60);
                calories=calories+ddd;

            }else{
               // Е=0,007 x V2 + 21,   где V -скорость ходьбы  в м/мин , Е—расход энергии (кал/кг/мин).
                double dd=(0.007*speed+21)*(double)user.weight;
                long sd=geoDatas.get(is).date-geoDatas.get(i).date;
                double ddd=dd*(((double)sd)/1000/60);
                calories=calories+ddd;
            }
        }
        return  calories/1000;
    }
}
