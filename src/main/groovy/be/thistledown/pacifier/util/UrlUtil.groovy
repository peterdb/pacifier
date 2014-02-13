package be.thistledown.pacifier.util

public class UrlUtil {

    /**
     * Copied from http://stackoverflow.com/questions/4826061/what-is-the-fastest-way-to-get-the-domain-host-name-from-a-url?lq=1
     */
    public static String getHost(String url){
        if(url == null || url.length() == 0)
            return "";

        int doubleslash = url.indexOf("//");
        if(doubleslash == -1)
            doubleslash = 0;
        else
            doubleslash += 2;

        int end = url.indexOf('/', doubleslash);
        end = end >= 0 ? end : url.length();

        int port = url.indexOf(':', doubleslash);
        end = (port > 0 && port < end) ? port : end;

        return url.substring(doubleslash, end);
    }
}
