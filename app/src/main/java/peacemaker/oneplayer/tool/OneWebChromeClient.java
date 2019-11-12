package peacemaker.oneplayer.tool;

import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by ouyang on 2017/2/10.
 */

public class OneWebChromeClient extends WebChromeClient {
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        LogTool.log(this,"收到标题"+title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        LogTool.log(this,"加载进度"+newProgress);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        LogTool.log(this,"控制台信息"+consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);

    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        super.onPermissionRequest(request);
        LogTool.log(this,"需求权限"+request.toString());
    }

}
