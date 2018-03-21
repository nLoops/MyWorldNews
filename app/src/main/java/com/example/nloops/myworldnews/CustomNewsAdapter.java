package com.example.nloops.myworldnews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Custom Array Adapter to fill our listView with our needs
 */

public class CustomNewsAdapter extends ArrayAdapter<CustomNews> {


    // Constructor of the class.
    public CustomNewsAdapter(@NonNull Context context, @NonNull ArrayList<CustomNews> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // if View not created ever we create it.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_news_item, parent, false);
        }

        // Get ref of current object (CustomNews)
        CustomNews currentObject = getItem(position);
        // find the Views.
        TextView txtSection = (TextView) convertView.findViewById(R.id.txt_section);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
        TextView txtType = (TextView) convertView.findViewById(R.id.txt_type);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txt_date);


        // Change Date Format.
        String formattedDate = formatDate(currentObject.getPupDate());
        // set Views Data.
        txtSection.setText(currentObject.getSectionName());
        txtTitle.setText(currentObject.getTitle());
        txtType.setText(currentObject.getType());
        txtDate.setText(formattedDate);


        // add a custom fonts to our TextViews.
        Typeface section_font = Typeface.createFromAsset(this.getContext().getApplicationContext().getAssets(), "fonts/font_bold.ttf");
        Typeface title_font = Typeface.createFromAsset(this.getContext().getApplicationContext().getAssets(), "fonts/font_regular.ttf");
        txtSection.setTypeface(section_font);
        txtType.setTypeface(section_font);
        txtDate.setTypeface(section_font);
        txtTitle.setTypeface(title_font);

        // Change Color Depends of the Section name.
        String currentSection = currentObject.getSectionName();

        int mColor = 0;

        if (currentSection.equals(convertView.getResources().getString(R.string.sec_sport))) {
            mColor = R.color.sport_cat;
        } else if (currentSection.equals(convertView.getResources().getString(R.string.sec_football))) {
            mColor = R.color.sport_cat;
        } else if (currentSection.equals(convertView.getResources().getString(R.string.sec_soccer))) {
            mColor = R.color.sport_cat;
        } else if (currentSection.equals(convertView.getResources().getString(R.string.sec_art))) {
            mColor = R.color.art_cat;
        } else if (currentSection.equals(convertView.getResources().getString(R.string.sec_politics))) {
            mColor = R.color.politics_cat;
        } else if (currentSection.equals(convertView.getResources().getString(R.string.sec_world))) {
            mColor = R.color.politics_cat;
        } else if (currentSection.equals(convertView.getResources().getString(R.string.sec_bus))) {
            mColor = R.color.finance_cat;
        } else {
            mColor = R.color.default_color;
        }

        // set selected color to txtSection View.
        txtSection.setTextColor(convertView.getResources().getColor(mColor));

        return convertView;

    }


    /**
     * a Helper Method that take StringDate to Change Format
     *
     * @param strDate
     * @return
     */
    private String formatDate(String strDate) {
        String outputDate = "";
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("LLL dd, yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(strDate);
            outputDate = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }
}
