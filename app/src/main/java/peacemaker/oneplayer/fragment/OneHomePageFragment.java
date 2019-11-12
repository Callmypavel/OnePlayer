package peacemaker.oneplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.tool.OneWebChromeClient;
import peacemaker.oneplayer.tool.OneWebViewClient;
import peacemaker.oneplayer.tool.StringUtil;


/**
 * Created by ouyang on 2017/2/16.
 */

public class OneHomePageFragment extends Fragment {
    public WebView webView;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new OneWebViewClient());
        webView.setWebChromeClient(new OneWebChromeClient());
        webView.loadUrl(StringUtil.getUrl_background());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_homepage_fragment,container,false);
        webView = (WebView) view.findViewById(R.id.webview);

        return view;
    }
}
