package org.tbw.FemurShield;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.OldSession;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.View.ActivityObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UI1 extends ActivityObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui1);

    }

/* TODO: MENU SETTINGS (sotto qui il codice...da modificare)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ui1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    public void onRecClick(View view){
        //TODO: business logic
        Session sessione=controller.CreateSession();
        controller.StartSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

        //aggiungo l'elemento della sessione attiva alla lista













//lista delle sessioni che la listview visualizzerà
        ArrayList<Session> sessionsList=new ArrayList<>();

        sessionsList.add(SessionManager.getInstance().getActiveSession());

        //TODO da aggiungere anche quelle old

        //Questa è la lista che rappresenta la sorgente dei dati della listview
        //ogni elemento è una mappa(chiave->valore)

        ArrayList<HashMap<String, Object>> data=new ArrayList<>();


        for(int i=0;i<sessionsList.size();i++){
            Session s=sessionsList.get(i);// per ogni sessione all'inteno della lista

            HashMap<String,Object> sessionMap=new HashMap<>();//creiamo una mappa di valori


            sessionMap.put("signature", s.getSignature());
            sessionMap.put("name", s.getName());
            sessionMap.put("data", s.getDataTime());
            sessionMap.put("falls", "" + s.getFalls().size());

            if(s instanceof ActiveSession){
                sessionMap.put("duration", "");
                sessionMap.put("state", R.mipmap.state);
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

        //utilizzo dell'adapter
        ((ListView)findViewById(R.id.listsessionui1)).setAdapter(adapter);

















    }

    public void onPauseClick(View view){
        //TODO: business logic
        //controller.PauseSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.VISIBLE);

    }

    public void onPlayClick(View view){
        //TODO: business logic
        //controller.StartSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

    }

    public void onStopClick(View view){
        //TODO: business logic
        //controller.StopSession();

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);
    }
}
