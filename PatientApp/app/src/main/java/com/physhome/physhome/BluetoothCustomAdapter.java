package com.physhome.physhome;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Monster on 5.5.2017.
 */

public class BluetoothCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<BluetoothDevice> arrayList = new ArrayList<BluetoothDevice>();
    private static LayoutInflater inflater=null;
    public  BluetoothCustomAdapter(Context context, ArrayList<BluetoothDevice> list) {
        // TODO Auto-generated constructor stub
        this.context=context;
        arrayList = list;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tvName;
        TextView tvMAC;
        LinearLayout layout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.bt_item, null);
        holder.tvName=(TextView) rowView.findViewById(R.id.tv_btname);
        holder.tvMAC  = (TextView) rowView.findViewById(R.id.tv_btmac);
        holder.layout = (LinearLayout) rowView.findViewById(R.id.bt_layout);
        holder.tvName.setText(arrayList.get(position).getName());
        holder.tvMAC.setText(arrayList.get(position).getAddress());
        return rowView;
    }

}
