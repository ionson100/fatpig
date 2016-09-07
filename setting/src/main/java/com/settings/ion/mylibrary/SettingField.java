package com.settings.ion.mylibrary;

import android.graphics.Color;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SettingField {
    int title();

    int index();

    int defaultColor() default Color.BLACK;

    int image() default 0;

    int styleTitle() default 0;

    int styleDescription() default 0;

    TypeField typeField() default TypeField.String;

    int descriptions() default 0;

    Class IListItem() default String.class;

    int[] padding() default {0};
}
