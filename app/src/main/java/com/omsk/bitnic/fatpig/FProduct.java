package com.omsk.bitnic.fatpig;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Model.Product;
import Model.ProductBase;
import Model.Work;
import linq.Linq;
import linq.Predicate;
import orm.Configure;


public class FProduct extends Fragment {


    private ListAdapterProduct mAdapterProduct;
    private List<ProductBase> mProductList;
    private View mView;
    private ListView  mListView;
    private RelativeLayout mRelativeLayout;
    private LinearLayout mLinearLayout;
    private EditText mEditText;
    private boolean sort;
    private View parentView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;
        menu.add("Редактировать").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                parentView.setVisibility(View.INVISIBLE);
                DialogAddOrEditProduct addProduct = new DialogAddOrEditProduct();
                addProduct.addIAction(new IAction() {
                    @Override
                    public void Action(final Object o) {

                     ProductBase product =   Linq.toStream(mProductList).firstOrDefault(new Predicate<ProductBase>() {
                            @Override
                            public boolean apply(ProductBase t) {
                                return t.id==((Product)o).id;
                            }
                        });

                        product.unclone((Product) o);


                        ActivateList(mProductList);
                        Configure.getSession().update(product);
                    }
                }).addProduct(mAdapterProduct.getProduct(position).cloneE()).addIActionDiasmiss(new IAction() {
                    @Override
                    public void Action(Object o) {
                        parentView.setVisibility(View.VISIBLE);
                    }
                }).show(getActivity().getSupportFragmentManager(), "adsfyusdfsdf");


                return true;
            }
        });

        menu.add("Добавить новое").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                parentView.setVisibility(View.INVISIBLE);
                DialogAddOrEditProduct addProduct = new DialogAddOrEditProduct();
                addProduct.addIAction(new IAction() {
                    @Override
                    public void Action(Object o) {
                        Configure.getSession().insert(o);
                        mProductList.add((Product) o);
                        ActivateList(mProductList);
                    }
                }).addProduct(new Product()).addIActionDiasmiss(new IAction() {
                    @Override
                    public void Action(Object o) {
                        parentView.setVisibility(View.VISIBLE);
                    }
                }).show(getActivity().getSupportFragmentManager(), "adsfyusdfsdf");
                return true;

            }
        });

        if(Settings.getSettings().getSateSystem()==StateSystem.PRODUCT){
            menu.add("Сожрать").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    parentView.setVisibility(View.INVISIBLE);
                    DialogEat eat = new DialogEat();
                    eat.addProduct(mAdapterProduct.getProduct(position)).addIAction(new IAction() {
                        @Override
                        public void Action(Object o) {
                            Configure.getSession().insert(o);
                            FillData.fill(getActivity());
                        }
                    }).addIActionDismiss(new IAction() {
                        @Override
                        public void Action(Object o) {
                            parentView.setVisibility(View.VISIBLE);
                        }
                    }).show(getActivity().getSupportFragmentManager(), "sdsd");

                    return true;
                }
            });
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_product, container, false);
        mRelativeLayout= (RelativeLayout) mView.findViewById(R.id.relative_text);
        mLinearLayout= (LinearLayout) mView.findViewById(R.id.panel_buttons);
        mEditText= (EditText) mView.findViewById(R.id.editText);
        parentView = mView. findViewById(R.id.list11);

        if(Settings.getSettings().getSateSystem()==StateSystem.PRODUCT){
            mProductList =new ArrayList<ProductBase>(Configure.getSession().getList(Product.class, null)) ;
        }else{
            mProductList =new ArrayList<ProductBase>(Configure.getSession().getList(Work.class, null)) ;
        }
        mListView = (ListView) mView.findViewById(R.id.list_product);




        mListView.setOnCreateContextMenuListener(this);

        Collections.sort(mProductList, new Comparator<ProductBase>() {
            @Override
            public int compare(ProductBase lhs, ProductBase rhs) {
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
                List<ProductBase> list = linq.Linq.toStream(mProductList).where(new Predicate<ProductBase>() {
                    @Override
                    public boolean apply(ProductBase t) {
                        return t.preferences;
                    }
                }).toList();
                ActivateList(list);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().equals("")) {
                    ActivateList(mProductList);
                } else {
                    List<ProductBase> list = linq.Linq.toStream(mProductList).where(new Predicate<ProductBase>() {
                        @Override
                        public boolean apply(ProductBase t) {
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
                List<ProductBase> list = Linq.toStream(mProductList).where(new Predicate<ProductBase>() {
                    @Override
                    public boolean apply(ProductBase t) {
                        return t.preferences;
                    }
                }).toList();
                ActivateList(list);
            }
        });


        setListnerToRootView(mView, new IKeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                if (visible) {
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    mLinearLayout.setVisibility(View.GONE);

                } else {
                    mRelativeLayout.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mView.findViewById(R.id.open_keyBord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRelativeLayout.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);
                Handler mHandler = new Handler();
                mHandler.post(
                        new Runnable() {
                            public void run() {
                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInputFromWindow(mEditText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                mEditText.requestFocus();
                            }
                        });
            }
        });

        sort();

        return mView;
    }

    void sort(){

        mView.findViewById(R.id.product_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              List<ProductBase> products=  mAdapterProduct.productList;
                Collections.sort(products, new Comparator<ProductBase>() {
                    @Override
                    public int compare(ProductBase lhs, ProductBase rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });
                ActivateList(products);
                sort=false;
            }
        });

        mView.findViewById(R.id.product_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ProductBase> products=  mAdapterProduct.productList;
                Collections.sort(products, new Comparator<ProductBase>() {
                    @Override
                    public int compare(ProductBase lhs, ProductBase rhs) {
                        return Double.compare(lhs.calorieses, rhs.calorieses);
                    }
                });
                if(sort==false){
                    sort=true;
                }else {
                    sort=false;
                    Collections.reverse(products);
                }

                ActivateList(products);
            }
        });
    }

    public void ActivateList(List<ProductBase> products){
        mAdapterProduct =new ListAdapterProduct(getActivity(),R.layout.item_list_product,products);
        mListView.setAdapter(mAdapterProduct);


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
                List<ProductBase> tempPlans = Linq.toStream(mProductList).where(new linq.Predicate<ProductBase>() {
                    @Override
                    public boolean apply(ProductBase t) {
                        return t.name != null && t.name.startsWith(chars);
                    }
                }).toList();
                ActivateList(tempPlans);
            }
        });
    }



    public void setListnerToRootView(View view, final IKeyboardVisibilityListener listener){


        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {

                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    return;
                }

                alreadyOpen = isShown;
                listener.onVisibilityChanged(isShown);
            }
        });
    }

}

