package com.hitachi_tstv.yodpanom.yaowaluk.proofdelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by musz on 10/12/2016.
 */

public class DateAdapter extends BaseAdapter{
    //Explicit
    private Context context;
    private String[] dateStrings,storeStrings,planIdStrings;

    public DateAdapter(Context context, String[] dateStrings, String[] storeStrings, String[] planIdStrings) {
        this.context = context;
        this.dateStrings = dateStrings;
        this.storeStrings = storeStrings;
        this.planIdStrings = planIdStrings;
    }

    @Override
    public int getCount() {
        return dateStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.date_listview, parent, false);

        //Bindwidget

        TextView dateTextView = (TextView) view.findViewById(R.id.textView6);
        TextView storeTextView = (TextView) view.findViewById(R.id.textView7);


        //ShowView

        dateTextView.setText("Date :" + dateStrings[position]);
        storeTextView.setText("Store " + storeStrings[position] + " place");


        return view;
    }
}   //Main  Class
