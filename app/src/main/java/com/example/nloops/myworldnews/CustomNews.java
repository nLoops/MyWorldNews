package com.example.nloops.myworldnews;

/**
 * Custom Class to fit our needs of displaying data on the ListView Using ArrayAdapter of this Custom Class.
 */

public class CustomNews {

    /**
     * Declare a required vars that will holds the data for each article.
     */
    private String mType;
    private String mSectionName;
    private String mTitle;
    private String mWebUrl;
    private String mPupDate;

    /**
     * Constructor of the Class.
     *
     * @param newsType
     * @param sectionName
     * @param title
     * @param webUrl
     */
    public CustomNews(String newsType, String sectionName, String title, String webUrl, String puplishDate) {
        this.mType = newsType;
        this.mSectionName = sectionName;
        this.mTitle = title;
        this.mWebUrl = webUrl;
        this.mPupDate = puplishDate;
    }

    /**
     * Vars Getters to help us to get the data once it required.
     *
     * @return
     */
    public String getType() {
        return mType;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getPupDate() {
        return mPupDate;
    }

    /**
     * CustomNews.toString()
     * it will help us if we need to check the data on Logger.
     *
     * @return
     */
    @Override
    public String toString() {
        return "CustomNews{" +
                "mType='" + mType + '\'' +
                ", mSectionName='" + mSectionName + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mWebUrl='" + mWebUrl + '\'' +
                ", mPupDate='" + mPupDate + '\'' +
                '}';
    }
}
