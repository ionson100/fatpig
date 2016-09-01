package com.omsk.bitnic.fatpig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;




    public class DialogRequest extends DialogFragment implements View.OnTouchListener{

        public interface IAction{
          void   action();
        }
        private IAction iAction;

        private View mView;

        public DialogRequest setView(View  mView){
            this.mView=mView;
            return this;
        }

        public DialogRequest setOnAction(IAction onAction){
            this.iAction=onAction;
            return  this;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if(mView!=null){
                mView.setVisibility(View.VISIBLE);
            }
        }



        private View selected_item = null;
        private int offset_x = 0;
        private int offset_y = 0;
        Boolean isTouch = false;
        boolean isDrop = false;
        ViewGroup.LayoutParams imageParams;
        ImageView imageSatelit;
        Button viewHost;
        int eX, eY;
        int topY, leftX, rightX, bottomY;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater vi;
            vi = LayoutInflater.from(getActivity());
            View v = vi.inflate(R.layout.dialog_request, null);
            builder.setView(v);

            View root = v.findViewById(R.id.base).getRootView();
            viewHost = (Button) v.findViewById(R.id.ImgDrop);

            viewHost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            imageSatelit = (ImageView) v.findViewById(R.id.img);

            imageSatelit.setOnTouchListener(this);

            root.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (isTouch) {
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                topY = viewHost.getTop();
                                leftX = viewHost.getLeft();
                                rightX = viewHost.getRight();
                                bottomY = viewHost.getBottom();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                eX = (int) event.getX();
                                eY = (int) event.getY();
                                int x = (int) event.getX() - offset_x;
                                int y = (int) event.getY() - offset_y;
                                int w = getActivity().getWindowManager().getDefaultDisplay().getWidth() - 50;
                                int h = getActivity().getWindowManager().getDefaultDisplay().getHeight() - 10;
                                if (x > w) x = w;
                                if (y > h) y = h;
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT));
                                lp.setMargins(x, y, 0, 0);

                                if (eX > leftX && eX < rightX && eY > topY && eY < bottomY) {
                                    viewHost.setEnabled(false);
                                    selected_item.bringToFront();
                                    isDrop = true;
                                    viewHost.setText(getString(R.string.ok));
                                } else {
                                    viewHost.setEnabled(true);
                                    viewHost.setText(R.string.close);
                                }
                                selected_item.setLayoutParams(lp);
                                break;
                            case MotionEvent.ACTION_UP:
                                isTouch = false;
                                if (isDrop) {
                                    isDrop = false;
                                } else {
                                    selected_item.setLayoutParams(imageParams);
                                }
                                if(!viewHost.isEnabled()){
                                    if(iAction!=null){
                                        iAction.action();
                                    }
                                }
                                dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                    return true;
                }
            });
            return builder.create();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    isTouch = true;
                    offset_x = (int) event.getX();
                    offset_y = (int) event.getY();
                    selected_item = v;
                    imageParams =  v.getLayoutParams();
                    break;
                case MotionEvent.ACTION_UP:
                    selected_item = null;
                    isTouch = false;

                    break;
                default:
                    break;
            }
            return false;
        }
    }


