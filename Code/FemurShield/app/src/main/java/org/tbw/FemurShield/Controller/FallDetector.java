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

import java.util.Observable;

/**
 * Created by Moro on 02/05/15.
 */
public class FallDetector extends IntentService implements SensorEventListener {

    private SensorManager sensorManagerGiro;
    private Sensor accelerometro;
    public static String DETECTOR_START = "startfalldetector";
    private boolean isRunning = false;

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
        Log.println(Log.INFO, "Service onCreate","fefwef");
        sensorManagerGiro = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometro = sensorManagerGiro.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManagerGiro.registerListener(this, accelerometro, SensorManager.SENSOR_DELAY_UI);
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
                    .setContentText("it's running...").setSmallIcon(R.drawable.notification)
                    .build();
            //setto L'ID per la notifica
            final int notificationID = 1234567;
            startForeground(notificationID, notification);
        }
    }

    private void stopDetector()
    {
        if (isRunning)
        {
            isRunning = false;
            stopForeground(true);
        }
    }
}
