package com.example.nloops.myworldnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<CustomNews>>,
        SearchView.OnQueryTextListener {

    /**
     * Class LOG_TAG
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Async LOADER constant ID
     */
    private static final int LOADER_ID = 1;

    /**
     * Ref of ListView
     */
    private ListView mListView;

    /**
     * Ref of Adapter.
     */
    private CustomNewsAdapter mAdapter;

    /**
     * ref of emptyView
     */
    private TextView mEmptyView;

    /**
     * ref of ProgressBar
     */
    private ProgressBar mProgBar;

    /**
     * flag to check if this is first RUN
     */
    private boolean isFirstRun = true;

    // Search View Elements
    private MenuItem mSearchItem;
    private SearchView mSearchView;
    private String mSearchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link Progress Bar
        mProgBar = (ProgressBar) findViewById(R.id.bar_prog);
        // link Empty TextView
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        // link ListView var with Layout ListView
        mListView = (ListView) findViewById(R.id.news_listView);
        // Assign emptyView to ListView
        mListView.setEmptyView(mEmptyView);
        // Initialize Adapter Ref.
        mAdapter = new CustomNewsAdapter(this, new ArrayList<CustomNews>());


        // Set on Item Click Listener to send implicit intent to web browser
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Get the Current Item
                CustomNews currentObject = mAdapter.getItem(i);
                if (currentObject.getWebUrl() != null || currentObject.getWebUrl() != "") {
                    // Declare a new Object of URI
                    Uri uri = Uri.parse(currentObject.getWebUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Log.e(LOG_TAG, "The Current Object has no URL item");
                }
            }
        });
        // Set Adapter to the ListView
        mListView.setAdapter(mAdapter);

        // Check if there is a internet connection our not.
        if (isNetworkConnected()) {

            // get ref of LoaderManager to handle LoaderManager.
            final LoaderManager loaderManager = getLoaderManager();

            // Initialize the Loader with unique ID.
            loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
        } else {

            // hide progress bar.
            mProgBar.setVisibility(View.GONE);
            // set EmptyView text.
            mEmptyView.setText(R.string.no_internet);
        }

    }


    @Override
    public Loader<List<CustomNews>> onCreateLoader(int i, Bundle bundle) {

        // get Stored Prefs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minPages = sharedPreferences.getString(getString(R.string.setting_min_page_key),
                getString(R.string.setting_min_pages_default)
        );

        // Sections Prefs
        String sections = sharedPreferences.getString(
                getString(R.string.setting_section_key),
                getString(R.string.setting_section_default)
        );

        Uri baseURI = Uri.parse(HelperClass.NEWS_BASE_URL);
        Uri.Builder uriBuilder = baseURI.buildUpon();

        // if not first run we will display the NEWEST news on the API
        // to avoid null query.
        if (!isFirstRun) {
            uriBuilder.appendQueryParameter("q", mSearchKey);
        }
        // if there's no section choosed we display all the data.
        if (!sections.equals(getString(R.string.setting_section_all_value))) {
            uriBuilder.appendQueryParameter("section", sections);
        }
        uriBuilder.appendQueryParameter("page-size", minPages);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new CustomNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<CustomNews>> loader, List<CustomNews> customNews) {

        // make progress bar INVISIBLE
        mProgBar.setVisibility(View.INVISIBLE);
        // Set EmptyView text.
        mEmptyView.setText(R.string.no_news);

        // Clear Adapter before filling it.
        mAdapter.clear();

        if (customNews != null && !customNews.isEmpty()) {
            mAdapter.addAll(customNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<CustomNews>> loader) {

        mAdapter.clear();
    }

    /**
     * @return state of current network True if internet available , false if not.
     */
    private Boolean isNetworkConnected() {
        ConnectivityManager conMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // find SearchItem.
        mSearchItem = menu.findItem(R.id.search_view);
        // Assign SearchView.
        mSearchView = (SearchView) mSearchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // open Setting Activity when click on Menu Button.
        int id = item.getItemId();
        if (id == R.id.setting_view_btn) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (isNetworkConnected()) {

            // update FirstRun Boolean
            isFirstRun = false;
            // make progress Bar VISIBLE
            mProgBar.setVisibility(View.VISIBLE);
            // update search String
            mSearchKey = query;
            // restart the current LoaderManager.
            getLoaderManager().restartLoader(LOADER_ID, null, this);

            // Set Query
            mSearchView.setQuery(query, false);

            // clear search word from search bar
            mSearchView.setIconified(true);
            // Hide keyboard
            HelperClass.hideKeyBoard(this);
        } else {

            // hide progress bar.
            mProgBar.setVisibility(View.GONE);
            // set EmptyView text.
            mEmptyView.setText(R.string.no_internet);
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
