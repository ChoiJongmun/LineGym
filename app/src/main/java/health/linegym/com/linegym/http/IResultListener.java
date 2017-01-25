package health.linegym.com.linegym.http;

/**
 * Created by innotree12 on 2016-12-09.
 */

public interface IResultListener {

    public void onSuccess(String type, String result_string);
    public void onFailed(String type, String result_string);
    public void onException(String type, String error_message);
}
