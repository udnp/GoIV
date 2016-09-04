package com.kamron.pogoiv.Overlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.kamron.pogoiv.R;

/**
 * Created by (remade & refactored by) Johan on 2016-09-04.
 * Original class created by Kamron
 * The class representing the button which when clicked starts the overlaywindow
 */
public class FloatingIVButton extends Activity {

    private final WindowManager.LayoutParams ivButtonParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    private WindowManager windowManager;
    private ImageView ivButton;
    private DisplayMetrics displayMetrics;


    /**
     * Shows the iv button on the screen
     */
    public static void show(Context startedFrom) {
        Intent intent = new Intent(startedFrom, FloatingIVButton.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //required so that the service can start an activity
        startedFrom.startActivity(intent);
        Log.d("refactor", "Show floatingivbutton called");
    }


    /**
     * Hides the iv button from the screen
     */
    public static void hide() {

        Log.d("refactor", "hide floatingivbutton called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        super.onCreate(savedInstanceState);
        displayMetrics = this.getResources().getDisplayMetrics();
        createIVButton();
    }

    /**
     * setIVButtonDisplay
     * Receiver called from MainActivity. Tells Pokefly to either show the IV Button (if on poke) or
     * hide the IV Button.
     */
    private void setIVButtonDisplay(boolean show) {
        if (show) {
            windowManager.addView(ivButton, ivButtonParams);
        } else if (!show) {
            windowManager.removeView(ivButton);

        }
    }

    /**
     * createIVButton
     * Creates the IV Button view
     */
    private void createIVButton() {
        ivButton = new ImageView(this);
        ivButton.setImageResource(R.drawable.button);

        ivButtonParams.gravity = Gravity.BOTTOM | Gravity.START;
        ivButtonParams.x = dpToPx(20);
        ivButtonParams.y = dpToPx(15);

        ivButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Log.d("refactor", "Iv button pressed");
                        break;
                }
                return false;
            }
        });
    }

    /**
     * Convert dp measurment to this units pixels by using the density of the used screen.
     *
     * @param dp how many dp you want to convert to pixels
     * @return the amount of pixels (dp) is on this device
     */
    private int dpToPx(int dp) {
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


}
