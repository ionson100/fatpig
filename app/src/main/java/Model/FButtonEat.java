package Model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.omsk.bitnic.fatpig.ButtonBase;
import com.omsk.bitnic.fatpig.ButtonEat;
import com.omsk.bitnic.fatpig.DialigEditButton;
import com.omsk.bitnic.fatpig.IAction;
import com.omsk.bitnic.fatpig.ListAdapterButton;
import com.omsk.bitnic.fatpig.R;
import com.omsk.bitnic.fatpig.Settings;
import com.omsk.bitnic.fatpig.StateSystem;

import java.util.ArrayList;
import java.util.List;

import orm.Configure;


public class FButtonEat extends Fragment {

    private List<ButtonBase> mButtonList;
    private ListView mListView;
    private View mView;
    private ListAdapterButton mAdapterButton;


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;
        menu.add("Редактировать кнопку").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DialigEditButton dialigEditButton=new DialigEditButton();
                dialigEditButton.setButton(mButtonList.get(position)).addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {
                        activateList();
                    }
                }).show(getActivity().getSupportFragmentManager(),"sd");
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_button, container, false);
        mListView = (ListView) mView.findViewById(R.id.list_button);
        mListView.setOnCreateContextMenuListener(this);

        if(Settings.getSettings().getSateSystem()== StateSystem.BUTTON_EAT){

            List<ButtonEat> buttonEats= Configure.getSession().getList(ButtonEat.class,null);
            mButtonList=new ArrayList<ButtonBase>(buttonEats);
        }

        if(Settings.getSettings().getSateSystem()==StateSystem.BUTTON_WORK){

            List<ButtonWork> buttonwork= Configure.getSession().getList(ButtonWork.class,null);
            mButtonList=new ArrayList<ButtonBase>(buttonwork);
        }
        activateList();

        return mView;
    }

    private void activateList() {
         mAdapterButton =new ListAdapterButton(getActivity(),R.layout.item_list_button,mButtonList);
        mListView.setAdapter(mAdapterButton);
    }


}
