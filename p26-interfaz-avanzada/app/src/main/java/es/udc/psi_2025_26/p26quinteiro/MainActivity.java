package es.udc.psi_2025_26.p26quinteiro;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import es.udc.psi_2025_26.p26quinteiro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "_TAG";
    public static final String CHANNEL_ID = "es.udc.psi_2025_26.CHANNEL_ID";
    public static final int NOTIFICATION_ADD_ID = 101;
    public static final String EXTRA_BUTTON_NAME = "es.udc.psi_2025_26.EXTRA_BUTTON_NAME";

    public int requestCodeButAdd;

    public static final int REQUEST_CODE_ADD = 12000;
    public static final int REQUEST_CODE_FGS = REQUEST_CODE_ADD + 1;

    public static final String COUNT = "es.udc.psi_2025_26.COUNT";
    public static int DEFAULT_COUNT = 10;

    ActivityMainBinding binding;

    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Toda notificación debe pertenecer a un canal si no no se mostrará
        // Entonces lo primero que se hace al crear la aplicación es crear el canal también
        createNotificationChannel();

        // Mostrar en el textview de la actividad el botón pulsado en la notificación generada por add()
        String buttonName = getIntent().getStringExtra(EXTRA_BUTTON_NAME);
        if (buttonName != null) {
            binding.tvPrincipal.setText(buttonName);
        }

        initListView();
        // Registrar el listView para el menú contextual
        // Se asigna el menú para todos sus elementos
        registerForContextMenu(binding.listView);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // Cómo de intrusiva será la notificación para el usuario
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notifManager = getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(channel);
        }
    }

    // Se definen todos los elementos de un menú en un recurso menú. Prácticas recomendadas por la doc, ya que:
    // I - Fácilmente visualizable
    // II - Separar el contenido del menú del código de comportamiento de la app
    // III - Permite realizar configuraciones alternativas

    // La actividad crea el menú de opciones inflando el xml definido en recursos...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // Cuando se clica en una opción del menú, se lanza su código asociado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            Toast.makeText(getApplicationContext(), getString(R.string.menu_add), Toast.LENGTH_SHORT).show();
            add();
        } else if (item.getItemId() == R.id.menu_snack) {
            Toast.makeText(getApplicationContext(), getString(R.string.menu_snack), Toast.LENGTH_SHORT).show();
            snack();
        } else if (item.getItemId() == R.id.extended_menu_clear) {
            // Cancelar la notificación activa, para lo que se necesita el id de esta
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ADD_ID);
            Toast.makeText(getApplicationContext(), getString(R.string.menu_clear), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.extended_menu_msg) {
            // Hecho
            // Ya no se hace función aparte, ya que sólo es hacer un toast
            Toast.makeText(getApplicationContext(), getString(R.string.menu_msg), Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.extended_menu_fgs) {
            fgs();
            Toast.makeText(getApplicationContext(), getString(R.string.menu_fgs), Toast.LENGTH_SHORT).show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }



    // Al clicar el item add del menú se llamará a esta función
    // La cuál se encarga de lanzar una notificación con icono, titulo y tres botones
    // Todos los botones lanzan la actividad principal cuando son clicados
    // Antes de enviar la notificación se comprueban los permisos
    // El elemento del menú clear cancela la notificación
    private void add(){
        launchNotifyWithButtons();
    }

    private void launchNotifyWithButtons(){

        requestCodeButAdd = 0;

        String buttonName1 = getString(R.string.add_notification_but_x, requestCodeButAdd);
        String buttonName2 = getString(R.string.add_notification_but_x, requestCodeButAdd + 1 );
        String buttonName3 = getString(R.string.add_notification_but_x, requestCodeButAdd + 2);

        PendingIntent pendingIntentBut1 = createNotifyButtons(buttonName1, requestCodeButAdd);
        PendingIntent pendingIntentBut2 = createNotifyButtons(buttonName2, requestCodeButAdd + 1 );
        PendingIntent pendingIntentBut3 = createNotifyButtons(buttonName3, requestCodeButAdd + 2);

        // Contrucción de la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.notification_add_title))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_send, buttonName1, pendingIntentBut1)
                .addAction(android.R.drawable.ic_menu_send, buttonName2, pendingIntentBut2)
                .addAction(android.R.drawable.ic_menu_send, buttonName3, pendingIntentBut3);

        // Se comprueba en tiempo de ejecución si tenemos el permiso POST_NOTIFICATION
        // Si no se gestiona de forma activa abriendo un diálogo para que el usuario acepte o no (función requestPermissions)
        // Con el resultado automáticamente se llama a onRequestPermissionResult, la cual hay que sobrecargar
        // Sino doy permiso dos veces ya no se pide más comprobación activa
        // Es la forma de trabajo de Android
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(NOTIFICATION_ADD_ID, builder.build());
            }else{
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_ADD);

            }
        }
    }

    /*
     * Función para generar los pendingIntents de cada botón...
     *
     * Es importante usar request code distintos para cada botón
     * Ya que de por sí Android considera que intents en los que solo cambia el extra son el mismo
     * Entonces al clicar cualquier botón se aplicaría el extra del último
     * */
    private PendingIntent createNotifyButtons(String buttonName, int requestCode){
        Intent intentBut1 = new Intent(this, MainActivity.class);
        intentBut1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentBut1.putExtra(EXTRA_BUTTON_NAME, buttonName);
        return PendingIntent.getActivity(
                this, requestCode, intentBut1,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){

            case REQUEST_CODE_ADD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    add();
                } else {
                    // Si el usuario no da el permiso se gestiona enviando un toast...
                    Toast.makeText(getApplicationContext(), getString(R.string.notification_permission_denied), Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_FGS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED /*&& grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED*/){
                    fgs();
                }else{
                    // Si el usuario no da el permiso se gestiona enviando un toast...
                    Toast.makeText(getApplicationContext(), R.string.permission_denied_fgs, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void snack(){
        // Es un toast con esteroides, i.e: le podemos añadir acciones
        Snackbar mySnackbar = Snackbar.make(binding.main, getString(R.string.menu_snack), Snackbar.LENGTH_SHORT);
        mySnackbar.setAction(R.string.snackbar_log_action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.snackbar_log_action), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick() log: " + getString(R.string.snackbar_log_action));
            }
        });

        mySnackbar.show();
    }

    private void initListView(){
        // El enunciado pide 7 elementos, asi que hasta el elemento 6 (0, 1, ..., 6)
        String[] data = {getString(R.string.zero_lv), getString(R.string.one_lv), getString(R.string.two_lv), getString(R.string.three_lv), getString(R.string.four_lv), getString(R.string.five_lv), getString(R.string.six_lv)};
        dataList = new ArrayList<>(Arrays.asList(data));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        binding.listView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.context_menu_delete) {
            // 1. Guardar referencia del elemento y su posición (UNDO)

            if (info == null) {
                return super.onContextItemSelected(item);
            }
            final int itemPosition = info.position;
            final String itemInfo = dataList.get(itemPosition);

            // Borrar elemento de la lista
            dataList.remove(itemPosition);
            // Notificar al adaptador
            adapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), getString(R.string.delete_item_context_menu, itemPosition), Toast.LENGTH_SHORT).show();

            // Snackbar con Undo
            Snackbar.make(binding.main, getString(R.string.delete_item_action_context_menu,itemPosition), Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_snackbar, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Durante la ventana de sincronía se puede deshacer el cambio
                            dataList.add(itemPosition, itemInfo);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), getString(R.string.undo_action, itemPosition), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();

            return true;
        } else if (item.getItemId() == R.id.context_menu_share) {
            if (info == null) {
                return super.onContextItemSelected(item);
            }
            final int itemPosition = info.position;
            Toast.makeText(getApplicationContext(), getString(R.string.context_menu_share_action, itemPosition), Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }



    //Field requires API level 34 (current min is 26): `android.Manifest.permission#FOREGROUND_SERVICE_SPECIAL_USE`
    //Field requires API level 28 (current min is 26): `android.Manifest.permission#FOREGROUND_SERVICE`
    //Field requires API level 33 (current min is 26): `android.Manifest.permission#POST_NOTIFICATIONS`

    // Para que sea retrocompatible...

    private void fgs(){
        boolean permissionsGranted = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
            }
        }

        // Al ser permisos de tipo normal no hace falta comprobarlos en tiempo de ejecución
        // Se conceden al instalar la aplicación y estar declarados en el manifest
        // ...
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
            }
        }*/

        if (permissionsGranted) {
            Intent service = new Intent(this, ForegroundService.class);
            // Aqui se podria poner un putExtra con la clave COUNT que se saque de un editText...
            // Como el enunciado no lo pide explícitamente,entonces al no pasarse nada por defecto el
            // servicio contará hasta 10, ver ForegorundService.java
            startForegroundService(service);
        } else {
            requestPermissions(getPermissionsToRequest(), REQUEST_CODE_FGS);
        }
    }

    private String[] getPermissionsToRequest() {
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissionsToRequest.add(Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE);
        }*/

        return permissionsToRequest.toArray(new String[0]);
    }

}