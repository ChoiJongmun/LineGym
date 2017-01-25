package health.linegym.com.linegym.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jongmun on 2017-01-14.
 */

public class Inbody {

    List<InbodyData> rows = new ArrayList<>();

    public List<InbodyData> getRows() {
        return rows;
    }

    public void setRows(List<InbodyData> rows) {
        this.rows = rows;
    }

    public class InbodyData {
        String body_point = "";
        String datetime = "";
        String weight = "";
        String muscle = "";
        String fat = "";
        String fat_per = "";

        public String getBody_point() {
            return body_point;
        }

        public void setBody_point(String body_point) {
            this.body_point = body_point;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getMuscle() {
            return muscle;
        }

        public void setMuscle(String muscle) {
            this.muscle = muscle;
        }

        public String getFat() {
            return fat;
        }

        public void setFat(String fat) {
            this.fat = fat;
        }

        public String getFat_per() {
            return fat_per;
        }

        public void setFat_per(String fat_per) {
            this.fat_per = fat_per;
        }
    }

}
