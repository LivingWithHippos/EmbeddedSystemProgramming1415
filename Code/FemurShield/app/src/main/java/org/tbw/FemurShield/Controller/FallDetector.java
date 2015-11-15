package org.tbw.FemurShield.Controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.R;

import java.util.ArrayList;

/**
 * Created by Moro on 02/05/15.
 */
public class FallDetector extends IntentService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometro;
    private Sensor giroscopio;
    private boolean isRunning = false;

    //costanti per l'algoritmo
    private final int DURATA_IMPATTO_ACC = 250; //identifica la durata in ms di un impatto con altri corpi (accelerometro)
    private final int DISTANZA_CADUTA_GYRO_IMPATTO_CADUTA_ACC = 30 + DURATA_IMPATTO_ACC;//identifica la distanza in ms tra la fine della caduta e la fine dell'impatto con il terreno
    private final int DURATA_CADUTA_GYRO = 300; //identifica la durata in ms della caduta (giroscopio)
    private final int LIMITE_MINIMO_IMPATTO_ACC = 28; //identifica il valore minimo che idedntifica un impatto (accelerometro)
    private final int LIMITE_MINIMO_CADUTA_GYRO = 7; //identifica il valore minimo che idedntifica una caduta (giroscopio)

    //tempo di campionamento dati
    private int tempocampionamento = 20;

    //durata che identifica per quanto tempo mi interessano i valori in ms
    private int tempovalori = 500;


    //costanti per la posizione dell'array dei dati
    public static final int X_VALUE = 0;//rappresenta la posizione dei valori delle x nell'array dei valori di una caduta
    public static final int Y_VALUE = 1;//rappresenta la posizione dei valori delle y nell'array dei valori di una caduta
    public static final int Z_VALUE = 2;//rappresenta la posizione dei valori delle z nell'array dei valori di una caduta

    /* costanti che identificano lo stato di un rilevamento di caduta o impatto
    "IN_PROGRESS" per dire che sta cercando di capire se è un impatto o una caduta.
    "IDENTIFIED" per dire che è stato identificato un impatto o una caduta.
    "NONE" per dire che non sono stati rilevati impatti o cadute e nemmeno ne stanno avvenendo
     */
    private static final int NONE = 0;
    private static final int IN_PROGRESS = 1;
    private static final int IDENTIFIED = 2;

    //variabili per lo stato attuale di caduta o impatto
    private int fallstate = NONE;
    private int impactstate = NONE;

    //elenco buffer di dati dell'accelerometro
    private ArrayList<ArrayList<Float>> bufferValueBeforeAcc = new ArrayList<>(3);
    private ArrayList<ArrayList<Float>> bufferValueFallAcc = new ArrayList<>(3);
    private ArrayList<ArrayList<Float>> bufferValueAfterAcc = new ArrayList<>(3);

    //elenco buffer di dati del giroscopio
    private ArrayList<ArrayList<Float>> bufferValueBeforeGyro = new ArrayList<>(3);
    private ArrayList<ArrayList<Float>> bufferValueFallGyro = new ArrayList<>(3);
    private ArrayList<ArrayList<Float>> bufferValueAfterGyro = new ArrayList<>(3);

    {//blocco di inizializzazione del buffer
        //buffer per l'accelerometro
        bufferValueBeforeAcc.add(X_VALUE, new ArrayList<Float>());
        bufferValueBeforeAcc.add(Y_VALUE, new ArrayList<Float>());
        bufferValueBeforeAcc.add(Z_VALUE, new ArrayList<Float>());

        bufferValueFallAcc.add(X_VALUE, new ArrayList<Float>());
        bufferValueFallAcc.add(Y_VALUE, new ArrayList<Float>());
        bufferValueFallAcc.add(Z_VALUE, new ArrayList<Float>());

        bufferValueAfterAcc.add(X_VALUE, new ArrayList<Float>());
        bufferValueAfterAcc.add(Y_VALUE, new ArrayList<Float>());
        bufferValueAfterAcc.add(Z_VALUE, new ArrayList<Float>());

        //buffer per il giroscopio
        bufferValueBeforeGyro.add(X_VALUE, new ArrayList<Float>());
        bufferValueBeforeGyro.add(Y_VALUE, new ArrayList<Float>());
        bufferValueBeforeGyro.add(Z_VALUE, new ArrayList<Float>());

        bufferValueFallGyro.add(X_VALUE, new ArrayList<Float>());
        bufferValueFallGyro.add(Y_VALUE, new ArrayList<Float>());
        bufferValueFallGyro.add(Z_VALUE, new ArrayList<Float>());

        bufferValueAfterGyro.add(X_VALUE, new ArrayList<Float>());
        bufferValueAfterGyro.add(Y_VALUE, new ArrayList<Float>());
        bufferValueAfterGyro.add(Z_VALUE, new ArrayList<Float>());
    }

    public FallDetector() {
        super("FallDetector");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //avvia il detector
        startDetector();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        //stoppa il detector
        stopDetector();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //se è un sato dell'accelerometro notifico gli osservatori (la ui3 in particolare per la rappresentazione in RT del grafico)
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            Controller.getNotification().NotifyAccData(event.values[0], event.values[1], event.values[2]);
        fallAlgorithm(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void startDetector() {
        if (!isRunning) {
            //ottengo il tempo di campionamento salvato nelle impostazioni
            PreferencesEditor pref = new PreferencesEditor(getBaseContext());
            tempocampionamento = 205 - (200 * pref.getSamplingRate() / 100);
            isRunning = true;
            //costruisco la notifica da visualizzare
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Fall detector:")
                    .setContentText("it's running...")
                    .setSmallIcon(R.drawable.notification)
                    .build();
            //setto L'ID per la notifica
            final int notificationID = 1234567;
            startForeground(notificationID, notification);

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometro, tempocampionamento);
            giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, giroscopio, tempocampionamento);
        }
    }

    private void stopDetector() {
        if (isRunning) {
            isRunning = false;
            //fermo il servizio
            stopForeground(true);
            //fermo il listener
            sensorManager.unregisterListener(this, accelerometro);
            sensorManager.unregisterListener(this, giroscopio);
        }
    }

    boolean fall = false;
    int cycleAfterFall = 0;

    public void fallAlgorithm(SensorEvent event) {
        /*
        L'algoritmo consiste nel salvare i dati provenienti da giroscopio e accelerometro in un buffer e localizzare
        quando è avvenuta una caduta.

        la caduta si può individuare nel seguente modo:
        Quando l'algoritmo rileva un impatto (alti valori dell'accelerometro) controlla se precedentemente
        la persona stava cadendo (cambiamento di almeno uno dei valori del giroscopio rispetto a quelli
        abituali nel passato più vicino). Se non risultano cadute (inclinazioni) si presuppone che la caduta
        non stia avvenendo o verrà localizzata al secondo impatto se l'impatto rilevato era un urto pre caduta.
         */

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //se i dati dell'evento riguardano il giroscopio allora cerco di localizzare l'inclinazione dell'utente
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            falldetect(x, y, z);
        else //se i dati dell'evento riguardano l'accelerometro allora cerco di localizzare l'impatto
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                impactdetect(x, y, z);

        if (fall) { //attendo che i valori di rumore della caduta si stabilizzino (qualche millisecondo)
            cycleAfterFall++;
            if (cycleAfterFall > 50000 / tempocampionamento) {
                fall = false;
                cycleAfterFall = 0;
            }
        } else {
            //se ho identificato sia un impatto che una caduta ho localizzato una Fall
            if (fallstate == IDENTIFIED && impactstate == IDENTIFIED) {
                fall = true;
                //trasformo i dati del buffer nel formato richiesto ovvero float[3][]
                //valori prima della caduta
                float[][] beforevalue = new float[3][];
                for (int i = 0; i < 3; i++) {
                    beforevalue[i] = new float[bufferValueBeforeAcc.get(i).size()];
                    for (int j = 0; j < bufferValueBeforeAcc.get(i).size(); j++) {
                        beforevalue[i][j] = bufferValueBeforeAcc.get(i).get(j);
                    }
                }
                //valori della caduta
                float[][] fallvalue = new float[3][];
                for (int i = 0; i < 3; i++) {
                    fallvalue[i] = new float[bufferValueFallGyro.get(i).size()];
                    for (int j = 0; j < bufferValueFallGyro.get(i).size(); j++) {
                        fallvalue[i][j] = bufferValueFallGyro.get(i).get(j);
                    }
                }

                //valori dopo la caduta
                float[][] aftervalue = new float[3][];
                for (int i = 0; i < 3; i++) {
                    aftervalue[i] = new float[bufferValueAfterAcc.get(i).size()];
                    for (int j = 0; j < bufferValueAfterAcc.get(i).size(); j++) {
                        aftervalue[i][j] = bufferValueAfterAcc.get(i).get(j);
                    }
                }

                // creato da Vianello
                //creo la caduta, notifico gli osservatori e invio la mail
                final double[] position = new double[2];
                final Fall f = new Fall(beforevalue, fallvalue, aftervalue);
                LocationLocator.LocationResult locationResult = new LocationLocator.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        position[0] = location.getLatitude(); // leggo la latuditine e la metto in position
                        position[1] = location.getLongitude(); // idem con la longitudine
                        //Log.d("Lat", "" + position[0]);
                        //Log.d("Long", "" + position[1]);
                        f.setPosition(position[0], position[1]);
                        Controller.getInstance().sendEmail(f);//attendo prima che sia stata localizzata la caduta
                    }
                };
                LocationLocator myLocation = new LocationLocator();
                myLocation.getLocation(getApplicationContext(), locationResult);
                Controller.getNotification().NotifyFall(f);


                //svuoto i buffer e i campi...e metto i valori di after in before
                fallstate = NONE;
                impactstate = NONE;
                bufferValueBeforeAcc.clear();
                bufferValueFallAcc.clear();
                bufferValueBeforeGyro.clear();
                bufferValueFallGyro.clear();
                bufferValueBeforeAcc = new ArrayList<>(3);
                bufferValueBeforeAcc.add(X_VALUE, bufferValueAfterAcc.get(X_VALUE));
                bufferValueBeforeAcc.add(Y_VALUE, bufferValueAfterAcc.get(Y_VALUE));
                bufferValueBeforeAcc.add(Z_VALUE, bufferValueAfterAcc.get(Z_VALUE));
                bufferValueAfterAcc.clear();
                bufferValueBeforeGyro = new ArrayList<>(3);
                bufferValueBeforeGyro.add(X_VALUE, bufferValueAfterGyro.get(X_VALUE));
                bufferValueBeforeGyro.add(Y_VALUE, bufferValueAfterGyro.get(Y_VALUE));
                bufferValueBeforeGyro.add(Z_VALUE, bufferValueAfterGyro.get(Z_VALUE));
                bufferValueAfterGyro.clear();

                //ri-inizializzo i buffer
                bufferValueFallAcc = new ArrayList<>(3);
                bufferValueAfterAcc = new ArrayList<>(3);

                bufferValueFallGyro = new ArrayList<>(3);
                bufferValueAfterGyro = new ArrayList<>(3);

                //buffer per l'accelerometro
                bufferValueFallAcc.add(X_VALUE, new ArrayList<Float>());
                bufferValueFallAcc.add(Y_VALUE, new ArrayList<Float>());
                bufferValueFallAcc.add(Z_VALUE, new ArrayList<Float>());

                bufferValueAfterAcc.add(X_VALUE, new ArrayList<Float>());
                bufferValueAfterAcc.add(Y_VALUE, new ArrayList<Float>());
                bufferValueAfterAcc.add(Z_VALUE, new ArrayList<Float>());

                //buffer per il giroscopio
                bufferValueFallGyro.add(X_VALUE, new ArrayList<Float>());
                bufferValueFallGyro.add(Y_VALUE, new ArrayList<Float>());
                bufferValueFallGyro.add(Z_VALUE, new ArrayList<Float>());

                bufferValueAfterGyro.add(X_VALUE, new ArrayList<Float>());
                bufferValueAfterGyro.add(Y_VALUE, new ArrayList<Float>());
                bufferValueAfterGyro.add(Z_VALUE, new ArrayList<Float>());
            }
        }
    }

    int countlifegyro = 0;

    public void falldetect(float x, float y, float z) {
        try {
            if (fallstate == NONE) {//se non ho ancora rilevato possibili cadute, analizzo i dati per capire se potrebbe esserci un inizio.
                /*
                controllo se ho la risultante delle accellerazioni sia un valore di una potenziale caduta.
                in quel caso mi metto in osservazione/studio dei prossimi valori impostando fallstate=IN_PROGRESS;
                 */
                if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) > LIMITE_MINIMO_CADUTA_GYRO) {
                    fallstate = IN_PROGRESS;

                    Log.println(Log.INFO, "caduta in progress", "");
                    //aggiungo il valore nell'array con i valori di caduta (nell'array dei dati precedenti alla caduta)
                    bufferValueFallGyro.get(X_VALUE).add(x);
                    bufferValueFallGyro.get(Y_VALUE).add(y);
                    bufferValueFallGyro.get(Z_VALUE).add(z);
                } else {
                    /*controllo che il buffer non inizi ad avere valori che non ci interessano più
                    (voglio mostrare valori solo per tempovalori millisecondi)...
                    In quel caso rimuovo il primo elemento (piu vecchio)
                     */
                    if (bufferValueBeforeGyro.get(X_VALUE).size() * tempocampionamento > tempovalori) {
                        bufferValueBeforeGyro.get(X_VALUE).remove(0);
                        bufferValueBeforeGyro.get(Y_VALUE).remove(0);
                        bufferValueBeforeGyro.get(Z_VALUE).remove(0);
                    }
                    //aggiungo i valori letti al buffer (con i valori prima della caduta) perchè mi potrebbero servire in futuro
                    bufferValueBeforeGyro.get(X_VALUE).add(x);
                    bufferValueBeforeGyro.get(Y_VALUE).add(y);
                    bufferValueBeforeGyro.get(Z_VALUE).add(z);
                }
            } else {
                if (fallstate == IN_PROGRESS) {
                    /* controllo il numero di valori alti per dire se è effettivamente un urto se al
                    raggiungimento del tempo standard di impatto ho raggiunto un tot di valori sufficienti
                    a quel punto passo ad IDENTIFIED atrimenti a NONE ed i valori nel buffer di caduta
                    vengono spostati sul buffer di beforefall.*/
                    if (bufferValueFallGyro.get(X_VALUE).size() * tempocampionamento > DURATA_CADUTA_GYRO) {
                        fallstate = IDENTIFIED;

                        Log.println(Log.INFO, "caduta identified", "");
                    }
                    //aggiungo il valore nell'array con i valori di caduta
                    bufferValueFallGyro.get(X_VALUE).add(x);
                    bufferValueFallGyro.get(Y_VALUE).add(y);
                    bufferValueFallGyro.get(Z_VALUE).add(z);
                } else { //fallstate==IDENTIFIED
                    countlifegyro++;
                    //attendo il tempo massimo in attesa dell'impatto...
                    //se potrebbe ancora verificarsi l'impatto mantengo su IDENTIFIED la variabile di
                    //stato, altrimenti la imposto nuovamente a NONE e i valori del buffer dopo la
                    // caduta li sposto su quello dei valori prima della caduta (eliminando quelli troppo vecchi)
                    if (countlifegyro * tempocampionamento > DISTANZA_CADUTA_GYRO_IMPATTO_CADUTA_ACC) {
                        fallstate = NONE;
                        countlifegyro = 0;
                        Log.println(Log.INFO, "caduta terminata", "");

                        for (int i = 0; i < bufferValueAfterGyro.get(X_VALUE).size(); i++) {
                            bufferValueBeforeGyro.get(X_VALUE).add(bufferValueAfterGyro.get(X_VALUE).get(i));
                            bufferValueBeforeGyro.get(Y_VALUE).add(bufferValueAfterGyro.get(Y_VALUE).get(i));
                            bufferValueBeforeGyro.get(Z_VALUE).add(bufferValueAfterGyro.get(Z_VALUE).get(i));
                            if (bufferValueBeforeGyro.get(X_VALUE).size() * tempocampionamento > tempovalori) {
                                bufferValueBeforeGyro.get(X_VALUE).remove(0);
                                bufferValueBeforeGyro.get(Y_VALUE).remove(0);
                                bufferValueBeforeGyro.get(Z_VALUE).remove(0);
                            }
                        }
                    } else {
                        bufferValueAfterGyro.get(X_VALUE).add(x);
                        bufferValueAfterGyro.get(Y_VALUE).add(y);
                        bufferValueAfterGyro.get(Z_VALUE).add(z);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    int countlifeacc = 0;

    public void impactdetect(float x, float y, float z) {
        try {
            if (impactstate == NONE) {
                /*
                controllo se ho la risultante delle accellerazioni sia un valore di una potenziale caduta.
                in quel caso mi metto in osservazione/studio dei prossimi valori impostando fallstate=IN_PROGRESS;
                 */
                if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) > LIMITE_MINIMO_IMPATTO_ACC) {
                    impactstate = IN_PROGRESS;

                    Log.println(Log.INFO, "impatto in progress", "");
                    //aggiungo il valore nell'array con i valori di caduta
                    bufferValueFallAcc.get(X_VALUE).add(x);
                    bufferValueFallAcc.get(Y_VALUE).add(y);
                    bufferValueFallAcc.get(Z_VALUE).add(z);
                } else {
                    /*controllo che il buffer non inizi ad avere valori che non ci interessano più
                    (voglio mostrare valori solo per tempovalori millisecondi)...
                    In quel caso rimuovo il primo elemento (piu vecchio)
                     */
                    if (bufferValueBeforeAcc.get(X_VALUE).size() * tempocampionamento > tempovalori) {
                        bufferValueBeforeAcc.get(X_VALUE).remove(0);
                        bufferValueBeforeAcc.get(Y_VALUE).remove(0);
                        bufferValueBeforeAcc.get(Z_VALUE).remove(0);
                    }
                    //aggiungo i valori letti al buffer (con i valori prima della caduta) perchè mi potrebbero servire in futuro
                    bufferValueBeforeAcc.get(X_VALUE).add(x);
                    bufferValueBeforeAcc.get(Y_VALUE).add(y);
                    bufferValueBeforeAcc.get(Z_VALUE).add(z);
                }
            } else {
                if (impactstate == IN_PROGRESS) {
                    /* controllo il numero di valori alti per dire se è effettivamente un urto se al
                    raggiungimento del tempo statndard di impatto ho raggiunto un tot di valori sufficienti
                    a quel punto passo ad IDENTIFIED atrimenti a NONE ed i valori nel buffer di caduta
                    vengono spostati sul buffer di beforefall.*/
                    if (bufferValueFallAcc.get(X_VALUE).size() * tempocampionamento > DURATA_IMPATTO_ACC) {
                        impactstate = IDENTIFIED;
                        Log.println(Log.INFO, "impatto identified", "");
                    }
                    //aggiungo il valore nell'array con i valori di caduta
                    bufferValueFallAcc.get(X_VALUE).add(x);
                    bufferValueFallAcc.get(Y_VALUE).add(y);
                    bufferValueFallAcc.get(Z_VALUE).add(z);
                } else { //impactstate==IDENTIFIED
                    countlifeacc++;
                    //attendo il tempo massimo in attesa dell'impatto...
                    //se potrebbe ancora verificarsi l'impatto mantengo su IDENTIFIED la variabile di
                    //stato, altrimenti la imposto nuovamente a NONE e i valori del buffer dopo la
                    // caduta li sposto su quello dei valori prima della caduta (eliminando quelli troppo vecchi)
                    if (countlifeacc * tempocampionamento > DISTANZA_CADUTA_GYRO_IMPATTO_CADUTA_ACC) {
                        impactstate = NONE;
                        countlifeacc = 0;
                        Log.println(Log.INFO, "impatto terminata", "");
                        for (int i = 0; i < bufferValueAfterGyro.get(X_VALUE).size(); i++) {
                            bufferValueBeforeAcc.get(X_VALUE).add(bufferValueAfterAcc.get(X_VALUE).get(i));
                            bufferValueBeforeAcc.get(Y_VALUE).add(bufferValueAfterAcc.get(Y_VALUE).get(i));
                            bufferValueBeforeAcc.get(Z_VALUE).add(bufferValueAfterAcc.get(Z_VALUE).get(i));
                            if (bufferValueBeforeAcc.get(X_VALUE).size() * tempocampionamento > tempovalori) {
                                bufferValueBeforeAcc.get(X_VALUE).remove(0);
                                bufferValueBeforeAcc.get(Y_VALUE).remove(0);
                                bufferValueBeforeAcc.get(Z_VALUE).remove(0);
                            }
                        }
                    }
                    bufferValueAfterAcc.get(X_VALUE).add(x);
                    bufferValueAfterAcc.get(Y_VALUE).add(y);
                    bufferValueAfterAcc.get(Z_VALUE).add(z);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
