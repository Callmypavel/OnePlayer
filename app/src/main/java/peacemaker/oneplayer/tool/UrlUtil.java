package peacemaker.oneplayer.tool;

/**
 * Created by ouyan on 2016/11/27.
 */

public class UrlUtil {
    private final static String url = "123.207.122.128";
    private static int port = 6666;

    public static String getUrl() {
        return url;
    }

    public static int getPort() {
        return port;
    }
    public static void setPort(int port){
        UrlUtil.port = port;
    }
}
