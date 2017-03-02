package com.settings.ion.mylibrary;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(value = RetentionPolicy.RUNTIME)
public @interface Caption {
    int CAPTION();
    ShowTypes SHOW_TYPES() default ShowTypes.Activity;
}
