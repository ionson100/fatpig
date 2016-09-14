package com.omsk.bitnic.fatpig;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.util.List;
import Model.GeoData;


 class Painter {
    private static  MyView view;
    private static  List<GeoData> mGeoDatas;

    static View getView(Context context, List<GeoData> mGeoDatas){
        Painter.mGeoDatas=mGeoDatas;
        view = new MyView(context);
        return view;
    }

     static void invalidate(List<GeoData> mGeoDatas){
        Painter.mGeoDatas=mGeoDatas;
        if(view!=null){
            view.invalidate();
        }
    }

     private static class MyView extends View {

        private Paint paint = new Paint();
        private Paint paint1=new Paint();

        public MyView(Context context) {
            super(context);
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
