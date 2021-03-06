package com.omsk.bitnic.fatpig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.List;

import Model.OneWork;
import orm.Configure;

/**
 * A simple {@link Fragment} subclass.
 */
public class FHome extends Fragment {


    private View mView;
    private TabHost mTabHost;
    private List<OneWork> oneWorks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        oneWorks = Configure.getSession().getList(OneWork.class, " 1=1 ORDER BY id DESC LIMIT 1; ");
        if (oneWorks.size() == 1 && oneWorks.get(0).date_finish == 0d) {
            Settings.getSettings().startTab = "t1";
            Settings.Save();
        }

        mView = inflater.inflate(R.layout.fragment_home, container, false);

//        setupTabHost( mView);
        final TabHost tabHost = (TabHost) mView.findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("t1");
        tabSpec.setIndicator("Еда", getActivity().getResources().getDrawable(android.R.drawable.ic_dialog_map));

        // название вкладки
        // tabSpec.setIndicator("+");
        // указываем id компонента из FrameLayout, он и станет содержимым
        tabSpec.setContent(R.id.tvTab1);
        // добавляем в корневой элемент
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("t2");

        // указываем название и картинку
        // в нашем случае вместо картинки идет xml-файл,
        // который определяет картинку по состоянию вкладки
        tabSpec1.setIndicator("Работа", getResources().getDrawable(R.drawable.tab_icon_selector));
        tabSpec1.setContent(R.id.tvTab2);
        tabHost.addTab(tabSpec1);


        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag(Settings.getSettings().startTab);

        if (Settings.getSettings().startTab.equals("t1")) {
            TabShoumen.showTab1(mView, getActivity());
        }
        if (Settings.getSettings().startTab.equals("t2")) {
            TabShoumen.showTab2(mView, getActivity());
        }

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {


                if (oneWorks.size() == 1 && oneWorks.get(0).date_finish == 0d && tabId.equals("t2")) {
                    Settings.getSettings().setStateSystem(StateSystem.TIMER_WORK, getActivity());
                }


                if (tabId.equals("t1")) {
                    TabShoumen.showTab1(mView, getActivity());
                }
                if (tabId.equals("t2")) {
                    TabShoumen.showTab2(mView, getActivity());
                }
                Toast.makeText(getActivity(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
                Settings.getSettings().startTab = tabId;
                Settings.Save();
            }
        });
        tabHost.setup();

        return mView;
    }

}
