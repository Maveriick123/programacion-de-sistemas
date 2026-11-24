package es.udc.psi_2025_26.p25quinteiro;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.p25quinteiro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "_TAG";

    public static final String ACTION_GENERAL = "es.udc.psi_2025_26.broadcast.GENERAL";
    public static final String COUNT = "es.udc.psi_2025_26.COUNT";

    public static final int DEFAULT_COUNT = 10;

    public static final String ACTION_BTASK_FINISH = "es.udc.psi_2025_26.broadcast.BT_FINISH";


    BindService myBoundService; // Aquí se guardará la instancia del BindService
    Boolean mBound = false;

    private Thread bTaskThread;

    /** Defines callbacks for service binding, passed to bindService(). */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            // Recuperamos el binder del BindService
            BindService.MyBinder binder = (BindService.MyBinder) service;
            // A través de su método publico accedemos a getService() -> la instanica del BindService
            myBoundService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    ActivityMainBinding binding;

    private final BroadcastReceiver brPropio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_USER_PRESENT.equals(action)) {
                Log.d(TAG, "Aviso del sistema recibido");
                Toast.makeText(context, getString(R.string.br_receiver_action_receive, action), Toast.LENGTH_LONG).show();
            } else if (ACTION_GENERAL.equals(action)) {
                Log.d(TAG, "Emisión propia recibida");
                Toast.makeText(context, getString(R.string.br_receiver_action_receive, action), Toast.LENGTH_LONG).show();
            } else if (ACTION_BTASK_FINISH.equals(ACTION_BTASK_FINISH)) {
                Log.d(TAG, "Recibida emisión de fin de cuenta de la tarea en segundo plano");
                // Cambiamos el estado del switch a off cuando finaliza la cuenta
                binding.swBtask.setChecked(false);
            }
        }
    };

    private final BroadcastReceiver brLocalService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LocalService.ACTION_LOCAL_SERVICE_FINISH.equals(action)) {
                Log.d(TAG, "Recibida emisión de fin de cuenta del servicio local");
                // Cambiamos el estado del switch a off cuando finaliza la cuenta
                binding.swLocalService.setChecked(false);
            }
        }
    };

    private final BroadcastReceiver brBindService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BindService.ACTION_BIND_SERVICE_FINISH.equals(action)) {
                Log.d(TAG, "Recibida emisión de fin de cuenta del servicio enlazado");
                // Cambiamos el estado del switch a off cuando finaliza la cuenta
                binding.swBindService.setChecked(false);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setUI();
    }



    /*
    * Set User Interface
    */

    private void setUI(){


        binding.btnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_GENERAL);
                sendBroadcast(intent);
                Log.d(TAG, "onClick(): Enviando emisión propia");
            }
        });

        // ---------------------------------------------------------------------------------
        // Ponemos en el xml inputType=number
        // En algunos dispositivos acepta modos de decimales y con signo
        // Entonces para forzar a que el usuario tenga que introducir un entero usamos
        // TextWatcher para filtrar negativos y decimales en tiempo real
        binding.etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.contains("-") || text.contains(".")) {
                    int index = text.contains("-") ? text.indexOf("-") : text.indexOf(".");
                    s.delete(index, index + 1);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        // ----------------------------------------------------------------------------


        binding.swLocalService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean b) {
                if(b) {
                    Log.d(TAG, "local service true");
                    Intent service = new Intent(getApplicationContext(), LocalService.class);

                    int count = DEFAULT_COUNT;
                    String etCount = binding.etCount.getText().toString();
                    try{
                        if (!etCount.isEmpty()) {
                            int etCountInt = Integer.parseInt(etCount);
                            // Verificamos que sea positivo, aq con el textwathcer ya garantizamos que lo sea.
                            if (etCountInt > 0) {
                                count = etCountInt;
                            }
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_format, count), Toast.LENGTH_SHORT).show();
                    }
                    service.putExtra(COUNT,count);
                    startService(service);
                }else{
                    Log.d(TAG, "local service false");
                    // Se podria construir el intent en una función
                    Intent service = new Intent(getApplicationContext(), LocalService.class);
                    stopService(service);
                }
            }
        });

        binding.swBindService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            // 1- onCheckedChanged() y switch a true
            // 2- bindService() crea o encuentra el LocalService
            // 3- onServiceConnected() -> obtiene el myBoundService
            // 4- acceso a través de sus métodos públicos
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean b) {

                if(b) {
                    Log.d(TAG, "bind service true");
                    Intent intent = new Intent(getApplicationContext(), BindService.class);
                    /*
                     The given connection will receive the service object when it is created and be told if it dies and restarts.
                     The service will be considered required by the system only for as long as the calling context exists.
                     For example, if this Context is an Activity that is stopped, the service will not be required to continue running until the Activity is resumed
                    * */
                    // Método del op que llama a ActivityManagerService
                    // intent -> a quién
                    // conection -> que hacer cuando se conecte/desconecte
                    // Context.BIND_AUTO_CREATE -> crearlo si no existe
                    // Destacar que si no tiene componentes vinculados se destruye
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    int count = DEFAULT_COUNT;
                    String etCount = binding.etCount.getText().toString();
                    try{
                        if (!etCount.isEmpty()) {
                            int etCountInt = Integer.parseInt(etCount);
                            // Verificamos que sea positivo, aq con el textwathcer ya garantizamos que lo sea.
                            if (etCountInt > 0) {
                                count = etCountInt;
                            }
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_format, count), Toast.LENGTH_SHORT).show();
                    }
                    intent.putExtra(COUNT, count);
                    Log.d(TAG, "BindService enlazado, mBound: " + mBound.toString());

                }else{
                    Log.d(TAG, "bind service false");
                    unbindService(connection); // Se pone el flag mBound a false en el objeto connection
                    Log.d(TAG, "BindService desenlazado, mBound: " + mBound.toString());
                }
            }
        });

        binding.butSetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.swBindService.isChecked() && mBound ){
                    int count = DEFAULT_COUNT;
                    String etCount = binding.etCount.getText().toString();
                    try{
                        if (!etCount.isEmpty()) {
                            int etCountInt = Integer.parseInt(etCount);
                            // Verificamos que sea positivo, aq con el textwathcer ya garantizamos que lo sea.
                            if (etCountInt > 0) {
                                count = etCountInt;
                            }
                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_format, count), Toast.LENGTH_SHORT).show();
                    }
                    myBoundService.setCount(count);
                    Toast.makeText(getApplicationContext(), getString(R.string.count_bind_servize_update,count), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.bind_service_not_running), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.butGetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.swBindService.isChecked() && mBound){
                    Toast.makeText(getApplicationContext(), getString(R.string.count_bind_service, myBoundService.getCount(), 0), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.bind_service_not_running), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.swBtask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean b) {
                if(b){
                    Log.d(TAG, "btask true");
                    int count = DEFAULT_COUNT;
                    String etCount = binding.etCount.getText().toString();
                    try {
                        if (!etCount.isEmpty()) {
                            int etCountInt = Integer.parseInt(etCount);
                            if (etCountInt > 0) {
                                count = etCountInt;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_format, count), Toast.LENGTH_SHORT).show();
                    }

                    final int c = count;

                    bTaskThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < c; i++) {
                                try {
                                    Thread.sleep(500);
                                    showData(String.valueOf(i));
                                    Log.d(TAG, "CountThread: i = " + i + " de " + c);

                                } catch (InterruptedException e) {
                                    Log.d(TAG, "Hilo interrumpido y finalizado");
                                    Thread.currentThread().interrupt();
                                    return;
                                }
                            }
                            Log.d(TAG, "Hilo de btask finalizado");

                            Intent intent = new Intent();
                            intent.setAction(ACTION_BTASK_FINISH);
                            sendBroadcast(intent);
                        }
                    });

                    bTaskThread.start();
                }else{
                    Log.d(TAG, "btask false");
                    if (bTaskThread != null && bTaskThread.isAlive()) {
                        bTaskThread.interrupt();
                    }
                }
            }
        });
    }

    // Así el hilo secundario puede actualizar la UI del hilo principal
    private void showData(String data) {
        binding.tvBackgroundTask.post(new Runnable() {
            @Override
            public void run() {
                binding.tvBackgroundTask.setText(data);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Registrar el receptor de emisiones propias
        IntentFilter filterPropio = new IntentFilter();
        // Nos subscribimos a los dos eventos...
        filterPropio.addAction(Intent.ACTION_USER_PRESENT);
        filterPropio.addAction(ACTION_GENERAL);
        // Nos subscribimos a la emisión que lanza el hilo secundario de la actividad cuando acaba
        filterPropio.addAction(ACTION_BTASK_FINISH);
        // A partir del API 33 se obliga a añadir a mayores de lo de las diapositivas el contexto
        registerReceiver(brPropio, filterPropio, Context.RECEIVER_EXPORTED);


        // Registrar receptor de emisiones del local service
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(LocalService.ACTION_LOCAL_SERVICE_FINISH);
        registerReceiver(brLocalService, filterLocal, Context.RECEIVER_EXPORTED);

        // Registrar receptor de emisiones del bind service
        IntentFilter filterEnlazado = new IntentFilter();
        filterEnlazado.addAction(BindService.ACTION_BIND_SERVICE_FINISH);
        registerReceiver(brBindService, filterEnlazado, Context.RECEIVER_EXPORTED);

    }


    @Override
    protected void onPause() {
        // Desregistrar receptor, en el equivalente del ciclo de vida
        super.onPause();
        unregisterReceiver(brPropio);
        unregisterReceiver(brLocalService);
        unregisterReceiver(brBindService);
    }
}