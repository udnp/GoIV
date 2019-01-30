package com.kamron.pogoiv;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.kamron.pogoiv.utils.CopyUtils;
import com.kamron.pogoiv.utils.FontsOverride;

import java.io.File;
import java.util.Locale;

import timber.log.Timber;

public class PoGoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            CrashlyticsWrapper.init(getApplicationContext());

            Timber.plant(new CrashlyticsWrapper.CrashReportingTree(this));
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
        String lang = Locale.getDefault().getLanguage();
        CopyUtils.copyAssetFolder(getAssets(), "tessdata", extDir + "/tessdata");
        if (lang.contains("ja")) {
//        if (!new File(extDir + "/tessdata/jpn.traineddata").exists())
            CopyUtils.copyAssetFile(getAssets(), "tessdata", "jpn.traineddata", extDir + "/tessdata");
        } else {
//        if (!new File(extDir + "/tessdata/eng.traineddata").exists())
            CopyUtils.copyAssetFile(getAssets(), "tessdata", "eng.traineddata", extDir + "/tessdata");
        }

    }
}
