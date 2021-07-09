package space.nianchu.selfcoolweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.DataInput;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import space.nianchu.selfcoolweather.db.City;
import space.nianchu.selfcoolweather.db.Country;
import space.nianchu.selfcoolweather.db.Province;
import space.nianchu.selfcoolweather.util.HttpUtil;
import space.nianchu.selfcoolweather.util.Utility;

//FIXME: 软件盘的显示与隐藏
//FIXME: back button的显示与隐藏
public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

//    为实现搜索功能而添加的控件
    private Button lookButton;
    private CardView lookForLayout;
    private Button lookCancel;
    private EditText lookForEdit;
    private InputMethodManager imm;
    /*
    省列表
     */
    private List<Province> provinceList;
       /*
    市列表
     */
       private List<City> cityList;

          /*
    县列表
     */
         private List<Country> countryList;
             /*
    选中的省份
     */
             private Province selectedProvince;
                /*
    选中的城市
     */
                private City selectedCity;
                   /*
    当前选中的级别
     */
                   private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton  = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1 ,dataList);
        listView.setAdapter(adapter);

        lookButton = (Button)view.findViewById(R.id.look_for);
        lookForLayout = (CardView)view.findViewById(R.id.look_for_layout);
        lookCancel = (Button)view.findViewById(R.id.look_cancel);
        lookForEdit = (EditText)view.findViewById(R.id.look_for_edit);
        imm = (InputMethodManager) lookForEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCountries();
                }else if (currentLevel == LEVEL_COUNTRY){
                    Country country = countryList.get(position);
                    String weatherId = country.getWeatherId().substring(2);
                    String cityName = country.getCountryName();
                    goToWeatherActivity(cityName, weatherId);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }else if (currentLevel == LEVEL_COUNTRY){
                    queryCities();
                }
            }
        });
//        Add for look
        lookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setVisibility(View.GONE);
                lookForLayout.setVisibility(View.VISIBLE);
                lookButton.setVisibility(View.GONE);
                lookCancel.setVisibility(View.VISIBLE);
                if (currentLevel == LEVEL_PROVINCE){
                backButton.setVisibility(View.GONE);}
//                Look For Edit 获得焦点
                lookForEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                lookForEdit.setHorizontallyScrolling(false);
                lookForEdit.setFocusable(true);
                lookForEdit.setFocusableInTouchMode(true);
                lookForEdit.requestFocus();
                hideShowKeyboard();
            }
        });

        lookCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookCancel.setVisibility(View.GONE);
                lookButton.setVisibility(View.VISIBLE);
                lookForLayout.setVisibility(View.GONE);
                titleText.setVisibility(View.VISIBLE);
                if (currentLevel == LEVEL_PROVINCE){
                backButton.setVisibility(View.GONE);}
                else {
                    backButton.setVisibility(View.VISIBLE);
                }
                hideShowKeyboard();
            }

        });
        lookForEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager inputMethodManager = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()){
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (!TextUtils.isEmpty(lookForEdit.getText().toString().trim())){
                                            String locationName = lookForEdit.getText().toString();
                    lookForEdit.setText("");
                            if (locationName != null){
                        String locationUrl = " https://geoapi.qweather.com/v2/city/lookup?location=" + locationName + "&key=" + Utility.KEY;
                        HttpUtil.sendOkHttpRequest(locationUrl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "查询城市信息失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                    String responseText = response.body().string();
                                    Location  location = Utility.handlerLocation(responseText);
                                    if ("200".equals(location.getCode())){
                                        Location.LocationBean targetLocation = location.getLocation().get(0);
                                        String cityName = targetLocation.getName();
                                        String weatherId = targetLocation.getId();
                                        goToWeatherActivity(cityName, weatherId);
                                    }
                                    hideLook();
                            }
                        });
                    }
                    }
                    return true;
                }
                return false;
            }
        });

        ((WeatherActivity)getActivity()).drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                hideShowKeyboard();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.INVISIBLE);
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0 ){
            dataList.clear();
            for (Province province: provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode  = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    private void queryCountries(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList = LitePal.where("cityid= ?", String.valueOf(selectedCity.getId())).find(Country.class);
        if (countryList.size() > 0){
            dataList.clear();
            for(Country country: countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTRY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "country");
        }
    }

    private void queryFromServer(final String address, final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Log.d(TAG, "run: Error: " + address);
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }
                else if ("city".equals(type)){
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                }
                else if ("country".equals(type)){
                    result = Utility.handleCountryResponse(responseText, selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("country".equals(type)){
                                queryCountries();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void goToWeatherActivity(final String cityName, final String weatherId){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getActivity() instanceof MainActivity){
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    Log.d(TAG, "onItemClick: City: " + cityName);
                    intent.putExtra("city_name", cityName);
                    startActivity(intent);
                    getActivity().finish();
                }else if (getActivity() instanceof WeatherActivity){
                    WeatherActivity activity = (WeatherActivity)getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.requestWeather(weatherId);
//                        activity.swipeRefresh.setRefreshing(true);
                    activity.requestCurrentWeather(weatherId, cityName);
                    activity.requestAqi(weatherId);
                    activity.requestSuggestion(weatherId);
                }
                lookForEdit.setFocusable(false);
            }
        });
    }
    /*
    显示/隐藏软件盘
     */
    public void hideShowKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /*
    在搜索城市之后移除搜索界面，换回城市名Title
     */
    public void hideLook(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentLevel != LEVEL_PROVINCE){
                    backButton.setVisibility(View.VISIBLE);
                }
                titleText.setVisibility(View.VISIBLE);
                lookForLayout.setVisibility(View.GONE);
                lookCancel.setVisibility(View.GONE);
                lookButton.setVisibility(View.VISIBLE);
            }
        });
    }
}
