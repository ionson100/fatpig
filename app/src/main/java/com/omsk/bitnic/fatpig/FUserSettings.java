package com.omsk.bitnic.fatpig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import Model.Sex;
import Model.User;
import orm.Configure;


/**
 * A simple {@link Fragment} subclass.
 */
public class FUserSettings extends Fragment {


    User user;
    View mView;

    public FUserSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_settings, container, false);
        List<User> list = Configure.getSession().getList(User.class, null);
        if (list.size() == 0) {
            user = new User();
            user.setSex(Sex.men);
            Configure.getSession().insert(user);
        } else {
            user = list.get(0);
        }
        EditText textUser = ((EditText) mView.findViewById(R.id.user_name));
        textUser.setText(user.name);

        EditText textAge = ((EditText) mView.findViewById(R.id.user_age));
        textAge.setText(String.valueOf(user.age == 0 ? "" : user.age));

        EditText textWeight = ((EditText) mView.findViewById(R.id.user_weight));
        textWeight.setText(String.valueOf(user.weight == 0 ? "" : user.weight));

        EditText textRost = ((EditText) mView.findViewById(R.id.user_рост));
        textRost.setText(String.valueOf(user.growing == 0 ? "" : user.growing));

        final EditText user_default_calorises = (EditText) mView.findViewById(R.id.user_default_calorises);
        user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));


        RadioButton sexMen = (RadioButton) mView.findViewById(R.id.sex_man);
        RadioButton sexWoman = (RadioButton) mView.findViewById(R.id.sex_woman);
        if (user.getSex() == Sex.men) {
            sexMen.setChecked(true);
        } else {
            sexWoman.setChecked(true);
        }
        sexMen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user.setSex(Sex.men);
                } else {
                    user.setSex(Sex.women);
                }
                if (isChecked) {
                    user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));
                }
                FillData.fill(getActivity());
            }
        });
        sexWoman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user.setSex(Sex.women);
                } else {
                    user.setSex(Sex.men);
                }
                if (isChecked) {
                    user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));
                }
                FillData.fill(getActivity());
            }
        });


        textUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    user.name = "";
                } else {
                    user.name = s.toString();
                }
                FillData.fill(getActivity());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        textAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    user.age = 0;
                } else {
                    user.age = Integer.parseInt(s.toString());
                }

                user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));
                FillData.fill(getActivity());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    user.weight = 0;
                } else {
                    user.weight = Integer.parseInt(s.toString());
                }
                user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));
                FillData.fill(getActivity());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textRost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    user.growing = 0;
                } else {
                    user.growing = Integer.parseInt(s.toString());
                }

                user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));
                FillData.fill(getActivity());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mView.findViewById(R.id.bt_user_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Configure.getSession().update(user);
                    FillData.fill(getActivity());
                }
            }
        });

        RadioGroup radio_delta = (RadioGroup) mView.findViewById(R.id.radio_delta);
        for (int i = 0; i < radio_delta.getChildCount(); i++) {
            final RadioButton button = (RadioButton) radio_delta.getChildAt(i);
            if (button.getTag().equals(String.valueOf(user.delta))) {
                button.setChecked(true);
            }
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        user.delta = Double.parseDouble(button.getTag().toString());
                        user_default_calorises.setText(String.valueOf(Utils.getCalorises(user, getActivity())));
                        FillData.fill(getActivity());
                    }
                }
            });
        }


        return mView;
    }

    public boolean validate() {
        if (user.name == null || user.name.trim().length() == 0) {
            ((EditText) mView.findViewById(R.id.user_name)).setError(getString(R.string.not_name));
            return false;
        }
        if (user.age == 0 || user.age < 10 || user.age > 100) {
            ((EditText) mView.findViewById(R.id.user_age)).setError(getString(R.string.not_age));
            return false;
        }
        if (user.weight == 0) {
            ((EditText) mView.findViewById(R.id.user_weight)).setError(getString(R.string.not_weith));
            return false;
        }
        return true;
    }

}
