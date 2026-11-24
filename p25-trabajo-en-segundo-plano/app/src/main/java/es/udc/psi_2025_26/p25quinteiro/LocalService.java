package es.udc.psi_2025_26.p25quinteiro;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocalService extends Service {

    public static final String ACTION_LOCAL_SERVICE_FINISH = "es.udc.psi_2025_26.broadcast.LS_FINISH";


    CountThread thread; // Referencia al hilo del servizio
    // Necesario para interrumpirlo cuando el servizio muere y se pueda entonces auto-cancelar


    private static  final String TAG = "_TAG";

    //public LocalService() {}


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d (TAG, getString(R.string.local_service_running));
        Toast.makeText(getApplicationContext(), R.string.local_service_running, Toast.LENGTH_SHORT).show();
        int count;
        // Se saca del intent hasta cuando contar...
        // Si no se tiene la clave COUNT se pone como valor por defecto DEFAULT

        count = intent.getIntExtra(MainActivity.COUNT, MainActivity.DEFAULT_COUNT);

        // Desacoplado del servicio
        thread = new CountThread(count); // El servizio crea su hilo al crearse
        thread.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return  null; // Ya que es un servizio local
    }

    // El contador está anclado al servizio...
    class CountThread extends Thread {
        private final int count;

        CountThread(int count) {
            this.count = count;
        }

        public void run() {
            // tarea pesada
            for (int i = 0; i < count; i++) {
                try {
                    Thread.sleep(500);
                    Log.d(TAG, "local service " + i + "de " + count); // LOG OBLIGATORIO
                } catch (InterruptedException e) {
                    // El hilo despierta al ser interrumpido en onDestroy()
                    // Ya que tiene que estar despierto para cancelarse...
                    // O se podria comprobar el estado del servizio cada vez que despierta el hilo
                    // Si el servizio esta out entonces el hilo se automatará
                    // Si estaba despierto cuando vuelva a dormir saltará la excepción...
                    Log.d(TAG, "Hilo destruido");
                    emision();
                    return;
                }
            }
            Log.d(TAG, "Hilo finalizado");
            emision();
            return;
        }
    }

    private void emision(){
        Intent intent = new Intent();
        intent.setAction(ACTION_LOCAL_SERVICE_FINISH);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "local service destroy");
        Toast.makeText(getApplicationContext(), R.string.local_service_destroy, Toast.LENGTH_SHORT).show();
        if(thread!=null){
            thread.interrupt();
        }
    }
}