package com.kamron.pogoiv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

/**
 * Created by Johan on 2016-09-03.
 * The service which runs in the background and identifies what parts of pogo is visible
 */
public class PoGoIdentifierService extends Service {

    private static final String KEY_TRAINER_LEVEL = "key_trainer_level";
    private static final String KEY_STATUS_BAR_HEIGHT = "key_status_bar_height";
    private static final String KEY_BATTERY_SAVER = "key_battery_saver";
    private static final String KEY_SCREENSHOT_URI = "key_screenshot_uri";

    private int trainerLevel = -1;
    private boolean batterySaver = false;
    private Uri screenshotUri;
    private String screenshotDir;
    private int statusBarHeight = 0;

    private OCRHelper ocr;
    private DisplayMetrics displayMetrics;
    private ScreenGrabber screen;

    private Timer timer;
    private int areaX1;
    private int areaY1;
    private int areaX2;
    private int areaY2;



    @Override
    public void onCreate() {
        super.onCreate();
        displayMetrics = this.getResources().getDisplayMetrics();
        initOCR();

    }

    private void initOCR() {
        String extdir = getExternalFilesDir(null).toString();
        if (!new File(extdir + "/tessdata/eng.traineddata").exists()) {
            copyAssetFolder(getAssets(), "tessdata", extdir + "/tessdata");
        }
        ocr = OCRHelper.init(extdir, displayMetrics.widthPixels, displayMetrics.heightPixels,
                getResources().getString(R.string.pokemon029),
                getResources().getString(R.string.pokemon032));
    }

    @Override
    /**
     * The method which gets called when he service is started
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Refactor", "PoGo identifier service started");
        if (intent != null && intent.hasExtra(KEY_TRAINER_LEVEL)) {
            trainerLevel = intent.getIntExtra(KEY_TRAINER_LEVEL, 1);
            statusBarHeight = intent.getIntExtra(KEY_STATUS_BAR_HEIGHT, 0);
            batterySaver = intent.getBooleanExtra(KEY_BATTERY_SAVER, false);
            if (intent.hasExtra(KEY_SCREENSHOT_URI)) {
                screenshotUri = Uri.parse(intent.getStringExtra(KEY_SCREENSHOT_URI));
            }
            makeNotification();
            /* Assumes MainActivity initialized ScreenGrabber before starting this service. */
            if (!batterySaver) {
                screen = ScreenGrabber.getInstance();
                startPeriodicScreenScan();
            }
        }

        return START_STICKY;
    }

    /**
     * scanPokemonScreen
     * Scans the device screen to check area1 for the white and area2 for the transfer button.
     * If both exist then the user is on the pokemon screen.
     */
    private void scanPokemonScreen() {
        Bitmap bmp = screen.grabScreen();
        if (bmp == null) {
            return;
        }

        if (bmp.getHeight() > bmp.getWidth()) {
            boolean isOnPoGoScreen = bmp.getPixel(areaX1, areaY1) == Color.rgb(250, 250, 250) && bmp.getPixel(areaX2,
                    areaY2)
                    == Color.rgb(28, 135, 150);
            if (isOnPoGoScreen) {

                Log.d("Refactor", "On pokemon screen!");
            }
        }
        bmp.recycle();
    }

    /**
     * Starts the periodic scan by initiating the values of where the scanner should look
     * and setting a timer which calls scanPokemonScreen every tick
     */
    private void startPeriodicScreenScan() {
        areaX1 = Math.round(displayMetrics.widthPixels / 24);  // these values used to get "white" left of "power up"
        areaY1 = (int) Math.round(displayMetrics.heightPixels / 1.24271845);
        areaX2 = (int) Math.round(displayMetrics.widthPixels /
                1.15942029);  // these values used to get greenish color in transfer button
        areaY2 = (int) Math.round(displayMetrics.heightPixels / 1.11062907);
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        scanPokemonScreen();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 750);
    }

    @Override
    public void onDestroy() {
        if (!batterySaver) {
            timer.cancel();
        }
        super.onDestroy();
        stopForeground(true);
        ocr.exit();
        //Now ocr contains an invalid instance hence let's clear it.
        ocr = null;
    }

    /**
     * makeNotification
     * Creates the GoIV notification
     */
    private void makeNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                8959, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(String.format(getString(R.string.notification_title), trainerLevel))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent);
        Notification n = builder.build();

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        startForeground(8959, n);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ///////////////////////////////////
    //TODO: below should be in another class
    ///////////////////////////////////
    ///////////////////////////////////
    private static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        String[] files = new String[0];

        try {
            files = assetManager.list(fromAssetPath);
        } catch (IOException exception) {
            Timber.e("Exception thrown in copyAssetFolder()");
            Timber.e(exception);
        }
        new File(toPath).mkdirs();
        boolean res = true;
        for (String file : files)
            if (file.contains(".")) {
                res &= copyAsset(assetManager, fromAssetPath + "/" + file, toPath + "/" + file);
            } else {
                res &= copyAssetFolder(assetManager, fromAssetPath + "/" + file, toPath + "/" + file);
            }
        return res;

    }

    private static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            InputStream in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            OutputStream out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (IOException exception) {
            Timber.e("Exception thrown in copyAsset()");
            Timber.e(exception);
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
