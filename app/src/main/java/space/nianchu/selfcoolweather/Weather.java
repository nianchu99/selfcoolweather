package space.nianchu.selfcoolweather;

import java.util.List;

public class Weather {

    /**
     * code : 200
     * updateTime : 2021-07-03T21:35+08:00
     * fxLink : http://hfx.link/2cn1
     * daily : [{"fxDate":"2021-07-03","sunrise":"05:50","sunset":"19:54","moonrise":"01:16","moonset":"14:02","moonPhase":"残月","tempMax":"28","tempMin":"23","iconDay":"306","textDay":"中雨","iconNight":"305","textNight":"小雨","wind360Day":"214","windDirDay":"西南风","windScaleDay":"1-2","windSpeedDay":"4","wind360Night":"135","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"92","precip":"1.0","pressure":"945","vis":"23","cloud":"55","uvIndex":"3"},{"fxDate":"2021-07-04","sunrise":"05:51","sunset":"19:54","moonrise":"01:45","moonset":"14:56","moonPhase":"残月","tempMax":"31","tempMin":"24","iconDay":"305","textDay":"小雨","iconNight":"306","textNight":"中雨","wind360Day":"135","windDirDay":"东南风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"135","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"97","precip":"1.0","pressure":"945","vis":"10","cloud":"55","uvIndex":"9"},{"fxDate":"2021-07-05","sunrise":"05:51","sunset":"19:54","moonrise":"02:14","moonset":"15:51","moonPhase":"残月","tempMax":"27","tempMin":"23","iconDay":"307","textDay":"大雨","iconNight":"307","textNight":"大雨","wind360Day":"225","windDirDay":"西南风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"135","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"98","precip":"31.9","pressure":"943","vis":"9","cloud":"77","uvIndex":"3"},{"fxDate":"2021-07-06","sunrise":"05:51","sunset":"19:53","moonrise":"02:47","moonset":"16:46","moonPhase":"残月","tempMax":"27","tempMin":"23","iconDay":"306","textDay":"中雨","iconNight":"306","textNight":"中雨","wind360Day":"180","windDirDay":"南风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"180","windDirNight":"南风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"98","precip":"23.7","pressure":"946","vis":"11","cloud":"87","uvIndex":"3"},{"fxDate":"2021-07-07","sunrise":"05:52","sunset":"19:53","moonrise":"03:24","moonset":"17:42","moonPhase":"残月","tempMax":"28","tempMin":"24","iconDay":"305","textDay":"小雨","iconNight":"306","textNight":"中雨","wind360Day":"270","windDirDay":"西风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"90","windDirNight":"东风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"97","precip":"2.3","pressure":"947","vis":"13","cloud":"63","uvIndex":"5"},{"fxDate":"2021-07-08","sunrise":"05:52","sunset":"19:53","moonrise":"04:05","moonset":"18:37","moonPhase":"残月","tempMax":"29","tempMin":"23","iconDay":"305","textDay":"小雨","iconNight":"306","textNight":"中雨","wind360Day":"180","windDirDay":"南风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"135","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"93","precip":"2.7","pressure":"948","vis":"24","cloud":"66","uvIndex":"4"},{"fxDate":"2021-07-09","sunrise":"05:53","sunset":"19:53","moonrise":"04:51","moonset":"19:30","moonPhase":"残月","tempMax":"31","tempMin":"24","iconDay":"305","textDay":"小雨","iconNight":"154","textNight":"阴","wind360Day":"90","windDirDay":"东风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"135","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"3","humidity":"84","precip":"1.0","pressure":"946","vis":"25","cloud":"55","uvIndex":"8"}]
     * refer : {"sources":["Weather China"],"license":["no commercial use"]}
     */

    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private List<DailyBean> daily;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public List<DailyBean> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyBean> daily) {
        this.daily = daily;
    }

    public static class ReferBean {
        private List<String> sources;
        private List<String> license;

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }

    public static class DailyBean {
        /**
         * fxDate : 2021-07-03
         * sunrise : 05:50
         * sunset : 19:54
         * moonrise : 01:16
         * moonset : 14:02
         * moonPhase : 残月
         * tempMax : 28
         * tempMin : 23
         * iconDay : 306
         * textDay : 中雨
         * iconNight : 305
         * textNight : 小雨
         * wind360Day : 214
         * windDirDay : 西南风
         * windScaleDay : 1-2
         * windSpeedDay : 4
         * wind360Night : 135
         * windDirNight : 东南风
         * windScaleNight : 1-2
         * windSpeedNight : 3
         * humidity : 92
         * precip : 1.0
         * pressure : 945
         * vis : 23
         * cloud : 55
         * uvIndex : 3
         */

        private String fxDate;
        private String sunrise;
        private String sunset;
        private String moonrise;
        private String moonset;
        private String moonPhase;
        private String tempMax;
        private String tempMin;
        private String iconDay;
        private String textDay;
        private String iconNight;
        private String textNight;
        private String wind360Day;
        private String windDirDay;
        private String windScaleDay;
        private String windSpeedDay;
        private String wind360Night;
        private String windDirNight;
        private String windScaleNight;
        private String windSpeedNight;
        private String humidity;
        private String precip;
        private String pressure;
        private String vis;
        private String cloud;
        private String uvIndex;

        public String getFxDate() {
            return fxDate;
        }

        public void setFxDate(String fxDate) {
            this.fxDate = fxDate;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public void setMoonrise(String moonrise) {
            this.moonrise = moonrise;
        }

        public String getMoonset() {
            return moonset;
        }

        public void setMoonset(String moonset) {
            this.moonset = moonset;
        }

        public String getMoonPhase() {
            return moonPhase;
        }

        public void setMoonPhase(String moonPhase) {
            this.moonPhase = moonPhase;
        }

        public String getTempMax() {
            return tempMax;
        }

        public void setTempMax(String tempMax) {
            this.tempMax = tempMax;
        }

        public String getTempMin() {
            return tempMin;
        }

        public void setTempMin(String tempMin) {
            this.tempMin = tempMin;
        }

        public String getIconDay() {
            return iconDay;
        }

        public void setIconDay(String iconDay) {
            this.iconDay = iconDay;
        }

        public String getTextDay() {
            return textDay;
        }

        public void setTextDay(String textDay) {
            this.textDay = textDay;
        }

        public String getIconNight() {
            return iconNight;
        }

        public void setIconNight(String iconNight) {
            this.iconNight = iconNight;
        }

        public String getTextNight() {
            return textNight;
        }

        public void setTextNight(String textNight) {
            this.textNight = textNight;
        }

        public String getWind360Day() {
            return wind360Day;
        }

        public void setWind360Day(String wind360Day) {
            this.wind360Day = wind360Day;
        }

        public String getWindDirDay() {
            return windDirDay;
        }

        public void setWindDirDay(String windDirDay) {
            this.windDirDay = windDirDay;
        }

        public String getWindScaleDay() {
            return windScaleDay;
        }

        public void setWindScaleDay(String windScaleDay) {
            this.windScaleDay = windScaleDay;
        }

        public String getWindSpeedDay() {
            return windSpeedDay;
        }

        public void setWindSpeedDay(String windSpeedDay) {
            this.windSpeedDay = windSpeedDay;
        }

        public String getWind360Night() {
            return wind360Night;
        }

        public void setWind360Night(String wind360Night) {
            this.wind360Night = wind360Night;
        }

        public String getWindDirNight() {
            return windDirNight;
        }

        public void setWindDirNight(String windDirNight) {
            this.windDirNight = windDirNight;
        }

        public String getWindScaleNight() {
            return windScaleNight;
        }

        public void setWindScaleNight(String windScaleNight) {
            this.windScaleNight = windScaleNight;
        }

        public String getWindSpeedNight() {
            return windSpeedNight;
        }

        public void setWindSpeedNight(String windSpeedNight) {
            this.windSpeedNight = windSpeedNight;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public String getCloud() {
            return cloud;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getUvIndex() {
            return uvIndex;
        }

        public void setUvIndex(String uvIndex) {
            this.uvIndex = uvIndex;
        }
    }
}