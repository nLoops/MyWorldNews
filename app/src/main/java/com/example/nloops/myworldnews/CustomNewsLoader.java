package com.example.nloops.myworldnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loader to handle background working
 */

public class CustomNewsLoader extends AsyncTaskLoader<List<CustomNews>> {


    /**
     * Log tag
     */
    private static final String LOG_TAG = CustomNewsLoader.class.getSimpleName();

    // private String to hold String Criteria.
    private String mURL;

    public CustomNewsLoader(Context context, String stringUrl) {
        super(context);
        mURL = stringUrl;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<CustomNews> loadInBackground() {
        // if mUrl empty we return early
        if (mURL == null) {
            return null;
        }
        List<CustomNews> results = HelperClass.grabData(mURL);
        return results;

    }
}
