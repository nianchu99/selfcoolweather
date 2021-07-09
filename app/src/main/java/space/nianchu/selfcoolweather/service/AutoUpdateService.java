package space.nianchu.selfcoolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import space.nianchu.selfcoolweather.AQI;
import space.nianchu.selfcoolweather.CurrentWeather;
import space.nianchu.selfcoolweather.Suggestion;
import space.nianchu.selfcoolweather.Weather;
import space.nianchu.selfcoolweather.WeatherActivity;
import space.nianchu.selfcoolweather.util.HttpUtil;
import space.nianchu.selfcoolweather.util.Utility;

public class AutoUpdateService extends Service {
    private static final String TAG = "AutoUpdateService";
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
                updateBingPic();
            }
        });
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        这是每小时的毫秒数
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /*
    更新天气信息
     */
    private void updateWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        从缓存中获取weatherId(其实是CityId)，用于进行向服务器获取响应
        final String weatherId = preferences.getString("weather_id", null);
        if (weatherId != null){
            String forecastWeatherUrl = "https://devapi.qweather.com/v7/weather/7d?location=" + weatherId + "&key=" + Utility.KEY;
            String currentWeatherUrl = "https://devapi.qweather.com/v7/weather/now?location=" + weatherId + "&key=" + Utility.KEY;
            String aqiUrl = "https://devapi.qweather.com/v7/air/now?location=" +  weatherId + "&key=" + Utility.KEY;
            String suggestionUrl = "https://devapi.qweather.com/v7/indices/1d?location=" + weatherId + "&key=" + Utility.KEY + "&type=1,2,8";
//            请求7天天气预报
            HttpUtil.sendOkHttpRequest(forecastWeatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handlerWeatherResponse(responseText);

                    if ("200".equals(weather.getCode())){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseText);
                        editor.putString("weather_id",weatherId);
                        editor.apply();
                    }
                }

            });
//            请求当前天气
            HttpUtil.sendOkHttpRequest(currentWeatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final CurrentWeather currentWeather = Utility.handlerCurrentWeather(responseText);
                            if (currentWeather != null && "200".equals(currentWeather.getCode())){
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                                editor.putString("current_weather", responseText);
                                editor.apply();
                            }

                }
            });
//            请求当日AQI空气质量
            HttpUtil.sendOkHttpRequest(aqiUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final AQI aqi = Utility.handlerAQI(responseText);
                            if (aqi != null && "200".equals(aqi.getCode())){
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                                editor.putString("aqi", responseText);
                                editor.apply();
                            }
                }
            });
//            请求当日生活建议
            HttpUtil.sendOkHttpRequest(suggestionUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Suggestion suggestion = Utility.handlerSuggestion(responseText);
                    if (suggestion != null && "200".equals(suggestion.getCode())){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("suggestion", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
    /*
    更新必应每日一图
     */
    private void updateBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }
}
