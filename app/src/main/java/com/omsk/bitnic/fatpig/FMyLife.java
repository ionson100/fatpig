package com.omsk.bitnic.fatpig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import Model.Life;
import linq.Linq;
import linq.Predicate;
import orm.Configure;


/**
 * A simple {@link Fragment} subclass.
 */
public class FMyLife extends Fragment {

    private ListAdapterLife mAdapterLife;
    private ListView mListView;
    private List<Life> mLifeList;
    private View mView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;
        menu.add("Редактировать").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       mView=inflater.inflate(R.layout.fragment_fmylife, container, false);

        mListView= (ListView) mView.findViewById(R.id.list_life);
        mLifeList= Configure.getSession().getList(Life.class,null);

        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar today = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        final long idd = today.getTime().getTime();

        Life life = Linq.toStream(mLifeList).firstOrDefault(new Predicate<Life>() {
            @Override
            public boolean apply(Life t) {
                return t.date==idd;
            }
        });
        if(life==null){
            Life l=new Life();
            l.date=idd;
            mLifeList.add(l);
        }







        mListView.setOnCreateContextMenuListener(this);
        activateList();


        return mView;
    }

    public void activateList(){
        mAdapterLife=new ListAdapterLife(getActivity(),R.layout.item_list_life,mLifeList);
        mListView.setAdapter(mAdapterLife);
    }

}
