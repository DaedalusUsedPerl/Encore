package cs130project.encore;

import com.loopj.android.http.*;


/**
 * This class contains static utility methods for interacting with the backend API
 */
public class Api {
    private static final String BASE_URL = "http://encoreapp.me/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * Call the HTTP GET method.
     * @param url The resource requested.
     * @param params Any additional parameters.
     * @param responseHandler A handler that accepts the result
     */
    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Call the HTTP POST method
     * @param url The URL to post to.
     * @param params Any additional parameters.
     * @param responseHandler A handler that accepts the result
     */
    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Call the HTTP DELETE method
     * @param url The resoure to delete
     * @param responseHandler A handler that accepts the result
     */
    public static void delete(String url, JsonHttpResponseHandler responseHandler) {
        client.delete(getAbsoluteUrl(url), responseHandler);
    }

    /**
     * Return the absolute URL of a resource.
     * @param relativeUrl The URL of a resource relative to the base url
     * @return the absolute URL of a resource
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}