package space.nianchu.selfcoolweather.db;

import org.litepal.crud.LitePalSupport;

public class Country extends LitePalSupport {
    private int id;
    private String countryName;
    private StringBuilder weatherId;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public StringBuilder getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(StringBuilder weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
