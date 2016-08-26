package com.omsk.bitnic.fatpig;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.omsk.bitnic.fatpig.R;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FHome extends Fragment {


    private View mView;
    private TabHost mTabHost;

//    private void setupTabHost(View mView) {
//        mTabHost = (TabHost) mView.findViewById(android.R.id.tabhost);
//        mTabHost.setup();
//    }
//    private void setupTab(final View view, final String tag) {
//        View tabview = createTabView(mTabHost.getContext(), tag);
//
//        TabHost.TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabHost.TabContentFactory() {
//            public View createTabContent(String tag) {return view;}
//        });
//        mTabHost.addTab(setContent);
//
//    }
//
//    private static View createTabView(final Context context, final String text) {
//        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
//        TextView tv = (TextView) view.findViewById(R.id.tabsText);
//        tv.setText(text);
//        return view;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView=inflater.inflate(R.layout.fragment_home, container, false);

//        setupTabHost( mView);
        TabHost tabHost = (TabHost) mView.findViewById(android.R.id.tabhost);
        // инициализация
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("t1");
        tabSpec.setIndicator("TAB2", getActivity().getResources().getDrawable(android.R.drawable.ic_dialog_map));

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
        tabSpec1.setIndicator("-", getResources().getDrawable(R.drawable.tab_icon_selector));
        tabSpec1.setContent(R.id.tvTab2);
        tabHost.addTab(tabSpec1);

       // tabSpec = tabHost.newTabSpec("tag3");
        // создаем View из layout-файла

//        LayoutInflater vi;
//        vi = LayoutInflater.from(getActivity());
//        View v = vi.inflate(R.layout.tab_header, null);
//
//
//
//        // и устанавливаем его, как заголовок
//        tabSpec.setIndicator(v);
//        tabSpec.setContent(R.id.tvTab3);
//        tabHost.addTab(tabSpec);

        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag(Settings.getSettings().startTab);

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(getActivity(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
                Settings.getSettings().startTab=tabId;
                Settings.Save();
            }
        });
        tabHost.setup();

        return mView;
    }

}
