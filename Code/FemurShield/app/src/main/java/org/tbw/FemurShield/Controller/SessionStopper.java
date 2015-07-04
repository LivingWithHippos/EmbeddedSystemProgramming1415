package org.tbw.FemurShield.Controller;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

/**
 * Created by Moro on 04/07/15.
 */
public class SessionStopper extends IntentService {
    StopperTimer stopper;
    @Override
    protected void onHandleIntent(Intent intent) {}

    public SessionStopper(){
        super("SessionStopper");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //ottengo le ore della durata massima salvata nelle impostazioni
        PreferencesEditor pref=new PreferencesEditor(getBaseContext());
        int scadenza=pref.getSessionDuration();
        //avvio lo stopper che permette lo stop della sessione
        startStopper(scadenza);
        return Service.START_STICKY;
    }



    @Override
    public void onDestroy() {
        stopper.interrupt();
    }

    public void startStopper(int hours){
        //avvio il thread demone che controlla il non superamento della scadenza
        stopper=new StopperTimer(hours);
        stopper.setDaemon(true);
        //è difficile che venga chiuso da android perchè è associato ad un Intent service
        stopper.start();
    }


    private class StopperTimer extends Thread{
        private int hours=0;

        public StopperTimer(int hours){
            this.hours=hours;
        }

        @Override
        public void run(){
            //se non è stato fermato e la durata della sessione è ancora valida ricontrollerò tra un minuto
            while((!interrupted())&&Controller.getInstance().getDurate()<3600000*hours) {//se scade è vinene interrotto esce
                try {
                    Thread.sleep(60000);//ricontrollo tra un minuto
                } catch (InterruptedException e) {}
            }
            //se è uscito dal ciclo perchè la sessione è scaduta la interrompo
            if (Controller.getInstance().getDurate()<3600000*hours)
                Controller.getInstance().InterruptSession();
            //altrimenti (terminazione volontaria da parte dell'utente) ci ha già il controller a stopparla
        }
    }

}
