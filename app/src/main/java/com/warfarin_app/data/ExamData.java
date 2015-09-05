package com.warfarin_app.data;

import android.util.Log;

import com.warfarin_app.SysUtil;

import java.util.Arrays;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/**
 * Created by Coming on 8/9/15.
 */
public class ExamData {

    public long id;
    public double pt = 0.0;
    public double inr = 0.0;
    public long date = 0;
    public double warfarin = 0.0;
    private Locale locale;

    public ExamData()
    {
        date = new Date().getTime();
    }

    public String getDateStr()
    {

        DateFormat shortDateFormat =
                DateFormat.getDateInstance(
                        DateFormat.SHORT, SysUtil.getLocal());


        Date d = new Date();
        d.setTime(date);
        return shortDateFormat.format(d);
    }

    public String getTimeStr()
    {

        DateFormat shortDateFormat =
                DateFormat.getTimeInstance(
                        DateFormat.SHORT, SysUtil.getLocal());

        Date d = new Date();
        d.setTime(date);
        return shortDateFormat.format(d);
    }

    public String getPtStr()
    {

        return String.format("%.2f", pt);

    }

    public String getInrStr()
    {

        return String.format("%.2f", inr);
    }

    public String getWarfarinStr()
    {

        return String.format("%.2f", warfarin);
    }

    public static ExamData parseBytes(byte[] bytes)
    {
        byte[] fbytes;
        float fv1;

        ExamData d;



        fv1 = 1;
        fbytes = ByteBuffer.allocate(4).putFloat(fv1).array();

        d       = new ExamData();
//        d.pt    = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 2, 5)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//        d.inr   = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 6, 10)).order(ByteOrder.LITTLE_ENDIAN).getFloat();

        try {
            fbytes = Arrays.copyOfRange(bytes, 2, 6);

            d.pt = ByteBuffer.wrap(fbytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//            Log.d("app", String.format("%f (%X-%X-%X-%X)", d.pt, fbytes[0], fbytes[1], fbytes[2], fbytes[3]));

            fbytes = Arrays.copyOfRange(bytes, 6, 10);
            d.inr = ByteBuffer.wrap(fbytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//            Log.d("app", String.format("%f (%X-%X-%X-%X)", d.inr, fbytes[0], fbytes[1], fbytes[2], fbytes[3]));
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.e("app", e.getStackTrace().toString());
        }

        return d;
    }

    @Override
    public String toString()
    {
        return String.format("ExamData: pt: %f, inr: %f", pt, inr);
    }
}
