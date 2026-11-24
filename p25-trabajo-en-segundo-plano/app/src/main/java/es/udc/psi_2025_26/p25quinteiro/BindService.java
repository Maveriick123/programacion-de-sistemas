package es.udc.psi_2025_26.p25quinteiro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BindService extends Service {

    public static final String ACTION_BIND_SERVICE_FINISH = "es.udc.psi_2025_26.broadcast.BS_FINISH";


    //public BindService() {}
    private static final String TAG = "_TAG";
    CountThread thread;


    private final IBinder binder = new MyBinder();

    // Inner class del BindService
    public class MyBinder extends Binder {
        BindService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return BindService.this;
        }
    }


    public class CountThread extends Thread{
        private int i;
        CountThread(int count) {
            this.i = count;
        }

        public int getThreadCount(){
            return i;
        }

        public void setThreadCount(int newCount){
            i = newCount;
        }


        public void run() {
            // tarea pesada
            for (; i > 0; i--) {
                try {
                    Thread.sleep(500);
                    Log.d(TAG, "bind service " + i + "hasta " + 0); // LOG OBLIGATORIO
                } catch (InterruptedException e) {
                    // El hilo despierta al ser interrumpido en onDestroy()
                    // Ya que tiene que estar despierto para cancelarse...
                    // O se podria comprobar el estado del servizio cada vez que despierta el hilo
                    // Si el servizio esta out entonces el hilo se automatará
                    // Si estaba despierto cuando vuelva a dormir saltará la excepción...
                    Log.d(TAG, "Hilo destruido cuando estaba durmiendo");
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
        intent.setAction(ACTION_BIND_SERVICE_FINISH);
        sendBroadcast(intent);
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");

        Log.d (TAG, getString(R.string.bind_service_running));
        Toast.makeText(getApplicationContext(), R.string.bind_service_running, Toast.LENGTH_SHORT).show();
        int count;
        // Se saca del intent hasta cuando contar...
        // Si no se tiene la clave COUNT se pone como valor por defecto DEFAULT

        count = intent.getIntExtra(MainActivity.COUNT, MainActivity.DEFAULT_COUNT);
        // Desacoplado del servicio
        if(thread == null){
            thread = new BindService.CountThread(count);
            thread.start();
        }
        return binder;
    }

    // Métodos públicos para los clientes

    public int getCount(){
        return thread.getThreadCount();
    }

    public void setCount(int newCount){
        thread.setThreadCount(newCount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(thread != null){
            thread.interrupt();
        }
    }
}