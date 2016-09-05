package com.omsk.bitnic.fatpig;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import Model.Sex;
import Model.User;

public class Utils {

    public static final String[] listABC = {"А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й",
            "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ы", "Э", "Ю", "Я"};

    public static double getCalorises(User user, Activity activity) {
        double d = 0;
        if (user.getSex() == Sex.women) {
            d = (float) (10 * user.weight + (6.25 * user.growing) - (5 * user.age) - 161);
        }
        if (user.getSex() == Sex.men) {
            d = (float) (10 * user.weight + 6.25 * user.growing - 5 * user.age + 5);
        }
        double res = d * user.delta;

        FillData.fill(activity);
        return Utils.round(res, 2);
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



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double getCalorisesCore(User user) {
        double d = 0;
        if (user.getSex() == Sex.women) {
            d = (float) (10 * user.weight + (6.25 * user.growing) - (5 * user.age) - 161);
        }
        if (user.getSex() == Sex.men) {
            d = (float) (10 * user.weight + 6.25 * user.growing - 5 * user.age + 5);
        }
        double res = d * user.delta;


        return Utils.round(res, 2);
    }

    public static String getStringDecimal(int value) {
        return String.format("%02d", value);
    }

    public static void messageBox(final String title, final String message, final Activity activity, final IAction iAction) {
        if (activity == null) return;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setTitle(title)
                        .setMessage(message)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (iAction != null) {
                                    iAction.Action(null);
                                }
                                dialog.dismiss();
                            }

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public static String simpleDateFormat(long date) {
        if (date <= 0) {
            return "нет данных";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(new Date(date));
    }

    public static String simpleDateFormatE(long date) {
        if (date <= 0) {
            return "нет данных";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(new Date(date));
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void messageBoxTrackData(final Track track, final FragmentActivity activity, User user) {
        String string = "Нет данных.";
        if (track.list.size() > 0) {

            long delta = track.list.get(track.list.size() - 1).date - track.list.get(0).date;

            double distance = Calculation.getDistance(track.list);

            double dd = (((double) delta) / 1000) / 60 / 60;// час;
            double speed = distance / dd; // км


            int m = (int) ((delta / 1000) / 60);


            string = "Время старта:                          " + Utils.simpleDateFormat(track.list.get(0).date) + "\n" +
                    "Время финиша:                       " + Utils.simpleDateFormat(track.list.get(track.list.size() - 1).date) + "\n" +
                    "Расстояние (км.):                   " + String.valueOf(Utils.round(distance, 2)) + "\n" +
                    "Средняя скорость (км./ч.): " + String.valueOf(Utils.round(speed, 2)) + "\n" +
                    "Время в пути (мин.):              " + String.valueOf(m) + " \n" +
                    "Расход калорий (ккал):         " + String.valueOf(Utils.round(Calculation.getCalories(track.list, user), 2));
        }


        final String finalString = string;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder
                        .setTitle(Utils.simpleDateFormat(track.trackName))
                        .setMessage(finalString)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
}
