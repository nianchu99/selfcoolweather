package space.nianchu.selfcoolweather;

import android.Manifest;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LitePal.initialize(mContext);
    }

    public static Context getMContext(){
        return mContext;
    }
}
