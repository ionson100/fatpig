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
import linq.Linq;
import linq.Predicate;
import orm.Configure;


public class FProduct extends Fragment {

    Product mSelectProduct;
    ListAdapterProduct mAdapterProduct;

    private List<Product> mProductList;
    private View mView;
    private ListView  mListView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;
        menu.add("Редактировать").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogAddOdEditProduct addProduct=new DialogAddOdEditProduct();
                addProduct.addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {
                        ActivateList(mProductList);
                        Configure.getSession().update(o);
                    }
                }).addProduct(mAdapterProduct.getProduct(position));
                addProduct.show(getActivity().getSupportFragmentManager(), "adsfyusdfsdf");
                return true;
            }
        });

        menu.add("Добавить новое").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogAddOdEditProduct addProduct=new DialogAddOdEditProduct();
                addProduct.addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {

                        mProductList.add(0, (Product) o);
                        ActivateList(mProductList);
                    }
                });
                addProduct.show(getActivity().getSupportFragmentManager(), "ad34df");
                return true;
            }
        });

        menu.add("Сожрать").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DialogEat eat=new DialogEat();
                eat.addProduct(mAdapterProduct.getProduct(position)).addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {
                     Configure.getSession().insert(o);
                        FillData.fill(getActivity());
                    }
                });
                eat.show(getActivity().getSupportFragmentManager(),"sdsd");
                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        mProductList = Configure.getSession().getList(Product.class, null);
        mListView = (ListView) mView.findViewById(R.id.list_product);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mSelectProduct = (Product) view.getTag();
            }
        });

        mListView.setOnCreateContextMenuListener(this);

        Collections.sort(mProductList, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });


        ActivateList(mProductList);
        createListABC();

        mView.findViewById(R.id.bt_show_all_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateList(mProductList);
            }
        });

        mView.findViewById(R.id.bt_select_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> list=linq.Linq.toStream(mProductList).where(new Predicate<Product>() {
                    @Override
                    public boolean apply(Product t) {
                       return t.preferences;
                    }
                }).toList();
                ActivateList(list);
            }
        });

        ((EditText)mView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) {
                    ActivateList(mProductList);
                } else {
                    List<Product> list = linq.Linq.toStream(mProductList).where(new Predicate<Product>() {
                        @Override
                        public boolean apply(Product t) {
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

        mView.findViewById(R.id.bt_select_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> list = Linq.toStream(mProductList).where(new Predicate<Product>() {
                    @Override
                    public boolean apply(Product t) {
                        return t.preferences;
                    }
                }).toList();
                ActivateList(list);
            }
        });

        return mView;
    }

    public void ActivateList(List<Product> products){
        mAdapterProduct =new ListAdapterProduct(getActivity(),R.layout.item_list_product,products);
        mListView.setAdapter(mAdapterProduct);
        mSelectProduct = null;

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
                List<Product> tempPlans = Linq.toStream(mProductList).where(new linq.Predicate<Product>() {
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
