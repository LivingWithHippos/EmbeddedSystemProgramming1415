package org.tbw.FemurShield.Controller;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.R;

import java.util.List;

/**
 * Created by Vianello on 21/05/15.
 */
public class FallListAdapter extends ArrayAdapter
{

        public FallListAdapter(Context context, List<FallListItem> items)
        {
            super(context, R.layout.fall_list_item, items);
        }

        // uso il pattern view holder che mi permette di evitare di richiamare spesso findViewById
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder;

            if(convertView == null) {
                // inflate the GridView item layout
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.fall_list_item, parent, false);

                // initialize the view holder
                viewHolder = new ViewHolder();
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvFallName);
                viewHolder.tvdate = (TextView) convertView.findViewById(R.id.tvFallDate);
                viewHolder.ivSent = (ImageView) convertView.findViewById(R.id.ivFallSent);
                convertView.setTag(viewHolder);
            } else {
                // recycle the already inflated view
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // update the item view
            FallListItem item =(FallListItem) getItem(position);
            viewHolder.tvName.setText(item.name);
            viewHolder.tvdate.setText(item.date);
            viewHolder.ivSent.setImageResource(R.drawable.uncheck);

            return convertView;
        }

        /**
         * Implementa il pattern View-Holder
         * poco utile visto che abbiamo poche voci ma in futuro non si sa mai
         *
         * vedi http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
         */
        private static class ViewHolder
        {
            TextView tvName;
            TextView tvdate;
            ImageView ivSent;
        }
}

    class FallListItem
    {
        public final String name;
        public final String date;
        public boolean sent;

        public FallListItem(String name, String date, boolean sent)
        {
            this.name = name;
            this.date = date;
            this.sent = sent;
        }

        public void setSent(boolean report)
        {
            sent = report;
        }
    }

