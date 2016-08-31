package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapterTrack extends ArrayAdapter<Track> {

    public List<Track> trackList;
    private int resource;
    public ListAdapterTrack(Context context, int resource, List<Track> objects) {
        super(context, resource, objects);
        this.trackList=objects;
        this.resource=resource;
    }
    public Track getTrack(int i){
        return trackList.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Track p = getItem(position);
        final View mView  = LayoutInflater.from(getContext()).inflate(R.layout.item_track, null);
        TextView trackDate= (TextView) mView.findViewById(R.id.track_date);
        trackDate.setText(p.name());
        mView.setTag(p);
        return mView;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


}
