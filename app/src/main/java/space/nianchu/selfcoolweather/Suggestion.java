package space.nianchu.selfcoolweather;

import java.util.List;

public class Suggestion {
    /**
     * code : 200
     * updateTime : 2021-07-04T09:36+08:00
     * fxLink : http://hfx.link/2cn2
     * daily : [{"date":"2021-07-04","type":"2","name":"洗车指数","level":"4","category":"不宜","text":"不宜洗车，路面积水较多，不宜擦洗汽车。如果执意擦洗，要做好溅上泥水的心理准备。"},{"date":"2021-07-04","type":"1","name":"运动指数","level":"2","category":"较适宜","text":"天气较好，较适宜进行各种运动，但因湿度偏高，请适当降低运动强度。"},{"date":"2021-07-04","type":"8","name":"舒适度指数","level":"6","category":"不舒适","text":"白天天气多云，并且空气湿度偏大，在这种天气条件下，您会感到有些闷热，不舒适。"}]
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
         * date : 2021-07-04
         * type : 2
         * name : 洗车指数
         * level : 4
         * category : 不宜
         * text : 不宜洗车，路面积水较多，不宜擦洗汽车。如果执意擦洗，要做好溅上泥水的心理准备。
         */

        private String date;
        private String type;
        private String name;
        private String level;
        private String category;
        private String text;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
