package com.settings.ion.mylibrary.colorpicker.builder;


import com.settings.ion.mylibrary.colorpicker.ColorPickerView;
import com.settings.ion.mylibrary.colorpicker.renderer.ColorWheelRenderer;
import com.settings.ion.mylibrary.colorpicker.renderer.FlowerColorWheelRenderer;
import com.settings.ion.mylibrary.colorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
	public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
		switch (wheelType) {
			case CIRCLE:
				return new SimpleColorWheelRenderer();
			case FLOWER:
				return new FlowerColorWheelRenderer();
		}
		throw new IllegalArgumentException("wrong WHEEL_TYPE");
	}
}