package com.omsk.bitnic.fatpig;


import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import Model.Sex;
import Model.User;

public class Utils {

    public static final String[] listABC = {"А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й",
            "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ы", "Э", "Ю", "Я"};

    public static double getCalorises(User user,Activity activity){
        double d=0;
        if(user.getSex()== Sex.women){
           d= (float) (10*user.weight + (6.25*user. growing)-(5 * user.age)-161);
        }
        if(user.getSex()== Sex.men){
             d= (float) (10*user.weight + 6.25*user.growing -5*user.age + 5);
        }
        double res=d*user.delta;

        FillData.fill(activity);
        return  Utils.round(res,2);
    }

    public static String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    public static int dateToInt(Date date) {
        return (int) (date.getTime() / 1000);
    }

    //bitnic.development@gmail.com
    private static Date intToDate(int i) {

        return new Date(((long) i * 1000));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double getCalorisesCore(User user) {
        double d=0;
        if(user.getSex()== Sex.women){
            d= (float) (10*user.weight + (6.25*user. growing)-(5 * user.age)-161);
        }
        if(user.getSex()== Sex.men){
            d= (float) (10*user.weight + 6.25*user.growing -5*user.age + 5);
        }
        double res=d*user.delta;


        return  Utils.round(res,2);
    }
}
