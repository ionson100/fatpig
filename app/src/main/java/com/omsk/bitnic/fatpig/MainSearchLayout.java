//package com.omsk.bitnic.fatpig;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//
//public class MainSearchLayout extends LinearLayout {
//
//    public MainSearchLayout(Context context, AttributeSet attributeSet) {
//        super(context, attributeSet);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.fragment_product, this);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d("Search Layout", "Handling Keyboard Window shown");
//
//        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
//        final int actualHeight = getHeight();
//
//        if (actualHeight > proposedheight){
//            // Keyboard is shown
//
//        } else {
//            // Keyboard is hidden
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//}
