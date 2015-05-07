package org.tbw.FemurShield.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.R;

import java.util.List;

/**
 * Created by Marco on 07/05/2015.
 */
public class EmailListAdapter extends ArrayAdapter {

    public EmailListAdapter(Context context, List<EmailListItem> items) {
        super(context, R.layout.email_list_item, items);
    }

    // uso il pattern view holder che mi permette di evitare di richiamare spesso findViewById
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.email_list_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEmailItemName);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvEmailItemAddress);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        EmailListItem item =(EmailListItem) getItem(position);
        viewHolder.tvName.setText(item.name);
        viewHolder.tvAddress.setText(item.address);

        return convertView;
    }

    /**
     * Implementa il pattern View-Holder
     * poco utile visto che abbiamo poche voci ma in futuro non si sa mai
     *
     * vedi http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        TextView tvName;
        TextView tvAddress;
    }
}
