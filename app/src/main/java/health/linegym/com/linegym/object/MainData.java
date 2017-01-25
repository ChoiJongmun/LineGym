package health.linegym.com.linegym.object;

import java.io.Serializable;

/**
 * Created by jongmun on 2017-01-02.
 */

public class MainData implements Serializable {

    String count = "0";
    String fitness = "0";

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getFitness() {
        return fitness;
    }

    public void setFitness(String fitness) {
        this.fitness = fitness;
    }
}
