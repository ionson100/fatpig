package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.GeoData;
import Model.User;

public class ListAdapterTrack extends ArrayAdapter<Track> {

    private Context context;
    public List<Track> trackList;
    private int resource;
    User user;

    public ListAdapterTrack(Context context, int resource, List<Track> objects) {
        super(context, resource, objects);
        this.context = context;
        this.trackList = objects;
        this.resource = resource;
        user=User.getUser();
    }

    public Track getTrack(int i) {
        return trackList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Track p = getItem(position);
        final View mView = LayoutInflater.from(getContext()).inflate(R.layout.item_track, null);
        TextView trackDate = (TextView) mView.findViewById(R.id.track_date);
        trackDate.setText(p.name());
        TextView trackCount = (TextView) mView.findViewById(R.id.track_count);
        trackCount.setText(String.valueOf(p.list.size()));
        mView.setTag(p);
        //////////////////////////////////////

        TextView  textView= (TextView) mView.findViewById(R.id.item_text);
        long delta = p.list.get(p.list.size() - 1).date - p.list.get(0).date;

        double distance = Calculation.getDistance(p.list);

        double dd = (((double) delta) / 1000) / 60 / 60;// час;
        double speed = distance / dd; // км


        int m = (int) ((delta / 1000) / 60);

        String ss = "Время старта:                          " + Utils.simpleDateFormat(p.list.get(0).date) + "\n" +
                "Время финиша:                       " + Utils.simpleDateFormat(p.list.get(p.list.size() - 1).date) + "\n" +
                "Расстояние (км.):                   " + String.valueOf(Utils.round(distance, 2)) + "\n" +
                "Средняя скорость (км./ч.): " + String.valueOf(Utils.round(speed, 2)) + "\n" +
                "Время в пути (мин.):              " + String.valueOf(m) + " \n" +
                "Расход калорий (ккал):         " + String.valueOf(Utils.round(Calculation.getCalories(p.list, user), 2));
        textView.setText(ss);

        LinearLayout linearLayout= (LinearLayout) mView.findViewById(R.id.w33);

        linearLayout.addView(new MyView(context,p.list));

        ///////////////////////////////////////////
        return mView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    private  class MyView extends View {
        private List<GeoData> mGeoDatas;
        private Paint paint = new Paint();
        private Paint paint1 = new Paint();

        public MyView(Context context, List<GeoData> mGeoDatas) {

            super(context);
            this.mGeoDatas = mGeoDatas;
            paint1.setAntiAlias(true);
            paint1.setStrokeWidth(1f);
            paint1.setColor(Color.WHITE);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setStrokeJoin(Paint.Join.ROUND);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Utils.drawPaint(canvas,paint,paint1,mGeoDatas);
        }
    }


}
