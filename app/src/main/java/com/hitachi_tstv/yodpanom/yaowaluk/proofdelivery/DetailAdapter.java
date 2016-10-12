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

public class DetailAdapter extends BaseAdapter{

    //Explicit

    private Context context;
    private String[] workSheetStrings, storeNameStrings, planArrivalTimeStrings;
    private TextView workSheetTextView, storeNameTextView, planArrivalTimeTextView;


    public DetailAdapter(Context context, String[] workSheetStrings, String[] storeNameStrings, String[] planArrivalTimeStrings) {
        this.context = context;
        this.workSheetStrings = workSheetStrings;
        this.storeNameStrings = storeNameStrings;
        this.planArrivalTimeStrings = planArrivalTimeStrings;
    }

    @Override
    public int getCount() {
        return workSheetStrings.length;
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
        View view = layoutInflater.inflate(R.layout.deatil_listview, parent, false);

        //BindWidget

        workSheetTextView = (TextView) view.findViewById(R.id.textView11);
        storeNameTextView = (TextView) view.findViewById(R.id.textView12);
        planArrivalTimeTextView = (TextView) view.findViewById(R.id.textView13);


        //Show View
        workSheetTextView.setText(workSheetStrings[position]);
        storeNameTextView.setText(storeNameStrings[position]);
        planArrivalTimeTextView.setText(planArrivalTimeStrings[position]);




        return view;
    }
}   //Main Class