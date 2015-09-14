package com.warfarin_app.util;

import android.content.Context;

import java.util.Locale;

/**
 * Created by Coming on 9/10/15.
 */
public class SystemInfo {
    public static boolean isBluetooth = true;

    private static Context context;


    public static void setContext(Context ctx)
    {
        context = ctx;

    }

    public static Locale getLocal()
    {
        return context.getResources().getConfiguration().locale;
    }
}
