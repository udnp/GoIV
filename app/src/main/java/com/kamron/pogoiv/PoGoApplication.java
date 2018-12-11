package com.kamron.pogoiv;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.kamron.pogoiv.pokeflycomponents.MovesetsManager;
import com.kamron.pogoiv.utils.CopyUtils;
import com.kamron.pogoiv.utils.CrashlyticsWrapper;
import com.kamron.pogoiv.utils.FontsOverride;

import java.io.File;

import timber.log.Timber;

public class PoGoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            CrashlyticsWrapper.getInstance().init(this);
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Fonts overriding application wide
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Lato-Medium.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Lato-Medium.ttf");

        // copy the trained data file for OCR
        // Note:Currently this data is being improved, so that is needed to be overwritten every time GoIV launched.
        //      it should considers other better place to call this logic, for UI performance and UX.
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir == null) {
            externalFilesDir = getFilesDir();
        }
        String extDir = externalFilesDir.toString();
//        if (!new File(extDir + "/tessdata/eng.traineddata").exists()) {
        CopyUtils.copyAssetFolder(getAssets(), "tessdata", extDir + "/tessdata");
//        }

        MovesetsManager.init(this);
    }
}
