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
 * Classe di supporto per la lista sessioni in {@link SessionsListFragment}, applica il pattern view-holder per evitare rallentamenti nello scorrimento.
 */
public class SessionsListAdapter extends ArrayAdapter {


    public SessionsListAdapter(Context context, List<SessionsListItem> items) {
        super(context, R.layout.settings_list_item, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // carica il layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sessionlistitemui1, parent, false);

            // inizializza il view holder
            viewHolder = new ViewHolder();
            viewHolder.ivSignature = (ImageView) convertView.findViewById(R.id.sessionsignatureui1);
            viewHolder.ivState = (ImageView) convertView.findViewById(R.id.sessionstateimgui1);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.sessionnameui1);
            viewHolder.tvStartingDate = (TextView) convertView.findViewById(R.id.sessionstartingdateui1);
            viewHolder.tvStartingTime = (TextView) convertView.findViewById(R.id.sessionstartingtimeui1);
            viewHolder.tvDuration = (TextView) convertView.findViewById(R.id.sessiondurationui1);
            viewHolder.tvFallsNumber = (TextView) convertView.findViewById(R.id.sessionfallsui1);
            convertView.setTag(viewHolder);
        } else {
            // ricila la view gia' pronta
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // aggiorna il viewholder
        SessionsListItem item = (SessionsListItem) getItem(position);
        //imposta la visibilita' del tasto rec
        viewHolder.ivState.setImageResource(R.drawable.state);
        viewHolder.ivState.setVisibility(item.isRecording() ? View.VISIBLE : View.INVISIBLE);

        //viewHolder.ivSignature.setImageBitmap(item.signature);
        Bitmap temp;
        if ((temp = BitmapCache.getInstance().getBitmapFromMemCache(item.getDataTime())) != null)
            viewHolder.ivSignature.setImageBitmap(temp);
        else {
            SignatureLoaderTask slt = new SignatureLoaderTask(viewHolder.ivSignature, item.getDataTime());
            slt.execute(viewHolder.ivSignature.getId());
        }

        viewHolder.tvName.setText(item.name);
        viewHolder.tvStartingDate.setText("Iniziata il " + item.startingDate + " ");
        viewHolder.tvStartingTime.setText("alle ore " + item.startingTime);
        if (item.duration.length() > 2)
            viewHolder.tvDuration.setText("Durata: " + item.duration);
        else
            viewHolder.tvDuration.setText(item.duration);
        String cadute = item.falls == 1 ? " caduta" : " cadute";
        viewHolder.tvFallsNumber.setText(item.falls + cadute);
        return convertView;
    }

    /**
     * Implementa il pattern View-Holder
     * <p/>
     * vedi http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView ivSignature;
        ImageView ivState;
        TextView tvName;
        TextView tvStartingDate;
        TextView tvStartingTime;
        TextView tvDuration;
        TextView tvFallsNumber;
    }
}

/**
 * Classe che rappresenta un oggetto della lista sessioni
 */
class SessionsListItem {

    public static final boolean RECORDING_STATE = true;
    public static final boolean INACTIVE_STATE = false;

    public String name;
    public String startingDate;
    public String startingTime;
    public String duration;
    public int falls;
    public boolean state;
    private String datetime;

    /**
     * Costruttore della classe
     * @param name nome della sessione
     * @param date timestamp della sessione
     * @param fallsNumber numero di cadute della sessione
     * @param duration durata della sessione
     * @param state stato della sessione (attiva  o no)
     */
    public SessionsListItem(String name, String date, int fallsNumber, String duration, boolean state) {

        this.name = name;
        this.falls = fallsNumber;
        this.datetime = date;
        this.duration = duration;
        this.state = state;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Session.datePattern);
            Date timestamp = sdf.parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);
            //scrive mesi giorni e ore ad una cifra come 0x invece di x
            DecimalFormat formatter = new DecimalFormat("00");
            //il mese parte da zero non da uno
            this.startingDate = formatter.format(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + formatter.format(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
            this.startingTime = formatter.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + formatter.format(calendar.get(Calendar.MINUTE)) + ":" + formatter.format(calendar.get(Calendar.SECOND));
        } catch (ParseException pe) {
            this.startingDate = "0";
            this.startingTime = "0";
        }
    }

    public boolean isRecording() {
        return state;
    }

    /**
     * Metodo per ottenere il timestamp
     * @return il timestamp della sessione
     */
    public String getDataTime() {
        return datetime;
    }
}
