package space.nianchu.selfcoolweather.util;

import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.GenericSignatureFormatError;
import java.time.chrono.ThaiBuddhistEra;

import space.nianchu.selfcoolweather.AQI;
import space.nianchu.selfcoolweather.CurrentWeather;
import space.nianchu.selfcoolweather.Suggestion;
import space.nianchu.selfcoolweather.Weather;
import space.nianchu.selfcoolweather.db.City;
import space.nianchu.selfcoolweather.db.Country;
import space.nianchu.selfcoolweather.db.Province;

public class Utility {
    public static final String KEY = "e752552475f6456ea0d333d3232c8773";
    /*
    解析和处理服务器返回的数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            return false;
        }

        public static boolean handleCityResponse(String  response, int provinceId){
            if (!TextUtils.isEmpty(response)){
                try{
                    JSONArray allCities = new JSONArray(response);
                    for (int i = 0; i < allCities.length(); i++) {
                        JSONObject cityObject = allCities.getJSONObject(i);
                        City city = new City();
                        city.setCityName(cityObject.getString("name"));
                        city.setCityCode(cityObject.getInt("id"));
                        city.setProvinceId(provinceId);
                        city.save();
                    }
                    return true;
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        return false;
        }

        /*
        解析和处理服务器返回的县级数据
         */
        public static boolean handleCountryResponse(String response, int cityId){
            if (!TextUtils.isEmpty(response)){
                try {
                    JSONArray allCountries = new JSONArray(response);
                    for (int i = 0; i < allCountries.length(); i++) {
                        JSONObject countryObject = allCountries.getJSONObject(i);
                        Country country = new Country();
                        country.setCityId(cityId);
                        country.setCountryName(countryObject.getString("name"));
                        country.setWeatherId(countryObject.getString("weather_id"));
                        country.save();
                    }
                    return true;
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return false;
        }

        /*
        将返回的数据解析成Weather实体类
         */
        public static Weather handlerWeatherResponse(String response){
            return new Gson().fromJson(response, Weather.class);
        }
    /*
    将返回的数据解析成CurrentWeather实体类
     */
    public static CurrentWeather handlerCurrentWeather(String response){
            return new Gson().fromJson(response, CurrentWeather.class);
        }
        /*
        将返回的数据解析成AQI实体类
         */
        public static AQI handlerAQI(String response){
            return new Gson().fromJson(response, AQI.class);
        }
        /*
        将返回的数据解析成Suggestion实体类
         */
        public static Suggestion handlerSuggestion(String response){
            return new Gson().fromJson(response, Suggestion.class);
        }
    }
