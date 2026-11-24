package es.udc.psi_2025_26.p23quinteiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.net.URI;
import java.net.URL;

import es.udc.psi_2025_26.p23quinteiro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements TopFragment.OnTopFragmentSelectedListener, TextFragment.OnTextFragmentListener {
    // Web y Text se reemplazan, hay un replace.
    // Top siempre presente.
    // Cambiar orientaciones, si pongo la pantalla horizontal, ajustar layouts.
    // Las acciones de los fragmentos las haces la actividad y deben estar expuestas a través
    // de un método público, buenas prácticas.
    private ActivityMainBinding binding;
    private static final String TAG ="_TAG";

    private static final String TOP_FRAGM_TAG = "TOP_FRAGM";
    private static final String TEXT_FRAGM_TAG ="TEXT_FRAGM";
    private static final String WEB_FRAGM_TAG = "WEB_FRAGM";

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "pulsado botón back");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.but_positive_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick() " + getString(R.string.but_positive_dialog) + ": Saliendo...");
                        Toast.makeText(getApplicationContext(),R.string.but_positive_dialog_action, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.but_negative_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick() " + getString(R.string.but_negative_dialog) + ": Abortando salida...");
                        Toast.makeText(getApplicationContext(),R.string.but_negative_dialog_action, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        launchTopFragment();
    }

    @Override
    public void onTextListenerSelected(String text, boolean enabled) {
        Log.d(TAG, "Text event");
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Vemos si ya hay creado uno...
        TextFragment fragment = (TextFragment) fragmentManager.findFragmentByTag(TEXT_FRAGM_TAG);

        if(fragment != null){
            fragment.updateText(text);
            Log.d(TAG, "Actualizando TextFragm");


        }else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = TextFragment.newInstance(text, enabled);
            fragmentTransaction.replace(R.id.bottom_fragm, fragment, TEXT_FRAGM_TAG);
            fragmentTransaction.commit();
            Log.d(TAG, "Lanzando TextFragm dinámicamente");
        }

    }


    // Le tiene que llegar una url válida...
    // Comprobaciones responsabilidad del fragmento.
    @Override
    public void onUrlListener(URL url) {
        Log.d(TAG, "URL event");
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Buscamos si ya hay un WebFragment...
        WebFragment fragment = (WebFragment) fragmentManager.findFragmentByTag(WEB_FRAGM_TAG);

        if (fragment != null) {
            fragment.loadUrl(url); // Se asume url siempre válida no nula comprobación en TopFragment
        } else {
            // Al crearse por primera vez se compruba en WebFragment que la url está bien formada
            // Es correcto, ya que tiene que ser interoperable aq en el caso de nuestra aplicación
            // Siempre le llegará bien por simplicidad. Comprobación en el primero de la cadena
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = WebFragment.newInstance(url);
            fragmentTransaction.replace(R.id.bottom_fragm, fragment, WEB_FRAGM_TAG);
            fragmentTransaction.commit();
            Log.d(TAG, "Lanzando WebFragm dinámicamente");
        }

    }

    @Override
    public void onClearListener() {
        Log.d(TAG, "Clear event");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment textFragment = fragmentManager.findFragmentByTag(TEXT_FRAGM_TAG);
        Fragment webFragment = fragmentManager.findFragmentByTag(WEB_FRAGM_TAG);
        if (textFragment != null || webFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (textFragment != null) {
                fragmentTransaction.remove(textFragment);
                Log.d(TAG, "onClearListener: Eliminando TextFragment");
            }
            if (webFragment != null) {
                fragmentTransaction.remove(webFragment);
                Log.d(TAG, "onClearListener: Eliminando WebFragment");
            }
            fragmentTransaction.commit();
        }else{
            Log.d(TAG, "onClearListener: No hay ningun fragmento que eliminar.");
        }


    }

    void launchTopFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TopFragment fragment = new TopFragment();
        fragmentTransaction.replace(R.id.top_fragm, fragment, TOP_FRAGM_TAG);
        fragmentTransaction.commit();
        Log.d(TAG, "Lanzando TopFragm dinámicamente");
    }

    @Override
    public void onEnableClicked(boolean enabled) {
        Log.d(TAG, "Enable Clear event");
        FragmentManager fragmentManager = getSupportFragmentManager();
        TopFragment fragment = (TopFragment) fragmentManager.findFragmentByTag(TOP_FRAGM_TAG);
        if (fragment != null) {
            fragment.setClearButton(enabled);
        }
    }
}