package com.warfarin_app;

import android.content.Context;
import java.util.Locale;

/**
 * Created by Coming on 8/12/15.
 */
public class SysUtil {
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
