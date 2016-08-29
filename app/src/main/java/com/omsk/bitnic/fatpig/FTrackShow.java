package com.omsk.bitnic.fatpig;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import Model.GeoData;
import linq.Function;
import linq.Linq;
import orm.Configure;


/**
 * A simple {@link Fragment} subclass.
 */
public class FTrackShow extends Fragment {

    private View mView;
    private ListView mListView;
    private List<Track> mListTracks=new ArrayList<>();

    public FTrackShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_ftrack_show, container, false);
        mListView= (ListView) mView.findViewById(R.id.list_track);

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

        ListAdapterTrack track=new ListAdapterTrack(getActivity(),0,tracks);
        mListView.setAdapter(track);

    }

}
