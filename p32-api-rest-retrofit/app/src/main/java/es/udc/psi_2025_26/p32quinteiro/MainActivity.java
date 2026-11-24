package es.udc.psi_2025_26.p32quinteiro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import es.udc.psi_2025_26.p32quinteiro.databinding.ActivityMainBinding;
import es.udc.psi_2025_26.p32quinteiro.domain.Moto;

// Los virus necesitan un huesped
// Es decir los fragmentos para existir necesitan una actividad

// 1. Permiso de internet en el manifest
// 2. Gradle módulo, dependencias y sacar api-key del local.properties
// Tmb activar en buildFeatures viewBinding y buildConfig

// La actividad tiene que implementar la interfaz del fragmento
// En este caso el método onMotoSeleccionada

public class MainActivity extends AppCompatActivity
        implements ListaMotosFragment.OnMotoSeleccionadaListener {

    public static final String TAG = "_TAG";

    // Para localizar el fragmento con findFragmentById
    private static final String TAG_LISTA = "lista";
    private static final String TAG_DETALLE = "detalle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityMainBinding binding;
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Los cambios de configuración
        // Como girar la pantall reinicia la actividad
        // Es decir que si se gira la pantalla
        // Se restaura el fragmento que estaba y no se vuelve a crear
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListaMotosFragment fragment = new ListaMotosFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_LISTA);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onMotoSeleccionada(Moto moto) {
        Log.d(TAG, "Abriendo detalle de " + moto.getMake() + " " + moto.getModel());
        DetalleMotoFragment fragment =
                DetalleMotoFragment.newInstance(moto.getMake(), moto.getModel(), moto.getYear());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, TAG_DETALLE);
        // Para que al volver atras no se destruya el fragmento
        // Así el usuario puede volver a el
        // No se destruye la lista solo la vista (binding=null)
        // Pero el objeto sigue vivo
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
