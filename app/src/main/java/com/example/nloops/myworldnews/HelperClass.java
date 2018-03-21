package com.example.nloops.myworldnews;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A Helper class that contains alot of methods for various needs
 * the main purpose of them to help us make our Main Activities clean and organised.
 */

public class HelperClass {

    /**
     * Static constant API URL
     */
    public static final String NEWS_BASE_URL = "http://content.guardianapis.com/search?";

    /**
     * Class LOG TAG
     */
    private static final String LOG_TAG = HelperClass.class.getSimpleName();

    /**
     * Declare a Constant values for HTTP Connection Read , Connect Timeout also requestMethod.
     */
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String REQUEST_METHOD = "GET";

    /**
     * Declare JSON items Constant Keys
     */

    private static final String RESPONE_OBJECT = "response";
    private static final String RESULTS_ARRAY = "results";
    private static final String STRING_TYPE = "type";
    private static final String STRING_SECTION = "sectionName";
    private static final String STRING_TITLE = "webTitle";
    private static final String STRING_WEBURL = "webUrl";
    private static final String STRING_PUP_DATE = "webPublicationDate";


    public static List<CustomNews> grabData(String stringURL) {
        List<CustomNews> results = null;
        // combine all Helper methods we made below to get our data and match our needs.
        try {
            results = fetchJsonResponse(makeHttpConnection(createURL(stringURL)));
        } catch (IOException e) {
            Log.e(LOG_TAG, "there's a problem when try to get data because of " + e);
        }

        return results;
    }

    /**
     * Helper Method that take StringUrl and convert it into URL object.
     *
     * @param stringUrl
     * @return
     */
    private static URL createURL(String stringUrl) {

        // Declare URL
        URL url = null;
        // if stringUrl is nothing we return early
        if (stringUrl == null || stringUrl == "") {
            return url;
        }

        try {

            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Cannot Convert String to URL because of " + e);
        }

        return url;
    }

    /**
     * Helper Method takes HttpConnection stream and convert it into String
     * using InputStreamReader and BufferReader.
     *
     * @param inputStream
     * @return
     */
    private static String readFromServerString(InputStream inputStream) {

        // Declare StringBuilder to Hold Server Response
        // we choosed StringBuilder because we can append the string instead of String.
        StringBuilder stringResult = new StringBuilder();

        // if the inputStream is null we return early.
        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();

                while (line != null) {
                    stringResult.append(line);
                    line = reader.readLine();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Cannot read from stream because of " + e);
            }
        }

        return stringResult.toString();
    }

    /**
     * Helper Method that work on make HttpConnection and Get Response from Server.
     *
     * @param url
     * @return
     */
    private static String makeHttpConnection(URL url) throws IOException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // declare string that will hold server response.
        String serverRespone = "";
        // if url is null we return early.
        if (url == null) {
            return serverRespone;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            // Cast URL Connection into URL
            urlConnection = (HttpURLConnection) url.openConnection();
            // Set Connection Status
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            // Open the Connection.
            urlConnection.connect();

            // Check if we got successful connection.
            if (urlConnection.getResponseCode() == 200) {
                // Get the Stream of data that received from the server
                inputStream = urlConnection.getInputStream();
                // Convert the Stream into String.
                serverRespone = readFromServerString(inputStream);
            } else {

                Log.e(LOG_TAG, "unsuccessful connection " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }

        if (inputStream != null) {
            inputStream.close();
        }

        return serverRespone;
    }


    /**
     * Helper method wokring on take parameter String Server Response and parse JSON
     * and turn it into ArrayList of CustomNews.
     *
     * @param stringRespone
     * @return
     */
    private static List<CustomNews> fetchJsonResponse(String stringRespone) {

        if (stringRespone == null || stringRespone == "") {
            return null;
        }
        List<CustomNews> newsList = new ArrayList<>();


        try {
            // Get JSON ROOT
            JSONObject root = new JSONObject(stringRespone);
            // GET JSON OBJECT response.
            JSONObject response = root.getJSONObject(RESPONE_OBJECT);
            // GET JSON Array results.
            JSONArray results = response.getJSONArray(RESULTS_ARRAY);

            if (response.has(RESULTS_ARRAY)) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentObject = results.getJSONObject(i);
                    String type = currentObject.optString(STRING_TYPE);
                    String sectionName = currentObject.optString(STRING_SECTION);
                    String title = currentObject.optString(STRING_TITLE);
                    String webURL = currentObject.optString(STRING_WEBURL);
                    String pupDate = currentObject.optString(STRING_PUP_DATE);

                    CustomNews customNews = new CustomNews(type, sectionName, title, webURL, pupDate);
                    newsList.add(customNews);
                }

            } else {

                Log.e(LOG_TAG, "cannot find results Array in this object");
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "cannot parse JSON response because of " + e);
        }

        return newsList;

    }


    /**
     * Helper Method to Hide the Keyboard when required.
     *
     * @param activity
     */
    public static void hideKeyBoard(Activity activity) {

        InputMethodManager manager =
                (InputMethodManager) activity.getSystemService
                        (Activity.INPUT_METHOD_SERVICE);

        manager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);

    }


}
