package com.example.smartbox_app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class DeliveryAdapter extends ArrayAdapter<Delivery> {
    public DeliveryAdapter(Context context, List<Delivery> object){
        super(context,0,object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_delivery, parent, false);
        }


        TextView DateTimeTextView = (TextView) convertView.findViewById(R.id.delivery_dt);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.delivery_name);
        TextView tracknoTextView = (TextView) convertView.findViewById(R.id.delivery_trackno);
        TextView typeTextView = (TextView) convertView.findViewById(R.id.delivery_type);
        TextView weightTextView = (TextView) convertView.findViewById(R.id.delivery_weight);

        Delivery delivery = getItem(position);

        DateTimeTextView.setText(delivery.getDate_Time());
        nameTextView.setText(delivery.getName());
        tracknoTextView.setText(delivery.getTrackno());
        typeTextView.setText(delivery.getType());
        weightTextView.setText(delivery.getWeight());

        return convertView;
    }


}
