package org.tbw.FemurShield.Controller.Settings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.R;

import java.util.List;

/**
 * @author Marco Biasin
 *
 * Adapter per gestire gli elementi della lista impostazioni sfruttando il pattern View Holder
 */
public class SettingListAdapter extends ArrayAdapter {

    public SettingListAdapter(Context context, List<SettingListItem> items) {
        super(context, R.layout.settings_list_item, items);
    }

    // uso il pattern view holder che mi permette di evitare di richiamare spesso findViewById
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.settings_list_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivSettingItemIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvSettingItemTitle);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvSettingItemDescription);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        SettingListItem item =(SettingListItem) getItem(position);
        viewHolder.ivIcon.setImageDrawable(item.icon);
        viewHolder.tvTitle.setText(item.title);
        viewHolder.tvDescription.setText(item.description);

        return convertView;
    }

    /**
     * Implementa il pattern View-Holder
     *
     * vedi http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;
    }
}

