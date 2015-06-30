package org.tbw.FemurShield.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        //imposta la visibilita' del tasto rec
        viewHolder.ivState.setImageResource(R.drawable.state);
        viewHolder.ivState.setVisibility(item.isRecording() ? View.VISIBLE : View.INVISIBLE);

        //viewHolder.ivSignature.setImageBitmap(item.signature);
        Bitmap temp;
        if((temp=BitmapCache.getInstance().getBitmapFromMemCache(item.getDataTime()))!=null)
            viewHolder.ivSignature.setImageBitmap(temp);
        else {
            SignatureLoaderTask slt = new SignatureLoaderTask(viewHolder.ivSignature, item.getDataTime());
            slt.execute(viewHolder.ivSignature.getId());
        }

        viewHolder.tvName.setText(item.name);
        viewHolder.tvDate.setText(item.date);
        viewHolder.tvStartingTime.setText(item.startingTime);
        viewHolder.tvDuration.setText(item.duration);
        viewHolder.tvFallsNumber.setText(item.falls + "");

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
    
    public String name;
    public String date;
    public String startingTime;
    public String duration;
    public int falls;
    public boolean state;
    private String datetime;

            public SessionsListItem(String name, String date,int fallsNumber,String duration,boolean state) {

                this.name = name;
                this.falls = fallsNumber;
                this.datetime=date;
                //TODO: ricevere durata
                this.duration=duration;
                this.state=state;

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
                    Date timestamp = sdf.parse(datetime);
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(timestamp);
                    //scrive mesi giorni e ore ad una cifra come 0x invece di x
                    DecimalFormat formatter = new DecimalFormat("00");
                    //il mese parte da zero non da uno
                    this.date=formatter.format(calendar.get(Calendar.DAY_OF_MONTH))+"/"+formatter.format(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
                    this.startingTime = formatter.format(calendar.get(Calendar.HOUR_OF_DAY))+":"+formatter.format(calendar.get(Calendar.MINUTE))+":"+formatter.format(calendar.get(Calendar.SECOND));
                }catch (ParseException pe){
                    this.date="0";this.startingTime="0";
                }
            }

    /**
     * aggiunge uno al numero di cadute da mostrare
     * */
    public void addFall()
    {
        this.falls++;
    }
    /**
     * @param falls il numero di cadute da impostare
     * */
    public void setFallsNumber(int falls)
    {
        this.falls=falls;
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

    public String getDataTime() {
        return datetime;
    }
}
