package com.example.sinmingu.login;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sinmingu on 2017-07-31.
 */

public class BaseActivity extends FragmentActivity{

    public Typeface mtypeface = null;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(mtypeface == null) {

            mtypeface = Typeface.createFromAsset(this.getAssets(), "fonts/HoonWhitecatR.ttf");

        }
        setGlobalFont(getWindow().getDecorView());
    }

    private void setGlobalFont(View view) {
        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int vgCnt = viewGroup.getChildCount();
                for(int i = 0; i<vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(mtypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }

}
