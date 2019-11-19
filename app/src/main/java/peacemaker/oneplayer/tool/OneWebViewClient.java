package peacemaker.oneplayer.tool;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ouyang on 2017/2/9.
 */

public class OneWebViewClient extends WebViewClient {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        LogTool.log(this,"页面开始");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogTool.log(this,"页面结束");
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        if (Build.VERSION.SDK_INT >= 21) {
            LogTool.log(this,"http错误:错误分析"+errorResponse.getReasonPhrase()+",错误码"+errorResponse.getStatusCode());

        }

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (Build.VERSION.SDK_INT >= 23) {
            LogTool.log(this,"错误"+ error.getDescription());
        }
    }


}
