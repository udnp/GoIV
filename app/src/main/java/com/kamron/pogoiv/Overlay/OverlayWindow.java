package com.kamron.pogoiv.Overlay;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;

import com.kamron.pogoiv.R;

/**
 * Created by Johan on 2016-09-03.
 *
 * The activity which floats on top of other screens, and holds the fragments for the input,
 * results and all iv combinations
 */
public class OverlayWindow extends Activity {


    private final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
            PixelFormat.TRANSPARENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_window);
    }




}
