package space.nianchu.selfcoolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import space.nianchu.selfcoolweather.util.HttpUtil;
import space.nianchu.selfcoolweather.util.Utility;
//TODO: 重新打开应用时没有weatherId
public class WeatherActivity extends AppCompatActivity {
    private Context mContext;
    private static final String TAG = "WeatherActivity";
    public DrawerLayout drawerLayout;
    private Button navButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout nowLayout;
    private CardView forecastLayout1;
    private CardView aqiLayout;
    private CardView suggestionLayout;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        mContext = WeatherActivity.this;
//        初始化各控件
        bindViews();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //        Deal with the city name
          String cityName = preferences.getString("city_name", null);
          if (cityName == null){
          cityName = getIntent().getStringExtra("city_name");
              SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
              editor.putString("city_name", cityName);
              editor.apply();
          }
        //        Deal with the weather id
        String weatherId = preferences.getString("weather_id", null);
        if (weatherId == null){
            weatherId = getIntent().getStringExtra("weather_id");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString("weather_id", weatherId);
            editor.apply();
        }
        String weatherString = preferences.getString("weather", null);
        String currentWeatherString = preferences.getString("current_weather", null);
        String aqiString = preferences.getString("aqi", null);
        String suggestionString = preferences.getString("suggestion", null);

//        bing pic
        String bingPic = preferences.getString("bing_pic", null);
        if (bingPic != null){
            Glide.with(mContext).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
//        city name
        Log.d(TAG, "onCreate: WeatherActivity: " + cityName);
//        if (cityName ==  null){
//         cityName = preferences.getString("city_name", null);
//            Log.d(TAG, "onCreate: ");
//        }
        Log.d(TAG, "onCreate: WeatherActivity: " + cityName);
//        weather
        if (weatherString != null){
            Weather weather = Utility.handlerWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }
        else {
            Log.d(TAG, "onCreate: Get the weather id: " + weatherId);
            forecastLayout1.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
//        current  weather
        if (currentWeatherString != null){
            CurrentWeather currentWeather = Utility.handlerCurrentWeather(currentWeatherString);
            Log.d(TAG, "currentWeatherString != null and the name is " + cityName);
            showCurrentWeather(currentWeather, cityName);
        }else {
            Log.d(TAG, "onCreate: Get the weather id: " + weatherId);
            nowLayout.setVisibility(View.INVISIBLE);
            requestCurrentWeather(weatherId, cityName);
        }
//        AQI
        if (aqiString != null){
            AQI aqi = Utility.handlerAQI(aqiString);
            showAqi(aqi);
        }else {
            aqiLayout.setVisibility(View.INVISIBLE);
            requestAqi(weatherId);
        }
//        Suggestion
        if (suggestionString != null){
            Suggestion suggestion = Utility.handlerSuggestion(suggestionString);
            showSuggestion(suggestion);
        }
        else {
            suggestionLayout.setVisibility(View.INVISIBLE);
            requestSuggestion(weatherId);
        }

//        手动刷新天气
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        设置一个final的weather_id和一个final的city_name用于手动刷新



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                final String weather_id = preferences.getString("weather_id", null);
                final String city_name = preferences.getString("city_name", null);
                Log.d(TAG, "onCreate: refresh weather_id: " + weather_id);
                Log.d(TAG, "onCreate: refresh city_name: " + city_name);
                requestWeather(weather_id);
                requestCurrentWeather(weather_id, city_name);
                requestAqi(weather_id);
                requestSuggestion(weather_id);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    /*
    初始化各控件
     */
    public void bindViews(){
        nowLayout = (LinearLayout)findViewById(R.id.now_layout);
        forecastLayout1 = (CardView)findViewById(R.id.forecast_layout_1);
        aqiLayout = (CardView)findViewById(R.id.aqi_layout);
        suggestionLayout = (CardView)findViewById(R.id.suggestion_layout);
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        navButton = (Button)findViewById(R.id.nav_button);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

    }

    /*
    根据天气id请求城市逐天天气信息
     */
    public void requestWeather(final String weatherId){
        String weatherUrl = "https://devapi.qweather.com/v7/weather/7d?location=" + weatherId + "&key=" + Utility.KEY;
        Log.d(TAG, "requestWeather: The weather url is: " + weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handlerWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("200".equals(weather.getCode())){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.putString("weather_id",weatherId);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        loadBingPic();
    }

    public void requestCurrentWeather(final String weatherId, final String cityName){
//        https://devapi.qweather.com/v7/weather/now?location=101042400&key=e752552475f6456ea0d333d3232c8773
        final String currentWeatherUrl = "https://devapi.qweather.com/v7/weather/now?location=" + weatherId + "&key=" + Utility.KEY;
        HttpUtil.sendOkHttpRequest(currentWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mContext, "获取当日天气失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final CurrentWeather currentWeather = Utility.handlerCurrentWeather(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentWeather != null && "200".equals(currentWeather.getCode())){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                            editor.putString("current_weather", responseText);
                            editor.putString("city_name", cityName);
                            editor.apply();
                            showCurrentWeather(currentWeather, cityName);
                        }else {
                            Toast.makeText(mContext, "获取当日天气失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void requestAqi(final String weatherId){
        //https://devapi.qweather.com/v7/air/now?location=101042400&key=e752552475f6456ea0d333d3232c8773
        final String aqiUrl = "https://devapi.qweather.com/v7/air/now?location=" +  weatherId + "&key=" + Utility.KEY;
        HttpUtil.sendOkHttpRequest(aqiUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Error Url: " + aqiUrl);
                        Toast.makeText(mContext, "请求实时空气质量信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final AQI aqi = Utility.handlerAQI(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (aqi != null && "200".equals(aqi.getCode())){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                            editor.putString("aqi", responseText);
                            editor.apply();
                            showAqi(aqi);
                        }else {
                            Log.d(TAG, "Error Url: " + aqiUrl);
                            Toast.makeText(mContext, "请求实时空气质量信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void requestSuggestion(final String weatherId){
//        https://devapi.qweather.com/v7/indices/1d?location=101042400&key=e752552475f6456ea0d333d3232c8773&type=1,2,8,10
        String suggestionUrl = "https://devapi.qweather.com/v7/indices/1d?location=" + weatherId + "&key=" + Utility.KEY + "&type=1,2,8";
        HttpUtil.sendOkHttpRequest(suggestionUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "获取生活建议失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Suggestion suggestion = Utility.handlerSuggestion(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (suggestion != null && "200".equals(suggestion.getCode())){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                            editor.putString("suggestion", responseText);
                            editor.apply();
                            showSuggestion(suggestion);
                        }else {
                            Toast.makeText(mContext, "获取生活建议失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    /*
    逐天天气(forecast)
     */
    private void showWeatherInfo(Weather weather){
        Log.d(TAG, "showWeatherInfo: ");
        forecastLayout.removeAllViews();
        List<Weather.DailyBean> forecastsList = weather.getDaily();
        for (Weather.DailyBean dailyBean: forecastsList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dateText.setText(dailyBean.getFxDate());
//            这里只显示了白天天气状况文字描述
            infoText.setText(dailyBean.getTextDay());
            maxText.setText(dailyBean.getTempMax());
            minText.setText(dailyBean.getTempMin());
            forecastLayout.addView(view);
        }
        forecastLayout1.setVisibility(View.VISIBLE);
    }

    /*
    show current weather
     */
    public void showCurrentWeather(CurrentWeather currentWeather, String cityName){
        Log.d(TAG, "showCurrentWeather: ");
        String updateTime  = currentWeather.getUpdateTime();
        String degree = currentWeather.getNow().getTemp();
        String weatherInfo = currentWeather.getNow().getText();
        Log.d(TAG, "showCurrentWeather: City Name" + cityName);
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime.substring(0, 10) + " " + updateTime.substring(11, 16));
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        nowLayout.setVisibility(View.VISIBLE);
    }

/*
        show aqi
 */
    public void showAqi(AQI aqi){
        aqiText.setText(aqi.getNow().getAqi());
        pm25Text.setText(aqi.getNow().getPm2p5());
        aqiLayout.setVisibility(View.VISIBLE);
    }

    /*
    show suggestion
     */
    public void showSuggestion(Suggestion suggestion){
        List<Suggestion.DailyBean> suggestionList = suggestion.getDaily();
        Suggestion.DailyBean comfortDaily = null;
        Suggestion.DailyBean carWashDaily = null;
        Suggestion.DailyBean sportDaily = null;
        for(Suggestion.DailyBean dailyBean: suggestionList){
            switch (dailyBean.getType()){
                case "1":
                    sportDaily = dailyBean;
                    break;
                case "2":
                    carWashDaily = dailyBean;
                    break;
                case "8":
                    comfortDaily = dailyBean;
                    break;
            }
        }
        comfortText.setText("舒适度: " + comfortDaily.getText());
        carWashText.setText("洗车指数: " + carWashDaily.getText());
        sportText.setText("运动建议: " + sportDaily.getText());
        suggestionLayout.setVisibility(View.VISIBLE);
    }
    /*
    加载必应每日一图
     */
    private void loadBingPic(){
        final String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(mContext).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
