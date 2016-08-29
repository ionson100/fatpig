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




    private RelativeLayout mRelativeLayout;
    private LinearLayout mLinearLayout;
    private EditText mEditText;
    private boolean sort;


    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final int position = aMenuInfo.position;
        menu.add(R.string.editor).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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

        menu.add(R.string.addnew).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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

        menu.add(R.string.createButton).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
        mView=inflater.inflate(R.layout.fragment_product, container, false);

        mRelativeLayout= (RelativeLayout) mView.findViewById(R.id.relative_text);
        mLinearLayout= (LinearLayout) mView.findViewById(R.id.panel_buttons);
        mEditText= (EditText) mView.findViewById(R.id.editText);

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


    public void setListnerToRootView(View view, final IKeyboardVisibilityListener listener){

        final View parentView = view. findViewById(R.id.list11);
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


    void sort(){

        mView.findViewById(R.id.product_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Work> works=  mAdapterWork.workList;
                Collections.sort(works, new Comparator<Work>() {
                    @Override
                    public int compare(Work lhs, Work rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });
                ActivateList(works);
                sort=false;
            }
        });

        mView.findViewById(R.id.product_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Work> works=  mAdapterWork.workList;
                Collections.sort(works, new Comparator<Work>() {
                    @Override
                    public int compare(Work lhs, Work rhs) {
                        return Double.compare(lhs.calorieses, rhs.calorieses);
                    }
                });
                if (sort == false) {
                    sort = true;
                } else {
                    sort = false;
                    Collections.reverse(works);
                }

                ActivateList(works);
            }
        });
    }


}
