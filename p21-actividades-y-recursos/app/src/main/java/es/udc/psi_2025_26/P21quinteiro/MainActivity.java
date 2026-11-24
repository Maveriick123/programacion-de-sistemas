package es.udc.psi_2025_26.P21quinteiro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.P21quinteiro.R;

public class MainActivity extends AppCompatActivity {
    // Sobrecargar todos los métodos del ciclo de vida de la actividad MainActivity
    // Mostrar nombre de la actividad y del método...
    private static final String TAG = "MainActivity";
    Button but_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Configuración de la vista de la actividad.
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: onCreate()");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Busca todos los ids, puede coger el de actividades que no están corriendo.
        // Entonces compila, pero posible error de ejecución.
        but_main = findViewById(R.id.but_main);
        // Suscripción al evento clicar el botón.
        // Creamos una clase anónima para implementar la interfaz View.OnClickListener()
        but_main.setOnClickListener(new View.OnClickListener() {
            // Cuando el usuario toque el botón actuará este código
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click Botón");
                ((Button) v).setText(R.string.boton);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity: onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy()");
    }
}