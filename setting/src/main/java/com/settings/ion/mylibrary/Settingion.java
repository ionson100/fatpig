package com.settings.ion.mylibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Settingion extends LinearLayout {

    //public static  OnSettingChangeListener onSettingChangeListener;
    private static final Map<Class, List<InnerAttribute>> map = new HashMap<>();

    private TableLayout table;

    private Class aClass;
    // LinearLayout base;
    // private Switch switch2;
    // private CheckBox check2;

    private Context context;

    public Settingion(Context context) {
        super(context);
        this.context = context;
        ini();

    }


    public Settingion(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        ini();

        // tt.getClass();
    }

    public Settingion(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        ini();
    }

    private void ini() {
        View v = inflate(context, R.layout.settinsion, null);
        table = (TableLayout) v.findViewById(R.id.table);


        this.addView(v);
    }


    public void setModelClass(Class modelClass) {
        this.aClass = modelClass;
        innerBuilder(aClass);
    }

    public void setModelClass(Class modelClass, Context context) {
        this.context = context;
        this.aClass = modelClass;
        innerBuilder(aClass);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void innerBuilder(final Class aClass) {

//        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;


        List<InnerAttribute> list = map.get(aClass);
        if (list == null) {
            list = Gudaperez();
            map.put(aClass, list);
        }

        final Object ob = Reanimator.get(aClass);

        for (final InnerAttribute ia : list) {
            final SettingField setting = ia.settingField;

            TableRow row;
            if (setting.typeField() == TypeField.BooleanCheck) {
                row = (TableRow) inflate(context, R.layout.boolean_check_row, null);
                final CheckBox check2 = (CheckBox) row.findViewById(R.id.check2);

                try {
                    Field field = ob.getClass().getDeclaredField(ia.fieldName);
                    field.setAccessible(true);
                    Object val = field.get(ob);
                    check2.setChecked((boolean) val);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException("settings core:" + e.getMessage());
                }
                row.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Burbulus((TableRow) v);
                        boolean val = !check2.isChecked();
                        try {
                            Field field = ob.getClass().getDeclaredField(ia.fieldName);
                            field.setAccessible(true);
                            field.set(ob, val);
                            if (Reanimator.mIListener != null) {
                                Reanimator.notify(ob, field.getName(), null, val);
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            throw new RuntimeException("settings core:" + e.getMessage());
                        }
                        Reanimator.save(aClass);
                        check2.setChecked(val);

                    }
                });

            } else if (setting.typeField() == TypeField.BooleanSwitch) {
                row = (TableRow) inflate(context, R.layout.boolean_switn_row, null);
                final SwitchCompat switch21 = (SwitchCompat) row.findViewById(R.id.switch2);

                try {
                    Field field = ob.getClass().getDeclaredField(ia.fieldName);
                    field.setAccessible(true);
                    Object val = field.get(ob);
                    switch21.setChecked((boolean) val);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException("settings core:" + e.getMessage());
                }
                row.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Burbulus((TableRow) v);
                        boolean val = !switch21.isChecked();
                        try {
                            Field field = ob.getClass().getDeclaredField(ia.fieldName);
                            field.setAccessible(true);
                            field.set(ob, val);
                            if (Reanimator.mIListener != null) {
                                Reanimator.notify(ob, field.getName(), null, val);
                            } else {
                                Reanimator.save(aClass);
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            throw new RuntimeException("Reanimator:" + e.getMessage());

                        }

                        switch21.setChecked(val);
                    }
                });

            } else {
                row = (TableRow) inflate(context, R.layout.row, null);
            }


            row.setTag(ia);
            if (setting.image() != 0) {
                ImageView image = new ImageView(context);
                image.setPadding(20, 0, 0, 0);
                image.setImageResource(setting.image());
                LinearLayout image_container = (LinearLayout) row.findViewById(R.id.image_container);
                image_container.addView(image, 0);
            }
            TextView textView = (TextView) row.findViewById(R.id.text1);

            textView.setText(setting.title());
            if (setting.styleTitle() != 0) {
                textView.setTextAppearance(context, setting.styleTitle());
            }
            if (setting.descriptions() != 0) {
                TextView textView1 = (TextView) row.findViewById(R.id.text2);
                textView1.setText(setting.descriptions());
                if (setting.styleDescription() != 0) {
                    textView1.setTextAppearance(context, setting.styleDescription());
                }
            }
            if (setting.padding().length == 4) {
                row.setPadding(setting.padding()[0], setting.padding()[1], setting.padding()[2], setting.padding()[3]);
            }
            table.addView(row);

            final String sd = ia.fieldName;
            if (setting.typeField() == TypeField.SubMenu) {
                row.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Burbulus((TableRow) v);
                        Intent(ia, ob);

                    }
                });

            } else if (setting.typeField() == TypeField.BooleanCheck) {


            } else if (setting.typeField() == TypeField.BooleanSwitch) {


            } else {
                row.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Burbulus((TableRow) v);
                        Clicker(setting, sd, aClass, ob);

                    }
                });
            }

        }
    }

    private void Burbulus(TableRow row) {
        for (int i = 0; i < table.getChildCount(); i++) {
            View d = table.getChildAt(i);
            d.setBackgroundResource(R.color.color_row_def);
        }
        row.setBackgroundResource(R.color.color_row_select);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Intent(InnerAttribute attribute, Object o) {
        Caption caption = (Caption) attribute.aClass.getAnnotation(Caption.class);
        if (caption == null || caption.SHOW_TYPES() == ShowTypes.Activity) {
           // Intent intent = new Intent(context, SubMenuActivity.class);
           // SubMenuActivity.innerAttribute = attribute;
           // SubMenuActivity.hostObject = o;
            //context.startActivity(intent);
        } else {
            DialogSubmenu f = new DialogSubmenu();
            f.showDialog(o, attribute, ((Activity) context).getFragmentManager());
        }

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void Clicker(SettingField settingField, final String fieldName, final Class aClass, final Object ob) {

        if (settingField.typeField() == TypeField.Color) {

            Object o = Reanimator.get(aClass);
            int val = 0;
            try {
                Field field = o.getClass().getDeclaredField(fieldName);

                field.setAccessible(true);
                val = field.getInt(o);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Reanimator:" + e.getMessage());
            }

            InnerAttribute f = new InnerAttribute();
            f.settingField = settingField;
            f.fieldName = fieldName;


            ColorPickerDialogE dialogE=new ColorPickerDialogE(context, new ColorPickerDialogE.IAction() {
                @Override
                public void Action(int color) {
                    Object o = Reanimator.get(aClass);
                    try {
                        Field field = o.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(o, color);
                        if (Reanimator.mIListener != null) {
                            Reanimator.mIListener.OnCallListen(aClass, fieldName, null, color);
                        }
                        Reanimator.save(aClass);

                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException("Reanimator:" + e.getMessage());
                    }
                }
            });
            dialogE.show(settingField.defaultColor(),context.getString(settingField.title()));


//            ColorPickerDialog ef = new ColorPickerDialog(context, new ColorPickerDialog.OnColorChangedListener() {
//                @Override
//                public void colorChanged(String key, int color) {
//
//                    Object o = Reanimator.get(aClass);
//                    try {
//                        Field field = o.getClass().getDeclaredField(fieldName);
//                        field.setAccessible(true);
//                        field.set(o, color);
//                        if (Reanimator.mIListener != null) {
//                            Reanimator.mIListener.OnCallListen(aClass, fieldName, null, color);
//                        }
//                        Reanimator.save(aClass);
//
//                    } catch (IllegalAccessException | NoSuchFieldException e) {
//                        throw new RuntimeException("Reanimator:" + e.getMessage());
//                    }
//
//                }
//            }, val, f);
//            ef.show();

        } else if (settingField.typeField() == TypeField.list) {

            ListDialog dialog = new ListDialog();
            InnerAttribute f = new InnerAttribute();
            f.settingField = settingField;
            f.fieldName = fieldName;
            String title = context.getString(settingField.title());
            String subTitle = "";
            if (settingField.descriptions() != 0) {
                subTitle = context.getString(settingField.descriptions());
            }
            dialog.showDialog(aClass, f, ((Activity) context).getFragmentManager(), title, subTitle, context);


        } else {
            SimpleDialog dialog = new SimpleDialog();
            InnerAttribute f = new InnerAttribute();
            f.settingField = settingField;
            f.fieldName = fieldName;
            String title = context.getString(settingField.title());
            String subTitle = "";
            if (settingField.descriptions() != 0) {
                subTitle = context.getString(settingField.descriptions());
            }
            dialog.showDialog(aClass, f, ((Activity) context).getFragmentManager(), title, subTitle);
        }

    }

    private List<InnerAttribute> Gudaperez() {
        List<InnerAttribute> list = new ArrayList<>();

        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            SettingField a = field.getAnnotation(SettingField.class);
            if (a == null) continue;

            InnerAttribute ia = new InnerAttribute();
            ia.aClass = field.getType();
            ia.fieldName = field.getName();
            ia.settingField = a;
            list.add(ia);
        }
        Collections.sort(list, new Comparator<InnerAttribute>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(InnerAttribute lhs, InnerAttribute rhs) {
                return Integer.compare(lhs.settingField.index(), rhs.settingField.index());
            }
        });
        return list;
    }

    public View getTableRow(String fiedName) {
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow r = (TableRow) table.getChildAt(i);
            InnerAttribute a = (InnerAttribute) r.getTag();
            if (a.fieldName.equals(fiedName)) {
                return r;
            }
        }
        return null;
    }

    public View getTitleTextView(String fiedName) {
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow r = (TableRow) table.getChildAt(i);
            InnerAttribute a = (InnerAttribute) r.getTag();
            if (a.fieldName.equals(fiedName)) {
                return r.findViewById(R.id.text1);
            }
        }
        return null;
    }

    public void itemInvisible(String fiedName) {
        View v = null;
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow r = (TableRow) table.getChildAt(i);
            InnerAttribute a = (InnerAttribute) r.getTag();
            if (a.fieldName.equals(fiedName)) {
                v = r;
            }
        }
        if (v != null) {
            table.removeView(v);
        }

    }

    public TextView getDescriptionTextView(String fiedName) {
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow r = (TableRow) table.getChildAt(i);
            InnerAttribute a = (InnerAttribute) r.getTag();
            if (a.fieldName.equals(fiedName)) {
                return (TextView) r.findViewById(R.id.text2);
            }
        }
        return null;
    }

    public ImageView getImageView(String fiedName) {
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow r = (TableRow) table.getChildAt(i);
            InnerAttribute a = (InnerAttribute) r.getTag();
            if (a.fieldName.equals(fiedName)) {
                return (ImageView) r.findViewById(R.id.image);
            }
        }
        return null;
    }

    public void setEnabledE(boolean b) {
        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow r = (TableRow) table.getChildAt(i);
            r.setEnabled(b);
        }
    }

    public interface OnSettingChangeListener {
        void SettingChanged(Object objects, String fieldName);
    }


}
