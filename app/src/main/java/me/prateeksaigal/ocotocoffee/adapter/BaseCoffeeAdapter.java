package me.prateeksaigal.ocotocoffee.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import me.prateeksaigal.ocotocoffee.R;

public class BaseCoffeeAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> names;
    private ArrayList<Integer> drawables;
    private ArrayList<Integer> cofIds;
    private ArrayList<Integer> types;

    public BaseCoffeeAdapter(Activity act, ArrayList<String> names, ArrayList<Integer> drawables, ArrayList<Integer> cofIds, ArrayList<Integer> types) {
        this.activity = act;
        this.names = names;
        this.drawables = drawables;
        this.cofIds = cofIds;
        this.types = types;

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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.base_coffee_single, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtItemName = (TextView) convertView.findViewById(R.id.coffee_name);
        holder.imageView = (ImageView) convertView.findViewById(R.id.coffee_thumbnail);
        holder.cof_id = (TextView) convertView.findViewById(R.id.cof_id);
        holder.type = (TextView) convertView.findViewById(R.id.type);
        holder.imageView.setImageResource(drawables.get(position));
        holder.txtItemName.setText(names.get(position));
        holder.cof_id.setText(cofIds.get(position) + "");
        holder.type.setText(types.get(position) + "");

        return convertView;
    }

    static class ViewHolder {
        TextView txtItemName;
        ImageView imageView;
        TextView cof_id;
        TextView type;
    }
}
