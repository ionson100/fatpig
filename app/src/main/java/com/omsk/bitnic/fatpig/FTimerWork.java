package com.omsk.bitnic.fatpig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.util.Date;
import java.util.List;

import Model.Life;
import Model.OneWork;
import Model.User;
import orm.Configure;


/**
 * A simple {@link Fragment} subclass.
 */
public class FTimerWork extends Fragment {

    public final static int START=0;
    public final static int PAUSE=1;
    public final static int STOP=2;


    private View mView;
    private PausableChronometerWork chronometer;
    private ImageButton bt1,bt2,bt3;
    Pupper calories;
    OneWork mOneWork;
    User user;


    public FTimerWork() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.fragment_ftimer_work, container, false);
        chronometer= (PausableChronometerWork) mView.findViewById(R.id.chronometer);
        calories= (Pupper) mView.findViewById(R.id.calories);

        calories.getTitul().setTextSize(25f);
        calories.getValue().setPadding(20,0,0,0);

        user=User.getUser();




        bt1= (ImageButton) mView.findViewById(R.id.tarack_run);
        bt2= (ImageButton) mView.findViewById(R.id.tarack_pause);
        bt3= (ImageButton) mView.findViewById(R.id.tarack_stop);

        List<OneWork> oneWorks = Configure.getSession().getList(OneWork.class," 1=1 ORDER BY id DESC LIMIT 1; ");
        if(oneWorks.size()==1&&oneWorks.get(0).date_finish==0d){
            mOneWork=oneWorks.get(0);
            switch (ChronometerWork.getCore().state){
                case 0:{
                    bt1.setEnabled(false);
                    bt2.setEnabled(true);
                    bt3.setEnabled(true);
                    chronometer.setCurrentTime((new Date().getTime() - oneWorks.get(0).date_start)*-1);
                    chronometer.start();
                    break;
                }
                case 1:{
                    bt1.setEnabled(true);
                    bt2.setEnabled(false);
                    bt3.setEnabled(true);
                    chronometer.setCurrentTime(ChronometerWork.getCore().timeWhenStopped);

                    break;
                }
                case 2:{
                    bt1.setEnabled(true);
                    bt2.setEnabled(false);
                    bt3.setEnabled(false);
                    chronometer.setCurrentTime(0);
                    break;
                }
            }
        }

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRequest dialog=new DialogRequest();
                dialog.setOnAction(new DialogRequest.IAction() {
                    @Override
                    public void action() {
                        start();
                    }
                }).show(getActivity().getSupportFragmentManager(),"sd");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRequest dialog=new DialogRequest();
                dialog.setOnAction(new DialogRequest.IAction() {
                    @Override
                    public void action() {
                        pause();
                    }
                }).show(getActivity().getSupportFragmentManager(),"sd");
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRequest dialog=new DialogRequest();
                dialog.setOnAction(new DialogRequest.IAction() {
                    @Override
                    public void action() {
                        stop();
                    }
                }).show(getActivity().getSupportFragmentManager(),"sd");
            }
        });


        ////////////////////////////////////
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                calories.setPairString("Калории: ",String.valueOf(mOneWork.getCalories(user)));
                FillData.fill(getActivity());
            }
        });

        return mView;
    }
    void start(){
        bt1.setEnabled(false);
        bt2.setEnabled(true);
        bt3.setEnabled(true);
        ChronometerWork.getCore().state=START;
        ChronometerWork.Save();;
        chronometer.start();
    }

    void pause(){
        bt1.setEnabled(true);
        bt2.setEnabled(false);
        bt3.setEnabled(true);
        chronometer.stop();
        ChronometerWork.getCore().state=PAUSE;
        ChronometerWork.Save();;
    }

    void stop(){
        bt1.setEnabled(true);
        bt2.setEnabled(false);
        bt3.setEnabled(false);
        chronometer.reset();
        ChronometerWork.getCore().state=0;
        ChronometerWork.Save();

        List<OneWork> oneWorks = Configure.getSession().getList(OneWork.class," 1=1 ORDER BY id DESC LIMIT 1; ");
        if(oneWorks.size()==1){
            oneWorks.get(0).date_finish=new Date().getTime();
            Configure.getSession().update(oneWorks.get(0));
        }
        Settings.getSettings().setStateSystem(StateSystem.HOME,getActivity());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ChronometerWork.getCore().timeWhenStopped=chronometer.getCurrentTime();
        ChronometerWork.Save();
    }
}
