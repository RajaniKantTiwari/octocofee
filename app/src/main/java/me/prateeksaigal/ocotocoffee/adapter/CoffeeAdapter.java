package me.prateeksaigal.ocotocoffee.adapter;

/**
 * Created by Prateek Saigal on 30-10-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.prateeksaigal.ocotocoffee.R;

/**
 * Created by Prateek Saigal on 22-06-2016.
 */
public class CoffeeAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> names;
    private ArrayList<Integer> drawables;
    private  ArrayList<Integer> cofIds;
    public CoffeeAdapter(Activity act, ArrayList<String> names, ArrayList<Integer> drawables, ArrayList<Integer> cofIds)
    {
        this.activity=act;
        this.names = names;
        this.drawables = drawables;
        this.cofIds = cofIds;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.coffee_layout,null);
            holder=new ViewHolder();
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.txtItemName=(TextView) convertView.findViewById(R.id.coffee_name);
        holder.imageView = (ImageView) convertView.findViewById(R.id.coffee_thumbnail);
        holder.cof_id = (TextView) convertView.findViewById(R.id.cof_id);
        holder.imageView.setImageResource(drawables.get(position));
        holder.txtItemName.setText(names.get(position));
        holder.cof_id.setText(cofIds.get(position) + "");

        return convertView;
    }

    static class ViewHolder {
        TextView txtItemName;
        ImageView imageView;
        TextView cof_id;
    }
}
