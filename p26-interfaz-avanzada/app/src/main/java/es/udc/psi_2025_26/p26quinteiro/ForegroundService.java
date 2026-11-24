package es.udc.psi_2025_26.p26quinteiro;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

public class ForegroundService extends Service {

    private static final String TAG = "_TAG";
    public static final String ACTION_STOP = "es.udc.psi_2025_26.action.STOP_SERVICE";

    // Referencia al hilo del servizio
    // Importante para poder cancelarlo en el onDestroy() si llega el momento
    // También se usará para verificar si ya hay un servizio creado
    // Así se evita tener que crear una variable boolena
    // Pero hay que ponerlo a null en onDestroy
    // Escenarios:
    // 1 - Thread Muere por causas naturales (acaba la cuenta) al acabar el mismo llama a
    // --> stopForeground() && stopSelf()
    // stopself() llama a onDestroy()
    // onDestroy aqui no interrumpe al hilo
    // 2 - Se recoge el action del pending intent
    // stopForeground() y stopSelf()
    // stopSelf() llama a onDestroy() y este interrumpe el hilo para que retorne y muera
    CountThread thread = null;

    public int requestCode = 0;

    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null; // Va a ser un servizio local...
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // Se comprueba que no se quiere parar el servicio
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            stopForeground(STOP_FOREGROUND_REMOVE); // Retirar la notificación
            stopSelf(); // Llamar a onDestroy()
            return START_NOT_STICKY;
        }


        // Comprobación de si hay un servicio vivo
        // Esto es equivalanete a que exista un hilo vivo
        if(thread != null) {
            Log.d(TAG, getString(R.string.local_service_running));
            Toast.makeText(getApplicationContext(), R.string.local_service_running, Toast.LENGTH_SHORT).show();
            return START_STICKY; // Devuelve el servicio que se instancio antes y no crea uno nuevo
        }
        else {
            Log.d(TAG, getString(R.string.local_service_created));
            Toast.makeText(getApplicationContext(), R.string.local_service_created, Toast.LENGTH_SHORT).show();
        }

        // Se crea el pendingIntent
        Intent stopIntent = new Intent(this, ForegroundService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(
                this, requestCode, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.foreground_service_active))
                .setContentText(getString(R.string.foreground_service_action_stop))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(stopPendingIntent)
                .setAutoCancel(true)
                .build();

        ServiceCompat.startForeground(
                this, // servicio
                startId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        );




        // Se saca del intent hasta cuando contar...
        // Si no se tiene la clave COUNT se pone como valor por defecto DEFAULT
        int count = MainActivity.DEFAULT_COUNT;
        if (intent != null) {
            count = intent.getIntExtra(MainActivity.COUNT, MainActivity.DEFAULT_COUNT);
        }

        // Desacoplado del servicio
        thread = new CountThread(count); // El servizio crea su hilo al crearse
        // Los permisos ya han sido comprobados antes... Al lanzar el servizio
        thread.start();

        return START_STICKY;
    }

    class CountThread extends Thread {
        private final int count;

        CountThread(int count) {
            this.count = count;
        }

        public void run() {
            // tarea pesada
            for (int i = 0; i < count; i++) {
                try {
                    Thread.sleep(1000);
                    Log.d(TAG, "local service " + i + "de " + count); // LOG OBLIGATORIO
                } catch (InterruptedException e) {
                    // El hilo despierta al ser interrumpido en onDestroy()
                    // Ya que tiene que estar despierto para cancelarse...
                    // O se podria comprobar el estado del servizio cada vez que despierta el hilo
                    // Si el servizio esta out entonces el hilo se automatará
                    // Si estaba despierto cuando vuelva a dormir saltará la excepción...
                    Log.d(TAG, "Hilo destruido");
                    return;
                }
            }
            Log.d(TAG, "Hilo finalizado");
            stopForeground(STOP_FOREGROUND_REMOVE); // Retira la notificación de la barra de estados
            stopSelf(); // Llama a onDestroy() para eliminar el servicio
            // return; no es necesario
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "local service destroy");
        Toast.makeText(getApplicationContext(), R.string.local_service_destroy, Toast.LENGTH_SHORT).show();
        if(thread!=null && thread.isAlive()){
            thread.interrupt();
        }
        thread = null; // Crítico !!
    }
}