package com.omsk.bitnic.fatpig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.Product;
import Model.Work;
import linq.Linq;
import linq.Predicate;
import orm.Configure;


/**
 * A simple {@link Fragment} subclass.
 */
public class FWork extends Fragment {


    Work mSelectWork;
    ListAdapterWork mAdapterWork;

    private List<Work> mWorkList;
    private View mView;
    private ListView mListView;




    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;
        menu.add("Редактировать").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogAddOdEditWork addWork =new DialogAddOdEditWork();
                addWork.addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {
                        ActivateList(mWorkList);
                        Configure.getSession().update(o);
                    }
                }).addWork(mAdapterWork.getWork(position));
                addWork.show(getActivity().getSupportFragmentManager(), "234");
                return true;
            }
        });

        menu.add("Добавить новое").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogAddOdEditWork addWork =new DialogAddOdEditWork();
                addWork.addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {

                        mWorkList.add( (Work) o);
                        ActivateList(mWorkList);
                    }
                });
                addWork.show(getActivity().getSupportFragmentManager(), "ader34df");
                return true;
            }
        });

        menu.add("Создать кнопку").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DialogCreateButton eat=new DialogCreateButton();
                eat.addWork(mAdapterWork.getWork(position)).addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {
                        Configure.getSession().insert(o);
                        FillData.fill(getActivity());
                    }
                });
                eat.show(getActivity().getSupportFragmentManager(),"srdsd");
                return true;
            }
        });
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_fwork, container, false);
        mListView = (ListView) mView.findViewById(R.id.list_product);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mSelectWork = (Work) view.getTag();
            }
        });
        mListView.setOnCreateContextMenuListener(this);
        mWorkList = Configure.getSession().getList(Work.class, null);
        Collections.sort(mWorkList, new Comparator<Work>() {
            @Override
            public int compare(Work lhs, Work rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });

        ActivateList(mWorkList);
        createListABC();

        ((EditText)mView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) {
                    ActivateList(mWorkList);
                } else {
                    List<Work> list = linq.Linq.toStream(mWorkList).where(new Predicate<Work>() {
                        @Override
                        public boolean apply(Work t) {
                            return t.name.toUpperCase().contains(s.toString().toUpperCase());
                        }
                    }).toList();

                    ActivateList(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });






        return  mView;
    }

    private void ActivateList(List<Work> mWorkList) {

        mAdapterWork =new ListAdapterWork(getActivity(),R.layout.item_list_product,mWorkList);
        mListView.setAdapter(mAdapterWork);
        mSelectWork = null;

    }
    private void createListABC() {
        ListView listView = (ListView) mView.findViewById(R.id.list_abc_plan);
        String[] res= Utils.listABC;
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(getActivity(), R.layout.item_list_stock_alphavit, res);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView sd = (TextView) view;
                final String chars = sd.getText().toString();
                List<Work> tempPlans = Linq.toStream(mWorkList).where(new linq.Predicate<Work>() {
                    @Override
                    public boolean apply(Work t) {
                        return t.name != null && t.name.startsWith(chars);
                    }
                }).toList();
                ActivateList(tempPlans);
            }
        });
    }

}
