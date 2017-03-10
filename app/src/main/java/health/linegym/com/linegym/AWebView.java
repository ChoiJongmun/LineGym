package health.linegym.com.linegym;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.TextView;

/**
 * Created by jongmun on 2017-03-04.
 */

public class AWebView extends BaseLineGymActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_webview_layout);
        findViewById(R.id.comm_title_btn_navi_back).setOnClickListener(onBaseClickListener);
        TextView title = (TextView) findViewById(R.id.tv_comm_title);

        WebView web_view = (WebView) findViewById(R.id.web_view);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setWebViewClient(new MyWebViewClient());
        web_view.getSettings().setPluginState(WebSettings.PluginState.ON);

        if(Build.VERSION.SDK_INT < 21)
        {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        }
        web_view.clearHistory();
        web_view.clearCache(true);
        WebViewDatabase.getInstance(this).clearHttpAuthUsernamePassword();
        //************************************************************

        web_view.getSettings().setSupportZoom(true);
        if(Build.VERSION.SDK_INT > 10)
        {
            web_view.getSettings().setDisplayZoomControls(false);
        }
        web_view.getSettings().setBuiltInZoomControls(true);
        web_view.getSettings().setUseWideViewPort(false);
        web_view.getSettings().setLoadWithOverviewMode(true);

        String pdfURL = "";
        if(getIntent().getBooleanExtra("is_private", false)) {
            pdfURL = "http://rain16boy.cafe24.com/private_individual.pdf";
            title.setText("개인정보 취급");
        }else {
            title.setText("서비스 이용방침");
            pdfURL = "http://rain16boy.cafe24.com/line_gym_member_agreement.pdf";
        }
        web_view.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + pdfURL);
    }

    public class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            // TODO Auto-generated method stub
//			view.loadUrl(url);
//			return false;
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
        {
            // TODO Auto-generated method stub

            handler.proceed("admin", "pass");
//			super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }

}
