package es.udc.psi_2025_26.p22quinteiro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.p22quinteiro.databinding.ActivityMainBinding;

/*
* OBSERVACIONES (práctica anterior):
* - Al internacionalizar las cadenas iguales como números no hace falta añadirlas al nuevo strings también.
* - Al internacionalizar tener en cuenta las regiones dentro de un mismo idioma
* */

// ATAJOS
// Alt + Retorno de Carro -> String Resources

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Quitar de themes el .NoActionBar y habilitar el modo de superposición
    // Crear los tres botones con sus strings en recursos
    // Usar view bindings en vez d findById para referenciar elementos del layout: diapositiva 28 del primer tema.
    ActivityMainBinding binding; // El nombre se construye a partir del layout en modo UpperCammelCase y añadiendo Binding al final.

    private static final int REQUEST_CODE_REACT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Transforma los elementos visuales del xml en objetos java en memoria
        View view = binding.getRoot(); // Recupera la vista raíz en este caso LinearLayout
        setContentView(view); // Se establece ese contenido que se va a mostrar en la pantalla de la actividad

        Log.d("_TAG", "MainActivity: onCreate()");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // En el caso de haber muchos botones sería bueno hacer un método aparte de inicialización
        // Listener usando el implements en la clase de la actividad
        binding.butNext.setOnClickListener(this);
        binding.butReact.setOnClickListener(this);
        // Listener anónimo
        binding.butShare.setOnClickListener(new View.OnClickListener() {

            // Toast: recurso para mostrar información al usuario (incluso en segundo plano) es como un mensaje emergente
            // @ Parámetros: contexto == texto a mostrar(en recursos), duración == tiempo en pantalla
            // No se puede usar this ya que el espacio de nombres del listener y el onCreate es distinto
            // Solucion: que busqué el contexto global de la aplicación con el método usado abajo
            // Clase anónima no se puede autoreferenciar
            @Override
            public void onClick(View v) {
                Log.d("_TAG", "onClick() - Button Share");
                String text = binding.etInput.getText().toString();
                if(text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_string, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Para abrir una lista de aplicaciones necesitamos un intent implícito
                // ACTION_SEND -> Para que el usuario pueda compartir información mediante otra app
                Intent intentSend = new Intent(Intent.ACTION_SEND);
                intentSend.setType("text/plain"); // MIME del tipo de datos a enviar, así android sabrá que aplicaciones mostrar.
                intentSend .putExtra(Intent.EXTRA_TEXT, text); // Bundle con nuestro texto a enviar
                // Se usa clave estándar de Android (la pública).
                // Para mostrar la lista de apps (dinámico) del dispositivo usar createChooser:
                Intent intentShareApps = Intent.createChooser(intentSend, null);

                // NOTA!!!!
                // Para que podamos comprobar que desde el share al llamar con clave publica que
                // en efecto respondemos con clave pública, y tmb se actualize el botón clicado como
                // Si se hiciera desde el botón react... (aq llamada privada funcionamiento el mismo)
                // Usaríamos:
                //startActivityForResult(intentShareApps, REQUEST_CODE_REACT);
                // En el caso de que la aplicación sólo reciba el texto, no pasa nada.
                // Ya que no será RESULT_OK y el método onActityResult no hará nada.
                // En vez de usar startActivity usaríamos startActivityForResult.
                startActivity(intentShareApps);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.but_next){
            Log.d("_TAG", "onClick() - Button Next");
            String text = binding.etInput.getText().toString();
            // Chequear que el contenido no sea nulo de edit_text
            if(text.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.empty_string, Toast.LENGTH_SHORT).show();
                return;
            }
            // Intent explícito a destino concreto, usamos clave privada propia
            Intent intent = new Intent(getApplicationContext(), NextActivity.class);
            intent.putExtra( "MY_KEY", text);
            //intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(intent);
        } else if (v.getId() == R.id.but_react) {
            Log.d("_TAG", "onClick() - Button React");
            String text = binding.etInput.getText().toString();
            if(text.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.empty_string, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getApplicationContext(), ReactActivity.class);
            //intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.putExtra("MY_KEY", text);

            startActivityForResult(intent, REQUEST_CODE_REACT); // Está deprecada pero lo estamos haciendo de la forma antigua
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_REACT && resultCode == RESULT_OK) {
            // Recoger datos y utilizarlos
            if (data != null) {
                boolean publicCall = true; // Si llamamos arriba con clave privada nos responden con clave privada tmb.
                String buttonText = data.getStringExtra(Intent.EXTRA_TEXT);
                if(buttonText == null) {
                    buttonText = data.getStringExtra("MY_KEY");
                    publicCall = false;
                }

                String msg = getString(R.string.but_clicked) + buttonText;

                binding.tvOutput.setText(msg);
                Toast.makeText(getApplicationContext(), publicCall ?
                        R.string.toast_response_public : R.string.toast_response_private, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("_TAG", "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("_TAG", "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("_TAG", "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("_TAG", "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("_TAG", "MainActivity: onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("_TAG", "MainActivity: onRestart()");
    }
}