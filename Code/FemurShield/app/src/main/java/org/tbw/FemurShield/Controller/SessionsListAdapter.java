package org.tbw.FemurShield.Controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.R;

import java.util.List;

/**
 * Created by Marco on 18/05/2015.
 */
public class SessionsListAdapter extends ArrayAdapter{


    public SessionsListAdapter(Context context, List<SessionsListItem> items) {
        super(context, R.layout.settings_list_item, items);
    }

    /* uso il pattern view holder che mi permette di evitare
    di richiamare findViewById  nella classe utilizzatrice */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // carica il layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sessionlistitemui1, parent, false);

            // inizializza il view holder
            viewHolder = new ViewHolder();
            viewHolder.ivSignature = (ImageView) convertView.findViewById(R.id.sessionsignatureui1);
            viewHolder.ivState = (ImageView) convertView.findViewById(R.id.sessionstateimgui1);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.sessionnameui1);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.sessiondateui1);
            viewHolder.tvStartingTime = (TextView) convertView.findViewById(R.id.sessionstartingtimeui1);
            viewHolder.tvDuration = (TextView) convertView.findViewById(R.id.sessiondurationui1);
            viewHolder.tvFallsNumber = (TextView) convertView.findViewById(R.id.sessionfallsui1);
            convertView.setTag(viewHolder);
        } else {
            // ricila la view gia' pronta
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // aggiorna il viewholder
        SessionsListItem item =(SessionsListItem) getItem(position);

        viewHolder.ivState.setImageResource(R.drawable.state);
        //imposta la visibilita' del tasto rec
        viewHolder.ivState.setVisibility(item.isRecording()?View.VISIBLE:View.INVISIBLE);
        viewHolder.ivSignature.setImageBitmap(item.signature);
        viewHolder.tvName.setText(item.name);
        viewHolder.tvDate.setText(item.date);
        viewHolder.tvStartingTime.setText(item.startingTime);
        viewHolder.tvDuration.setText(item.duration);
        viewHolder.tvFallsNumber.setText(item.fallsNumber);

        return convertView;
    }

    /**
     * Implementa il pattern View-Holder
     *
     * vedi http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView ivSignature;
        ImageView ivState;
        TextView tvName;
        TextView tvDate;
        TextView tvStartingTime;
        TextView tvDuration;
        TextView tvFallsNumber;
    }
}

class SessionsListItem{

    public static final boolean RECORDING_STATE=true;
    public static final boolean INACTIVE_STATE=false;

    public final Bitmap signature;
    public String name;
    public final String date;
    public final String startingTime;
    public String duration;
    public String fallsNumber;
    private int falls;
    private boolean state;

            public SessionsListItem(Bitmap signature,String name, String date,String startingTime,String fallsNumber,boolean state) {
                this.signature = signature;
                this.name = name;
                this.date = date;
                this.startingTime = startingTime;
                this.fallsNumber=fallsNumber;
                try {
                    this.falls = Integer.parseInt(fallsNumber);
                }catch(NumberFormatException nfe){falls=0;}
                duration="in corso...";
                this.state=state;
            }

    /**
     * aggiunge uno al numero di cadute da mostrare
     * */
    public void addFall()
    {
        this.falls++;
        this.fallsNumber=falls+"";
    }
    /**
     * @param falls il numero di cadute da impostare
     * */
    public void setFallsNumber(int falls)
    {
        this.falls=falls;
        this.fallsNumber=falls+"";
    }
    /**
     * @param state indica se la sessione rappresentatae' attiva o no
     * */
    public void setState(boolean state)
    {
        this.state=state;
    }
    /**
     * @return  se la sessione rappresentata sta registrando o no
     * */
    public boolean isRecording() {
        return state;
    }
}
