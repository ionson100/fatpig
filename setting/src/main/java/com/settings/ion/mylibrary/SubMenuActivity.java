package com.settings.ion.mylibrary;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Field;

//class SubMenuActivity extends Activity {
//
////    public static InnerAttribute innerAttribute;
////    public static Object hostObject;
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////      //  super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_sub_menu);
////
////
////        Settingion settingion= (Settingion) findViewById(R.id.subsetting);
////        settingion.setModelClass(innerAttribute.aClass);
////        setTitle(innerAttribute.settingField.title());
////    }
////
////
////    @Override
////    protected void onDestroy() {
////
////        try {
////            Field field=hostObject.getClass().getDeclaredField(innerAttribute.fieldName);
////            field.setAccessible(true);
////            field.set(hostObject,Reanimator.get(innerAttribute.aClass));
////        } catch (Exception e) {
////           throw  new RuntimeException(e.getMessage());
////        }
////       // super.onDestroy();
////    }
//}
