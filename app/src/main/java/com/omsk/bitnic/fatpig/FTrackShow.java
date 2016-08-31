package com.omsk.bitnic.fatpig;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import Model.GeoData;
import Model.User;
import linq.Function;
import linq.Linq;
import orm.Configure;


/**
 * A simple {@link Fragment} subclass.
 */
public class FTrackShow extends Fragment {
    private ListAdapterTrack track;
    private View mView;
    private ListView mListView;
    private List<Track> mListTracks=new ArrayList<>();
    User user;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;

        menu.add(1,1,1,"Показать на карте").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Settings.getSettings().trackshow= track.getTrack(position);
                Settings.getSettings().setStateSystem(StateSystem.MAP_TRACK,getActivity());
                return true;
            }
        });

        menu.add(1,1,1,"Показать данные").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Utils.messageBoxTrackData(track.getTrack(position),getActivity(),user);

                return true;
            }
        });


        menu.add(1,1,1,"Удалить").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Configure.getSession().execSQL(" delete from track_data where track_name = "+ String.valueOf(track.getTrack(position).trackName));
                List<GeoData> geoDatas= Configure.getSession().getList(GeoData.class,null);
                Map<Integer, List<GeoData>> dataMap = Linq.toStream(geoDatas).groupBy(new Function<GeoData, Integer>() {
                    @Override
                    public Integer foo(GeoData t) {
                        return t.trackName;
                    }
                });
                mListTracks.clear();
                for (Integer ss : dataMap.keySet()) {
                    Track track = new Track();
                    track.trackName =ss;
                    track.list=dataMap.get(ss);
                    mListTracks.add(track);
                }
                Collections.sort(mListTracks, new Comparator<Track>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(Track lhs, Track rhs) {
                        return Long.compare(lhs.trackName,rhs.trackName);
                    }
                });
                activateList(mListTracks);
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_ftrack_show, container, false);
        mListView= (ListView) mView.findViewById(R.id.list_track);
        mListView.setOnCreateContextMenuListener(this);
        List<User> userList=Configure.getSession().getList(User.class,null);
        if(userList.size()>0){
            user=userList.get(0);
        }

        List<GeoData> geoDatas= Configure.getSession().getList(GeoData.class,null);

        Map<Integer, List<GeoData>> dataMap = Linq.toStream(geoDatas).groupBy(new Function<GeoData, Integer>() {
            @Override
            public Integer foo(GeoData t) {
                return t.trackName;
            }
        });

        for (Integer ss : dataMap.keySet()) {
            Track track = new Track();
            track.trackName =ss;
            track.list=dataMap.get(ss);
            mListTracks.add(track);
        }
        Collections.sort(mListTracks, new Comparator<Track>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(Track lhs, Track rhs) {
                return Long.compare(lhs.trackName,rhs.trackName);
            }
        });

        activateList(mListTracks);
        return mView;
    }

    private void activateList(List<Track> tracks) {

        track=new ListAdapterTrack(getActivity(),0,tracks);
        mListView.setAdapter(track);

    }

}
