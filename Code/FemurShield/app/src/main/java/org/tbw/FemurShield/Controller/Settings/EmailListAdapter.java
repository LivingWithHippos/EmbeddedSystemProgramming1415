package org.tbw.FemurShield.Controller.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.tbw.FemurShield.R;

import java.util.List;

/**
 * Estensione della classe adapter per poter utilizzare il pattern View-Holder sulla lista contatti
 *
 *@author Marco Biasin
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
            // inflate del layout degli oggetti della lista
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.email_list_item, parent, false);

            // inizializzo la view holder
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEmailItemName);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tvEmailItemAddress);
            convertView.setTag(viewHolder);
        } else {
            // riciclo la view holder gia' pronta
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // aggiorno la item view
        EmailListItem item =(EmailListItem) getItem(position);
        viewHolder.tvName.setText(item.name);
        viewHolder.tvAddress.setText(item.address);

        return convertView;
    }

    /**
     * Implementa il pattern View-Holder per la lista di contatti
     *
     * vedi {@see http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder}
     */
    private static class ViewHolder {
        TextView tvName;
        TextView tvAddress;
    }
}


