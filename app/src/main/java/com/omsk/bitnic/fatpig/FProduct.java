package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import Model.Product;
import Model.Work;
import linq.Linq;
import linq.Predicate;
import orm.Configure;


public class FProduct extends Fragment {


    private List<Product> productList;
    private View mView;
    private ListView  mListView;
    public FProduct() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_product, container, false);
        mListView= (ListView) mView.findViewById(R.id.list_product);
       productList= Configure.getSession().getList(Product.class,null);
      //  List<Work> list2= Configure.getSession().getList(Work.class,null);
       ActivateList(productList);
        createListABC();

        mView.findViewById(R.id.bt_show_all_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateList(productList);
            }
        });

        mView.findViewById(R.id.bt_select_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> list=linq.Linq.toStream(productList).where(new Predicate<Product>() {
                    @Override
                    public boolean apply(Product t) {
                       return t.preferences==true;
                    }
                }).toList();
                ActivateList(list);
            }
        });


        return mView;
    }

    public void ActivateList(List<Product> products){
        ListAdapterProduct p=new ListAdapterProduct(getActivity(),R.layout.item_list_product,products);
        mListView.setAdapter(p);
    }

    private void createListABC() {
        ListView listView = (ListView) mView.findViewById(R.id.list_abc_plan);
        String[] res= Utils.listABC;
        android.widget.ArrayAdapter adapter = new android.widget.ArrayAdapter(getActivity(), R.layout.item_list_stock_alphavit,res);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView sd = (TextView) view;
                final String chars = sd.getText().toString();

                List<Product> tempPlans = Linq.toStream(productList).where(new linq.Predicate<Product>() {
                    @Override
                    public boolean apply(Product t) {
                        return t.name != null && t.name.startsWith(chars);
                    }
                }).toList();
                ActivateList(tempPlans);
            }
        });
    }


}
