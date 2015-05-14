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
import android.os.IBinder;
import android.util.Log;

import org.tbw.FemurShield.Model.Fall;
import org.tbw.FemurShield.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Created by Moro on 02/05/15.
 */
public class FallDetector extends IntentService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometro;
    public static String DETECTOR_START = "startfalldetector";
    private boolean isRunning = false;
    private final String X_VALUE="X";
    private final String Y_VALUE="Y";
    private final String Z_VALUE="Z";
    private Map<String,ArrayList<Float>> buffer =new HashMap<>(3);

    {//blocco di inizializzazione del buffer
        buffer.put(X_VALUE,new ArrayList<Float>());
        buffer.put(Y_VALUE,new ArrayList<Float>());
        buffer.put(Z_VALUE,new ArrayList<Float>());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FallDetector(String name) {
        super(name);
    }

    public FallDetector() {
        super("FallDetector");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.println(Log.INFO, "onHandleIntent", "fefwef");

        /*float[][] beforefall=null;//TODO assign
        float[][] falldata=null;//TODO assign
        float[][] afterfall=null;//TODO assign
        //notifico il controller che è stata rilevata una caduta e la gestirà.
        Controller.handleFallEvent(new Fall(beforefall,falldata,afterfall));
*/
    }

    @Override
    public void onCreate() {
        Log.println(Log.INFO, "Service onCreate", "fefwef");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.println(Log.INFO, "Start FallDetector", "Helloworld...");
        startDetector();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onDestroy() {
        Log.println(Log.INFO, "Stop FallDetector","bye...");
        stopDetector();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            buffer.get(X_VALUE).add(new Float(event.values[0]));
            buffer.get(Y_VALUE).add(new Float(event.values[1]));
            buffer.get(Z_VALUE).add(new Float(event.values[2]));

            if(checkFall(event.values[0],event.values[1],event.values[2])) {
                Controller.handleFallEvent(new Fall(null, null, null, getBaseContext()));
                Log.println(Log.INFO, "FALL DETECTED", "Aiaaaaa");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void startDetector(){
        if(!isRunning) {
            isRunning=true;
            //costruisco la notifica da visualizzare
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Fall detector:")
                    .setContentText("it's running...")
                    .setSmallIcon(R.drawable.notification)
                    .build();
            //setto L'ID per la notifica
            final int notificationID = 1234567;
            startForeground(notificationID, notification);
            //TODO assegnazione at the first call only
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void stopDetector()
    {
        if (isRunning)
        {
            isRunning = false;
            //fermo il servizio
            stopForeground(true);
            //fermo il listener
            sensorManager.unregisterListener(this, accelerometro);
        }
    }

    public boolean checkFall(float x,float y, float z){
        double result = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
        if(result>27.00){
            Log.println(Log.INFO,"listener event","" + result);
            return true;
        }
        return false;
    }
}
