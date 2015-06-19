package org.tbw.FemurShield.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.tbw.FemurShield.Model.ActiveSession;
import org.tbw.FemurShield.Model.OldSession;
import org.tbw.FemurShield.Model.Session;
import org.tbw.FemurShield.Model.SessionManager;
import org.tbw.FemurShield.R;

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
        AggiornaLista();
        instantiateColors();
        ListView list=((ListView)findViewById(R.id.listsessionui1));
        //final Activity activity=this;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0 && SessionManager.getInstance().getActiveSession()!=null) {
                    Intent i = new Intent(getBaseContext(), UI3.class);
                    startActivity(i);
                    return;
                }
                HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
                String datetime = item.get("data");
                Intent i = new Intent(getBaseContext(), UI2.class);
                i.putExtra(UI2.SESSION_DATA_STAMP, datetime);
                startActivity(i);
            }

        });
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
        Controller.getInstance().CreateSession();

        onPlayClick(view);

        //aggiorno la listView
        AggiornaLista();
        Intent intent=new Intent(this,UI3.class);
        startActivity(intent);
    }

    public void onPauseClick(View view){
        //TODO: business logic
        Controller.getInstance().PauseSession(this);

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.VISIBLE);

    }

    public void onPlayClick(View view){
        //TODO: business logic
        Controller.getInstance().StartSession(this);

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

    }

    public void onStopClick(View view){
        //TODO: business logic
        Controller.getInstance().StopSession(this);

        //modifo le visibilità dei bottoni di controllo
        ((ImageView)findViewById(R.id.recbtnun1)).setVisibility(ImageView.VISIBLE);
        ((ImageView)findViewById(R.id.pausebtnui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.stopbntui1)).setVisibility(ImageView.INVISIBLE);
        ((ImageView)findViewById(R.id.startbtnui1)).setVisibility(ImageView.INVISIBLE);

        //aggiorno la listView
        AggiornaLista();
    }

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
    }

    public void openSettings()
    {
        //lancio senza opzioni perchè la ui impostazioni non ne richiede
        Intent intent=new Intent(this,UI5.class);
        startActivity(intent);
    }

    private void instantiateColors()
    {

        ColorsPicker colorsPicker=ColorsPicker.getInstance(new String[][]{
                getResources().getStringArray(R.array.random_palette_1),
                getResources().getStringArray(R.array.random_palette_2),
                getResources().getStringArray(R.array.random_palette_3),
                getResources().getStringArray(R.array.random_palette_4),
                getResources().getStringArray(R.array.random_palette_5),
                getResources().getStringArray(R.array.random_palette_6),
                getResources().getStringArray(R.array.random_palette_7)
        });

    }
}
