package health.linegym.com.linegym.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import health.linegym.com.linegym.LineGymDefine;

/**
 * Created by innotree12 on 2016-12-09.
 */

public class HttpConnector extends Thread {

    String mType = "";
    StringBuilder builder;
    String url = "";
    HashMap<String,String> mParams = new HashMap<>();
    IResultListener mListener;

    protected final static String 	CHAR_SET 		= "UTF-8";

    public HttpConnector(String mType, IResultListener listener) {
        this.mType = mType;
        mListener = listener;
    }

    public void login(String name , String phone_no) {
        url = LineGymDefine.COMM_URL + LineGymDefine.SERVER_API_LOGIN_URL;
//        System.out.println("Url = " + url);
        mParams.clear();
        mParams.put(LineGymDefine.SERVER_API_PARAM_NAME_KEY, name);
        mParams.put(LineGymDefine.SERVER_API_PARAM_PHONE_KEY, phone_no);

        start();
    }

    public void getMainData(String name, String mem_id) {
        url = LineGymDefine.COMM_URL + LineGymDefine.SERVER_API_MAIN_DATA;
        mParams.clear();
        mParams.put(LineGymDefine.SERVER_API_PARAM_NAME_KEY, name);
        mParams.put(LineGymDefine.SERVER_API_PARAM_MEMNO_KEY, mem_id);
        System.out.println("Url = " + url);
        start();
    }

    public void getAttendDays(String name, String year, String month) {
        url = LineGymDefine.COMM_URL + LineGymDefine.SERVER_API_ATTEND_DAY_LIST;
        mParams.clear();
        mParams.put(LineGymDefine.SERVER_API_PARAM_NAME_KEY, name);
        mParams.put(LineGymDefine.SERVER_API_PARAM_YEAR, year);
        mParams.put(LineGymDefine.SERVER_API_PARAM_MONTH, month);
        System.out.println("Url = " + url);
        start();
    }

    public void getAttendMonth(String name) {
        url = LineGymDefine.COMM_URL + LineGymDefine.SERVER_API_ATTEND_MONTH_LIST;
        mParams.clear();
        mParams.put(LineGymDefine.SERVER_API_PARAM_NAME_KEY, name);
        System.out.println("Url = " + url);
        start();
    }

    public void getInbodyList(String mem_no) {
        url = LineGymDefine.COMM_URL + LineGymDefine.SERVER_API_INBODY_LIST;
        mParams.clear();
        mParams.put(LineGymDefine.SERVER_API_PARAM_MEMNO_KEY, mem_no);
        System.out.println("Url = " + url);
        start();
    }

    public void getMyLastDate(String name, String mem_no) {
        url = String.format(LineGymDefine.COMM_URL + LineGymDefine.SERVER_API_MY_LAST_DATE, name, mem_no);
        mParams.clear();
        mParams.put(LineGymDefine.SERVER_API_PARAM_NAME_KEY, name);
        mParams.put(LineGymDefine.SERVER_API_PARAM_MEMNO_KEY, mem_no);
        start();
    }

    @Override
    public void run() {
        try {

            HttpURLConnection con = makeConnection();
            String request = getRequest();

            if(request != null && request.length() > 0)
            {
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream()));
                pw.write(request);
                pw.flush();
            }

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            if(response.toString().contains("code:\"0000\"")) {
                mListener.onSuccess(mType,response.toString());
            }else {
                mListener.onFailed(mType,response.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
            mListener.onException(mType,"서버와의 연결이 정상적으로 이루어 지지 않았습니다.");
        }
//        super.run();
    }

    private HttpURLConnection makeConnection() throws IOException
    {
        HttpURLConnection	conn 	= null;
        int					count 	= 0;


        if(url != null)
        {
            try
            {
                URL url = new URL(this.url);
                conn =(HttpURLConnection) url.openConnection();

                conn.setConnectTimeout(2000);

                conn.setRequestMethod("POST");
                conn.setDefaultUseCaches(false);
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFollowRedirects(false);
                conn.setChunkedStreamingMode(0);

            }
            catch (IOException e)
            {
                if(conn != null)
                {
                    conn.disconnect();
                    conn = null;
                }

                throw e;
            }
        }
        return conn;
    }

    protected String getRequest()
    {
        StringBuilder sBuilder = new StringBuilder();

        if(mParams != null && mParams.size() > 0)
        {
            int count = 0;
            for( Map.Entry<String, String> req : mParams.entrySet())
            {
                if(req != null)
                {
                    try
                    {
                        if(count != 0)
                        {
                            sBuilder.append("&");
                        }

                        /*************************************************/
                        String strKey		= req.getKey();
                        String strValue 	= req.getValue();

                        sBuilder
                                .append(URLEncoder.encode(strKey, CHAR_SET))
                                .append("=")
                                .append(strValue);

                        /*************************************************/
                    }
                    catch(Exception e)
                    {
                        return "";
                    }
                    count++;
                }
            }
        }
        return sBuilder.toString();
    }

}
