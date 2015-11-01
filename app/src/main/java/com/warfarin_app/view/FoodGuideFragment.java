package com.warfarin_app.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.warfarin_app.R;

/**
 * Created by Coming on 8/5/15.
 */
public class FoodGuideFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_guide, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        WebView wv = (WebView)this.getView().findViewById(R.id.food_guide_webview);
//        wv.loadUrl("http://tw.yahoo.com");
        wv.loadUrl("file:///android_asset/food_guide.htm");
    }
}