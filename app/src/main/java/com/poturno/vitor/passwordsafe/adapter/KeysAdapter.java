package com.poturno.vitor.passwordsafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.poturno.vitor.passwordsafe.R;
import com.poturno.vitor.passwordsafe.model.Key;

import java.util.ArrayList;

/**
 * Created by vitor on 20/11/2017.
 */

public class KeysAdapter extends ArrayAdapter<Key>{

    private ArrayList<Key> keys;
    private Context context;

    public KeysAdapter(Context context, ArrayList<Key> objects){
        super(context,0,objects);
        this.context = context;
        this.keys = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View _view = convertView;
        ViewHolder vh;

        if (keys!=null){
            if (_view==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                _view = inflater.inflate(R.layout.key_list, parent, false);
                vh = new ViewHolder();
                vh.keyName = (TextView)_view.findViewById(R.id.txt_key_name);
                _view.setTag(vh);
            }else{
                vh = (ViewHolder) _view.getTag();
            }

            Key key = keys.get(position);
            vh.keyName.setText(key.getKeyName());

            if(position%2==1){
                _view.setBackgroundResource(R.color.colorWhite);
            }else{
                _view.setBackgroundResource(R.color.colorClear);
            }
        }

        return _view;
    }

    private static class ViewHolder{
        public TextView keyName;
    }

}
