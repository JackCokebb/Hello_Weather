package edu.skku.cs.personalproject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoclistAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<recent_loc> locList;

    public LoclistAdapter(Context mContext, ArrayList<recent_loc> locList) {
        this.mContext = mContext;
        this.locList = locList;
    }

    @Override
    public int getCount() {
        return locList.size();
    }

    @Override
    public Object getItem(int i) {
        return locList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.loc_element, viewGroup, false);
        }
        TextView loc_desc = view.findViewById(R.id.loc_title);
        ImageView loc_icon = view.findViewById(R.id.loc_icon);
        //LinearLayout touchable = view.findViewById(R.id.touchable_layout);
        List<Address> citylist = null;
        final Geocoder geocoder = new Geocoder(mContext);
        try {
            citylist = geocoder.getFromLocation(locList.get(i).lat,locList.get(i).lon,10);
            if(citylist != null) {
                String city = citylist.get(0).	getAddressLine(0);
                loc_desc.setText(city);
                loc_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic__f50d));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loc_desc.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, WeatherInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("lat",locList.get(i).lat);
            intent.putExtra("lon",locList.get(i).lon);
            mContext.startActivity(intent);
        });

        return view;
    }
}
