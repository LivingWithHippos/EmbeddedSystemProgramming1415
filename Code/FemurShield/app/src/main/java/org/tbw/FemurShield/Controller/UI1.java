package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.OldSession;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class UI1 extends Activity {

    protected Calendar calendar;
    private List<SessionsListItem> mItems;
    private SessionsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui1);
        //aggiorno la listView
        //AggiornaLista();
        inizializzaLista();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onRecClick(View view){
        //TODO: business logic
        Session sessione=Controller.CreateSession();


        new Thread() {
            public void run(){
                Intent i = new Intent(getBaseContext(),FallDetector.class);
                startService(i);
            }
        }.start();

        onPlayClick(view);

        //aggiorno la listView
        //AggiornaLista();
        inizializzaLista();
    }

    public void onPauseClick(View view){
        //TODO: business logic
        Controller.PauseSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.VISIBLE);

        mItems.get(0).setState(SessionsListItem.INACTIVE_STATE);
        mAdapter.notifyDataSetChanged();

        Intent i=new Intent(getApplicationContext(), FallDetector.class);
        stopService(i);
    }

    public void onPlayClick(View view){
        //TODO: business logic
        Controller.StartSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);


        if(mItems.size()>1)
            mItems.get(0).setState(SessionsListItem.ACTIVE_STATE);
        mAdapter.notifyDataSetChanged();

        Intent i = new Intent(getBaseContext(),FallDetector.class);
        startService(i);
    }

    public void onStopClick(View view){
        //TODO: business logic
        Controller.StopSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

        Intent i=new Intent(getBaseContext(), FallDetector.class);
        stopService(i);

        //aggiorno la listView
        //AggiornaLista();
        inizializzaLista();
    }

    public void inizializzaLista()
    {
        //costruisco la listview
        mItems=new ArrayList<>();
        calendar= Calendar.getInstance();

        //lista delle sessioni che la listview visualizzerà
        //sono già una copia non serve ricopiarle, vedi il metodo
        ArrayList<OldSession> sessionsList=SessionManager.getInstance().getOldSessions();
        ActiveSession activeSession=SessionManager.getInstance().getActiveSession();
        //metto a sessione attiva in cima alla lista
        if(activeSession!=null)
        {
            try {
                //inizializzo il calendario con il timestamp della sessione ottenuto tramite getDataTime()
                calendar.setTime(new SimpleDateFormat(Session.datePattern).parse(activeSession.getDataTime()));
                // creo l'oggetto che andra' nella lista (vedi costruttore per info)
                mItems.add(new SessionsListItem(activeSession.getSignature().toBitmap(),
                        activeSession.getName(),
                        calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND),
                        activeSession.getFallsNumber() + "",
                        SessionsListItem.ACTIVE_STATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        //aggiungo le altre sessioni, quelle vecchie
        for(Session session:sessionsList) {

            try {
                calendar.setTime(new SimpleDateFormat(Session.datePattern).parse(session.getDataTime()));
                mItems.add(new SessionsListItem(session.getSignature().toBitmap(),
                        session.getName(),
                        calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND),
                        session.getFallsNumber()+"",
                        SessionsListItem.INACTIVE_STATE));

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //utilizzo dell'adapter
        mAdapter=new SessionsListAdapter(this,mItems);
        ((ListView)findViewById(R.id.listsessionui1)).setAdapter(mAdapter);

    }
    /*
    public void AggiornaLista(){
        //costruisco la listview

        //lista delle sessioni che la listview visualizzerà
        ArrayList<Session> sessionsList=new ArrayList<>();

        //aggiungo le sessioni vecchie
        ArrayList<OldSession> old=SessionManager.getInstance().getOldSessions();
        for(int i=0;i<old.size();i++){
            sessionsList.add(old.get(i));
        }

        //aggiungo la sessone attiva
        ActiveSession a =SessionManager.getInstance().getActiveSession();
        if(a!=null)
            sessionsList.add(a);

        //Questa è la lista che rappresenta la sorgente dei dati della listview
        //ogni elemento è una mappa(chiave->valore)

        ArrayList<HashMap<String, Object>> data=new ArrayList<>();


        for(int i=sessionsList.size()-1;i>=0;i--){
            Session s=sessionsList.get(i);// per ogni sessione all'inteno della lista

            HashMap<String,Object> sessionMap=new HashMap<>();//creiamo una mappa di valori


            sessionMap.put("signature", s.getSignature().toBitmap());
            sessionMap.put("name", s.getName());
            sessionMap.put("data", s.getDataTime());
            sessionMap.put("falls", "" + s.getFalls().size());

            if(s instanceof ActiveSession){
                sessionMap.put("duration", "");
                sessionMap.put("state", R.drawable.state);
            }
            else if(s instanceof OldSession){
                sessionMap.put("duration", ((OldSession) s).getDuration());
                sessionMap.put("state", "");
            }

            data.add(sessionMap);  //aggiungiamo la mappa di valori alla sorgente dati
        }


        String[] from={"signature","name","data","falls","duration","state"}; //dai valori contenuti in queste chiavi
        int[] to={R.id.sessionsignatureui1,R.id.sessionnameui1,R.id.sessiondateui1,R.id.sessionfallsui1,R.id.sessiondurationui1,R.id.sessionstateimgui1};//agli id delle view

        //costruzione dell adapter
        SimpleAdapter adapter=new SimpleAdapter(
                getApplicationContext(),
                data,//sorgente dati
                R.layout.sessionlistitemui1, //layout contenente gli id di "to"
                from,
                to);


        adapter.setViewBinder(new SimpleAdapter.ViewBinder(){

            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if( (view instanceof ImageView) & (data instanceof Bitmap) ) {
                    ImageView iv = (ImageView) view;
                    Bitmap bm = (Bitmap) data;
                    iv.setImageBitmap(bm);
                    return true;
                }
                return false;

            }

        });

        //utilizzo dell'adapter
        ((ListView)findViewById(R.id.listsessionui1)).setAdapter(adapter);
    }*/

    public void openSettings()
    {
        //lancio senza opzioni perchè la ui impostazioni non ne richiede
        Intent intent=new Intent(this,UI5.class);
        startActivity(intent);
    }
}
